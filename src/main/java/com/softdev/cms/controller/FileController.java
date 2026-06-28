package com.softdev.cms.controller;

import com.softdev.cms.entity.Media;
import com.softdev.cms.entity.SiteConfig;
import com.softdev.cms.entity.exception.StorageFileNotFoundException;
import com.softdev.cms.mapper.MediaMapper;
import com.softdev.cms.mapper.SiteConfigMapper;
import com.softdev.cms.service.StorageService;
import com.softdev.cms.util.Result;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private SiteConfigMapper siteConfigMapper;

    @Value("${server.servlet.context-path:/cms}")
    private String contextPath;

    /** 默认允许上传的图片扩展名（当未从 site_config 读取时使用） */
    private static final Set<String> DEFAULT_ALLOWED_IMG_EXT = Set.of("jpg", "jpeg", "png", "gif", "bmp", "webp");

    /** 默认最大文件大小 10MB */
    private static final long DEFAULT_MAX_FILE_SIZE = 10 * 1024 * 1024;

    /** 允许上传的图片 MIME 类型前缀 */
    private static final List<String> ALLOWED_IMG_MIME = List.of("image/");

    /** 图片文件魔数（文件头签名） */
    private static final Map<String, byte[]> IMAGE_MAGIC_NUMBERS = new HashMap<>();
    static {
        // JPEG: FF D8 FF
        IMAGE_MAGIC_NUMBERS.put("jpg", new byte[]{(byte)0xFF, (byte)0xD8, (byte)0xFF});
        IMAGE_MAGIC_NUMBERS.put("jpeg", new byte[]{(byte)0xFF, (byte)0xD8, (byte)0xFF});
        // PNG: 89 50 4E 47 0D 0A 1A 0A
        IMAGE_MAGIC_NUMBERS.put("png", new byte[]{(byte)0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A});
        // GIF: 47 49 46 38
        IMAGE_MAGIC_NUMBERS.put("gif", new byte[]{0x47, 0x49, 0x46, 0x38});
        // BMP: 42 4D
        IMAGE_MAGIC_NUMBERS.put("bmp", new byte[]{0x42, 0x4D});
        // WebP: 52 49 46 46 xx xx xx xx 57 45 42 50
        IMAGE_MAGIC_NUMBERS.put("webp", new byte[]{0x52, 0x49, 0x46, 0x46});
    }

    // ==================== 页面 ====================

    @GetMapping("/upload")
    public ModelAndView upload() {
        return new ModelAndView("cms/fileupload");
    }

    @GetMapping("/files")
    public ModelAndView listUploadedFiles(ModelAndView modelAndView) throws IOException {
        modelAndView.addObject("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
        modelAndView.addObject("path", storageService.getPath());
        modelAndView.setViewName("uploadForm");
        return modelAndView;
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws UnsupportedEncodingException {
        // 路径遍历防护：拒绝包含 .. 或反斜杠的路径
        if (filename == null || filename.contains("..") || filename.contains("\\")
                || filename.startsWith("/") || filename.contains("\0")) {
            log.warn("拒绝非法路径的文件下载: {}", filename);
            return ResponseEntity.badRequest().build();
        }
        Resource file = storageService.loadAsResource(filename);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        log.info("download file:{}", file.getFilename());
        // 推断 Content-Type 并严格限制（防止存储型 XSS：HTML/SVG 不可作为附件）
        String contentType = "application/octet-stream";
        try {
            Path path = file.getFile().toPath();
            contentType = Files.probeContentType(path);
            if (contentType == null) contentType = "application/octet-stream";
        } catch (Exception e) {
            log.debug("probeContentType failed, use octet-stream", e);
        }
        // 禁止直接以 HTML/SVG/XML 等可执行脚本类型提供下载
        String lower = contentType.toLowerCase();
        if (lower.contains("html") || lower.contains("svg") || lower.contains("xml")
                || lower.contains("javascript") || lower.contains("ecmascript")) {
            log.warn("拦截可疑 MIME 类型的文件下载: filename={}, contentType={}", filename, contentType);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + URLEncoder.encode(file.getFilename(), "UTF-8") + "\"")
                .header("X-Content-Type-Options", "nosniff")
                .body(file);
    }

    // ==================== 通用文件上传 ====================

    @PostMapping("/files")
    public Result<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        // 1. 校验文件
        String validationError = validateImageFile(file);
        if (validationError != null) {
            return Result.fail(validationError);
        }

        // 2. 生成安全文件名（UUID + 扩展名）
        String originalFilename = file.getOriginalFilename();
        String fileName = generateSafeFileName(originalFilename != null ? originalFilename : "upload.png");

        // 3. 保存文件
        storageService.store(file, fileName);
        return Result.success(fileName);
    }

    // ==================== TinyMCE / 富文本编辑器统一上传接口 ====================

    /**
     * 富文本编辑器图片上传接口（兼容 TinyMCE、wangEditor 等）
     * <p>
     * 请求：multipart/form-data，字段名 "file"
     * 成功响应：{"url":"/cms/file/files/xxx.jpg"}
     * 失败响应：{"error":{"message":"原因"}}
     */
    @PostMapping("/editorUpload")
    public Map<String, Object> editorUpload(@RequestParam("file") MultipartFile file,
                                            HttpSession session) {
        try {
            // 1. 文件类型校验
            String validationError = validateImageFile(file);
            if (validationError != null) {
                return Map.of("error", Map.of("message", validationError));
            }

            // 2. 确保存储目录存在
            ensureUploadDirExists();

            // 3. 确定扩展名并生成安全文件名（UUID + 扩展名）
            String originalFilename = file.getOriginalFilename();
            String ext;
            if (originalFilename != null && !originalFilename.isBlank() && getExtension(originalFilename).length() > 0) {
                ext = getExtension(originalFilename);
            } else {
                // 无扩展名时从 MIME 推断
                ext = getExtensionFromMime(file.getContentType());
            }
            String fileName = UUID.randomUUID().toString() + (ext.isEmpty() ? "" : "." + ext);

            // 4. 保存文件
            storageService.store(file, fileName);
            String fileUrl = contextPath + "/file/files/" + fileName;

            // 5. 注册到 Media 表
            saveMediaRecord(file, fileName, fileUrl, ext, session);

            // 6. 返回 TinyMCE 兼容格式
            return Map.of("url", fileUrl);
        } catch (Exception e) {
            log.error("图片上传失败", e);
            return Map.of("error", Map.of("message", "上传异常: " + e.getMessage()));
        }
    }

    // ==================== LayUI 上传（向下兼容） ====================

    @PostMapping("/layuiUpload")
    public Result<Map<String, String>> layuiUpload(@RequestParam("file") MultipartFile file,
                                                    HttpSession session) {
        // 1. 校验文件
        String validationError = validateImageFile(file);
        if (validationError != null) {
            return Result.fail(validationError);
        }

        // 2. 生成安全文件名（UUID + 扩展名）
        String originalFilename = file.getOriginalFilename();
        String ext = getExtension(originalFilename != null ? originalFilename : "upload.png");
        String fileName = UUID.randomUUID().toString() + (ext.isEmpty() ? "" : "." + ext);

        // 3. 保存文件
        storageService.store(file, fileName);
        String fileUrl = contextPath + "/file/files/" + fileName;

        // 4. 注册到 Media 表
        saveMediaRecord(file, fileName, fileUrl, ext, session);

        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("src", fileUrl);
        returnMap.put("title", fileName);
        return Result.success(returnMap);
    }

    // ==================== Base64 图片上传（支持多种格式） ====================

    @PostMapping("/base64upload")
    public Result<Map<String, String>> base64upload(@RequestParam String base64String,
                                                     @RequestParam(defaultValue = "png") String ext,
                                                     HttpSession session) {
        // 校验扩展名（从 site_config 动态读取）
        Set<String> allowedExt = getAllowedExtensions();
        if (!allowedExt.contains(ext.toLowerCase())) {
            return Result.fail("不支持的图片格式: " + ext + "，允许: " + String.join(", ", allowedExt));
        }

        // 文件名使用 UUID + 扩展名
        String fileName = UUID.randomUUID().toString() + "." + ext;
        // 去掉 data:image/xxx;base64, 前缀（兼容带前缀和不带前缀的情况）
        String imageData = base64String;
        if (base64String.contains(",")) {
            imageData = base64String.substring(base64String.indexOf(",") + 1);
        }
        generateImage(imageData, storageService.getPathString() + fileName);

        String fileUrl = contextPath + "/file/files/" + fileName;

        // 注册到 Media 表
        Media media = new Media();
        media.setFileName(fileName);
        media.setFilePath(fileUrl);
        media.setFileSize((long) (imageData.length() * 0.75)); // base64 解码后约为此大小
        media.setFileType("image/" + ext);
        media.setFileExt(ext);
        media.setMediaType("image");
        media.setUploadUserId((Integer) session.getAttribute("userId"));
        media.setUploadUserName((String) session.getAttribute("showName"));
        mediaMapper.insert(media);

        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("src", fileUrl);
        returnMap.put("title", fileName);
        return Result.success(returnMap);
    }

    // ==================== 网络图片转存 ====================

    @PostMapping("/saveNetworkImg")
    public Result<Map<String, String>> saveNetworkImg(@RequestParam String imgUrl,
                                                       HttpSession session) {
        // 基础 URL 校验（防止 SSRF）
        if (imgUrl == null || (!imgUrl.startsWith("http://") && !imgUrl.startsWith("https://"))) {
            return Result.fail("无效的图片URL");
        }

        try {
            URL url = new URL(imgUrl);
            String host = url.getHost().toLowerCase();
            // 1) 校验初始 host 是否内网（防止 SSRF）
            if (isInternalHost(host)) {
                return Result.fail("不允许转存内网地址的图片");
            }
            // 1b) 解析 host 的 IP 并校验（防 DNS rebinding 绕过：解析时拿到的 IP 必须是公网）
            if (isInternalHostByIp(host)) {
                log.warn("SSRF防护：域名 {} 解析到内网 IP", host);
                return Result.fail("不允许转存内网地址的图片");
            }

            // 2) 使用安全的连接配置（禁用重定向，由我们手动校验重定向目标）
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            connection.setRequestProperty("Accept", "image/avif,image/webp,image/apng,image/*,*/*;q=0.8");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(15000);
            // 3) 禁用自动重定向（关键 SSRF 防护：避免被攻击者用公网 URL 跳转到内网）
            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection) connection).setInstanceFollowRedirects(false);
            }

            // 4) 如果遇到重定向（301/302/303/307/308），手动跟随并校验目标 host
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection httpConn = (HttpURLConnection) connection;
                int status = httpConn.getResponseCode();
                int maxRedirects = 3;
                int redirectCount = 0;
                while (isRedirectStatus(status) && redirectCount < maxRedirects) {
                    String location = httpConn.getHeaderField("Location");
                    httpConn.disconnect();
                    if (location == null || location.isEmpty()) {
                        return Result.fail("无效的重定向响应");
                    }
                    URL nextUrl = new URL(url, location);
                    String nextHost = nextUrl.getHost().toLowerCase();
                    if (isInternalHost(nextHost) || isInternalHostByIp(nextHost)) {
                        log.warn("SSRF防护：拦截到指向内网的重定向 {} -> {}", imgUrl, nextUrl);
                        return Result.fail("重定向目标不允许为内网地址");
                    }
                    URLConnection nextConn = nextUrl.openConnection();
                    nextConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                    nextConn.setRequestProperty("Accept", "image/avif,image/webp,image/apng,image/*,*/*;q=0.8");
                    nextConn.setConnectTimeout(5000);
                    nextConn.setReadTimeout(15000);
                    if (nextConn instanceof HttpURLConnection) {
                        ((HttpURLConnection) nextConn).setInstanceFollowRedirects(false);
                    }
                    connection = nextConn;
                    url = nextUrl;
                    httpConn = (HttpURLConnection) nextConn;
                    status = httpConn.getResponseCode();
                    redirectCount++;
                }
                if (isRedirectStatus(status)) {
                    return Result.fail("重定向次数过多（>3）");
                }
            }

            String contentType = connection.getContentType();
            int contentLength = connection.getContentLength();
            log.info("下载网络图片: {} (Content-Type: {}, Length: {})", imgUrl, contentType, contentLength);
            String ext = "png"; // 默认
            if (contentType != null) {
                if (contentType.contains("jpeg") || contentType.contains("jpg")) {
                    ext = "jpg";
                } else if (contentType.contains("gif")) {
                    ext = "gif";
                } else if (contentType.contains("webp")) {
                    ext = "webp";
                } else if (contentType.contains("bmp")) {
                    ext = "bmp";
                }
                // 出于安全考虑，不再支持 SVG（SVG 可包含 JS，可能造成存储 XSS）
            }
            // 也可以通过 URL 路径扩展名判断
            String pathExt = getExtension(imgUrl);
            Set<String> allowedExt = getAllowedExtensions();
            if (allowedExt.contains(pathExt)) {
                ext = pathExt;
            }

            // 文件名使用 UUID + 扩展名
            String fileName = UUID.randomUUID().toString() + "." + ext;
            downloadPicture(url, storageService.getPathString() + fileName);

            String fileUrl = contextPath + "/file/files/" + fileName;

            // 注册到 Media 表
            Media media = new Media();
            media.setFileName(fileName);
            media.setFilePath(fileUrl);
            media.setFileType(contentType != null ? contentType : "image/" + ext);
            media.setFileExt(ext);
            media.setMediaType("image");
            media.setUploadUserId((Integer) session.getAttribute("userId"));
            media.setUploadUserName((String) session.getAttribute("showName"));
            mediaMapper.insert(media);

            Map<String, String> returnMap = new HashMap<>();
            returnMap.put("src", fileUrl);
            returnMap.put("title", fileName);
            return Result.success(returnMap);
        } catch (IOException e) {
            log.error("转存网络图片失败: {}", imgUrl, e);
            return Result.fail("转存失败: " + e.getMessage());
        }
    }

    // ==================== 内部方法 ====================

    /**
     * 从 site_config 读取允许上传的文件类型
     */
    private Set<String> getAllowedExtensions() {
        SiteConfig config = siteConfigMapper.selectByKey("upload_allowed_types");
        if (config != null && config.getConfigValue() != null && !config.getConfigValue().isBlank()) {
            Set<String> exts = new HashSet<>();
            for (String ext : config.getConfigValue().split(",")) {
                exts.add(ext.trim().toLowerCase());
            }
            return exts;
        }
        return DEFAULT_ALLOWED_IMG_EXT;
    }

    /**
     * 从 site_config 读取最大文件大小（字节）
     */
    private long getMaxFileSize() {
        SiteConfig config = siteConfigMapper.selectByKey("upload_max_size");
        if (config != null && config.getConfigValue() != null) {
            try {
                return Long.parseLong(config.getConfigValue());
            } catch (NumberFormatException e) {
                log.warn("upload_max_size 配置值无效: {}", config.getConfigValue());
            }
        }
        return DEFAULT_MAX_FILE_SIZE;
    }

    /**
     * 生成安全的文件名：UUID + 原扩展名
     */
    private String generateSafeFileName(String originalFilename) {
        String ext = getExtension(originalFilename);
        return UUID.randomUUID().toString() + (ext.isEmpty() ? "" : "." + ext);
    }

    /**
     * 校验上传的图片文件
     *
     * @return 校验失败时的错误信息，成功返回 null
     */
    private String validateImageFile(MultipartFile file) {
        // 1. 文件空校验
        if (file.isEmpty()) {
            return "上传文件为空";
        }

        // 2. 文件大小校验
        long maxSize = getMaxFileSize();
        if (file.getSize() > maxSize) {
            return "文件大小超过限制: 最大 " + (maxSize / 1024 / 1024) + "MB";
        }

        // 3. 文件名和扩展名校验
        String originalFilename = file.getOriginalFilename();
        Set<String> allowedExt = getAllowedExtensions();

        if (originalFilename == null || originalFilename.isBlank()) {
            // 没有文件名时尝试从 MIME 类型推断
            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
                return null; // MIME 是图片类型，通过校验
            }
            return "文件名不能为空且无法从MIME类型判断";
        }

        // 扩展名校验 —— 如果无扩展名但 MIME 是图片类型，也通过
        String ext = getExtension(originalFilename);
        if (!allowedExt.contains(ext)) {
            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
                return null; // 无扩展名但 MIME 是图片，通过校验
            }
            return "不支持的文件类型: " + ext + "，允许: " + String.join(", ", allowedExt);
        }

        // 4. MIME 类型校验
        String contentType = file.getContentType();
        if (contentType != null && !contentType.isBlank()) {
            boolean mimeAllowed = ALLOWED_IMG_MIME.stream().anyMatch(contentType::startsWith);
            if (!mimeAllowed) {
                return "不支持的文件MIME类型: " + contentType;
            }
        }

        // 4b. 文件魔数校验（防止扩展名伪造/双扩展名绕过）
        if (ext != null && !ext.isEmpty() && IMAGE_MAGIC_NUMBERS.containsKey(ext)) {
            try (InputStream is = file.getInputStream()) {
                byte[] header = new byte[12];
                int read = is.read(header);
                if (read < 4) {
                    return "文件内容过小，不像是图片";
                }
                byte[] expected = IMAGE_MAGIC_NUMBERS.get(ext);
                for (int i = 0; i < expected.length; i++) {
                    if (header[i] != expected[i]) {
                        log.warn("文件魔数不匹配：扩展名 {}，实际头部: {}", ext, bytesToHex(header));
                        return "文件内容与扩展名不符，可能是伪装文件";
                    }
                }
            } catch (IOException e) {
                return "读取文件失败: " + e.getMessage();
            }
        }

        // 5. 校验文件名安全（防止路径遍历攻击）
        if (originalFilename.contains("..") || originalFilename.contains("/") || originalFilename.contains("\\")) {
            return "文件名包含非法字符";
        }

        return null;
    }

    /**
     * 确保存储目录存在
     */
    private void ensureUploadDirExists() {
        try {
            Files.createDirectories(storageService.getPath());
        } catch (IOException e) {
            throw new RuntimeException("无法创建上传目录: " + storageService.getPath(), e);
        }
    }

    /**
     * 从 MIME 类型推断扩展名
     */
    private String getExtensionFromMime(String mimeType) {
        if (mimeType == null) return "png";
        switch (mimeType) {
            case "image/jpeg": return "jpg";
            case "image/gif": return "gif";
            case "image/webp": return "webp";
            case "image/bmp": return "bmp";
            case "image/svg+xml": return "svg";
            default: return "png";
        }
    }

    /**
     * 保存 Media 记录到数据库
     */
    private void saveMediaRecord(MultipartFile file, String storedName, String fileUrl, String ext, HttpSession session) {
        try {
            Media media = new Media();
            media.setFileName(storedName);
            media.setFilePath(fileUrl);
            media.setFileSize(file.getSize());
            media.setFileType(file.getContentType() != null ? file.getContentType() : "image/" + ext);
            media.setFileExt(ext);
            media.setMediaType("image");
            media.setUploadUserId((Integer) session.getAttribute("userId"));
            media.setUploadUserName((String) session.getAttribute("showName"));
            mediaMapper.insert(media);
            log.debug("Media 记录已保存: {}", storedName);
        } catch (Exception e) {
            log.warn("保存 Media 记录失败(不影响上传): {}", storedName, e);
        }
    }

    /**
     * 获取文件扩展名（小写，不含点）
     */
    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 判断是否为内网地址（防止 SSRF）
     */
    private boolean isInternalHost(String host) {
        return host.equals("localhost")
                || host.equals("127.0.0.1")
                || host.equals("0.0.0.0")
                || host.startsWith("10.")
                || host.startsWith("172.16.")
                || host.startsWith("172.17.")
                || host.startsWith("172.18.")
                || host.startsWith("172.19.")
                || host.startsWith("172.20.")
                || host.startsWith("172.21.")
                || host.startsWith("172.22.")
                || host.startsWith("172.23.")
                || host.startsWith("172.24.")
                || host.startsWith("172.25.")
                || host.startsWith("172.26.")
                || host.startsWith("172.27.")
                || host.startsWith("172.28.")
                || host.startsWith("172.29.")
                || host.startsWith("172.30.")
                || host.startsWith("172.31.")
                || host.startsWith("192.168.")
                || host.startsWith("169.254.")            // 链路本地地址
                || host.startsWith("100.64.")              // CGNAT 共享地址
                || host.endsWith(".local")                  // mDNS
                || host.equals("[::1]")                     // IPv6 回环
                || host.equals("::1")
                || host.startsWith("fe80:")                 // IPv6 链路本地
                || host.startsWith("fc")                    // IPv6 私有
                || host.startsWith("fd")                    // IPv6 唯一本地
                || host.startsWith("0.0.0.0");
    }

    /**
     * 是否是 HTTP 重定向状态码
     */
    private boolean isRedirectStatus(int status) {
        return status == HttpURLConnection.HTTP_MOVED_PERM       // 301
            || status == HttpURLConnection.HTTP_MOVED_TEMP       // 302
            || status == HttpURLConnection.HTTP_SEE_OTHER        // 303
            || status == 307                                     // 307 Temporary Redirect
            || status == 308;                                    // 308 Permanent Redirect
    }

    /**
     * 字节数组转十六进制字符串（用于日志）
     */
    private String bytesToHex(byte[] bytes) {
        if (bytes == null) return "null";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(bytes.length, 12); i++) {
            sb.append(String.format("%02X ", bytes[i] & 0xFF));
        }
        return sb.toString().trim();
    }

    /**
     * 将域名解析为 IP 并检查所有解析结果（防 DNS rebinding 绕过）
     */
    private boolean isInternalHostByIp(String host) {
        try {
            java.net.InetAddress[] addresses = java.net.InetAddress.getAllByName(host);
            for (java.net.InetAddress addr : addresses) {
                if (isInternalHost(addr.getHostAddress())) {
                    return true;
                }
            }
        } catch (Exception ignored) {
            // DNS 解析失败视为可疑
            return true;
        }
        return false;
    }

    /**
     * 下载网络图片到本地
     */
    private void downloadPicture(URL url, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        // 确保父目录存在
        Files.createDirectories(path.getParent());
        try (InputStream in = url.openStream()) {
            Files.copy(in, path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Base64 解码生成图片文件
     */
    private boolean generateImage(String imgStr, String imgFilePath) {
        if (imgStr == null) return false;
        try {
            byte[] bytes = Base64.getDecoder().decode(imgStr);
            try (OutputStream out = new FileOutputStream(imgFilePath)) {
                out.write(bytes);
                out.flush();
            }
            return true;
        } catch (Exception e) {
            log.error("generate image failed", e);
            return false;
        }
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
