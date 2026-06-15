package com.softdev.cms.controller;

import com.softdev.cms.entity.exception.StorageFileNotFoundException;
import com.softdev.cms.service.StorageService;
import com.softdev.cms.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Autowired
    private StorageService storageService;

    @Value("${server.servlet.context-path:/cms}")
    private String contextPath;

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
        Resource file = storageService.loadAsResource(filename);
        log.info("download file:{}", file.getFilename());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + URLEncoder.encode(file.getFilename(), "UTF-8") + "\"").body(file);
    }

    @PostMapping("/files")
    public Result<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String fileName = System.currentTimeMillis() + file.getOriginalFilename();
        storageService.store(file, fileName);
        return Result.success(fileName);
    }

    @PostMapping("/layuiUpload")
    public Result<Map<String, String>> layuiUpload(@RequestParam("file") MultipartFile file) {
        String fileName = System.currentTimeMillis() + file.getOriginalFilename();
        storageService.store(file, fileName);
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("src", contextPath + "/file/files/" + fileName);
        returnMap.put("title", fileName);
        return Result.success(returnMap);
    }

    @PostMapping("/base64upload")
    public Result<Map<String, String>> base64upload(@RequestParam String base64String) {
        String fileName = System.currentTimeMillis() + "_img_upload.png";
        String imageData = base64String.replace("data:image/png;base64,", "");
        generateImage(imageData, storageService.getPathString() + fileName);
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("src", contextPath + "/file/files/" + fileName);
        returnMap.put("title", fileName);
        return Result.success(returnMap);
    }

    @PostMapping("/saveNetworkImg")
    public Result<Map<String, String>> saveNetworkImg(@RequestParam String imgUrl) {
        String fileName = System.currentTimeMillis() + "_img_upload.png";
        downloadPicture(imgUrl, storageService.getPathString() + fileName);
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("src", contextPath + "/file/files/" + fileName);
        returnMap.put("title", fileName);
        return Result.success(returnMap);
    }

    private void downloadPicture(String urlList, String fileName) {
        try {
            URL url = new URL(urlList);
            try (DataInputStream dataInputStream = new DataInputStream(url.openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName));
                 ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = dataInputStream.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                fileOutputStream.write(output.toByteArray());
            }
        } catch (IOException e) {
            log.error("download picture failed", e);
        }
    }

    private boolean generateImage(String imgStr, String imgFilePath) {
        if (imgStr == null) return false;
        try {
            byte[] bytes = Base64.getDecoder().decode(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
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
