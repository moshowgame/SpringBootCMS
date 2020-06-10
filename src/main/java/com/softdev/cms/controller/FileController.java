package com.softdev.cms.controller;

import com.alibaba.fastjson.JSON;
import com.softdev.cms.entity.exception.StorageFileNotFoundException;
import com.softdev.cms.service.StorageService;
import com.softdev.cms.util.ReturnT;
import com.softdev.cms.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/file")
@Slf4j
/**
 * SpringBoot2FileUpload文件上传
 * @author zhengkai.blog.csdn.net
 * */
public class FileController {

	@Autowired
    private StorageService storageService;
    
    @GetMapping("/upload")
    public ModelAndView upload(){
        return new ModelAndView("cms/fileupload");
    }

	@GetMapping("/files")
	public ModelAndView listUploadedFiles(ModelAndView modelAndView) throws IOException {
		//返回目录下所有文件信息
		modelAndView.addObject("files", storageService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
						"serveFile", path.getFileName().toString()).build().toString())
				.collect(Collectors.toList()));
		//返回目录信息
		modelAndView.addObject("path",storageService.getPath());
		modelAndView.setViewName("uploadForm");
		//查看ModelAndView包含的内容
		System.out.println(JSON.toJSONString(modelAndView));
		return modelAndView;
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws UnsupportedEncodingException {
		//加载文件
		Resource file = storageService.loadAsResource(filename);
		log.info("download file:"+file.getFilename());
		//attachment附件下载模式,直接下载文件
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + URLEncoder.encode(file.getFilename(), "UTF-8") + "\"").body(file);
	}

    @PostMapping("/files")
    public ReturnT handleFileUpload(@RequestParam("file") MultipartFile file) {
        String fileName=System.currentTimeMillis()+file.getOriginalFilename();
        //存储文件
		storageService.store(file,fileName);
        //返回成功消息
        return ReturnT.SUCCESS(fileName);
    }

	/**
	 * FileUpload文件上传 For LayEdit
	 * @author zhengkai.blog.csdn.net
	 * */
	@PostMapping("/layuiUpload")
	public ReturnT layuiUpload(@RequestParam("file") MultipartFile file) {
    	//自定义文件名
		String fileName=System.currentTimeMillis()+file.getOriginalFilename();
		//存储文件
		storageService.store(file,fileName);
		//封装返回显示的url和文件名
		Map returnMap = new HashMap();
		returnMap.put("src", StringUtils.SYSTEM_PATH+"/file/files/"+fileName);
		returnMap.put("title",fileName);
		//返回成功消息
		return ReturnT.DEFINE(ReturnT.PAGE_CODE,"上传成功",returnMap);
	}
	/**
	 * base64保存为图片
	 * @author zhengkai.blog.csdn.net
	 * */
	@PostMapping("/base64upload")
	public ReturnT base64upload(String base64String){
		//log.info((base64String!=null)?base64String.length()+"":"null");

		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		//自定义文件名
		String fileName=System.currentTimeMillis()+"_img_upload.png";
		StringUtils.generateImage(base64String,storageService.getPathString()+fileName);
		Map returnMap = new HashMap();
		returnMap.put("src", StringUtils.SYSTEM_PATH+"/file/files/"+fileName);
		returnMap.put("title",fileName);
		//返回成功消息
		return ReturnT.DEFINE(ReturnT.PAGE_CODE,"上传成功",returnMap);
	}
	/**
	 * 保存网络图片
	 * @author zhengkai.blog.csdn.net
	 * */
	@PostMapping("/saveNetworkImg")
	public ReturnT saveNetworkImg(String imgUrl){
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		//自定义文件名
		String fileName=System.currentTimeMillis()+"_img_upload.png";
		downloadPicture(imgUrl,storageService.getPathString()+fileName);
		Map returnMap = new HashMap();
		returnMap.put("src", StringUtils.SYSTEM_PATH+"/file/files/"+fileName);
		returnMap.put("title",fileName);
		//返回成功消息
		return ReturnT.DEFINE(ReturnT.PAGE_CODE,"上传成功",returnMap);
	}
	private static void downloadPicture(String urlList,String fileName) {
		URL url =null;
		int imageNumber = 0;
		try {
		  url =new URL(urlList);
		  DataInputStream dataInputStream =new DataInputStream(url.openStream());
		  FileOutputStream fileOutputStream =new FileOutputStream(new File(fileName));
		  ByteArrayOutputStream output =new ByteArrayOutputStream();
		  byte[] buffer =new byte[1024];
		  int length;
		  while((length = dataInputStream.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		  }
		  byte[] context=output.toByteArray();
		  fileOutputStream.write(output.toByteArray());
		  dataInputStream.close();
		  fileOutputStream.close();
		} catch (MalformedURLException e) {
		  e.printStackTrace();
		} catch (IOException e) {
		  e.printStackTrace();
		}
  }

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}

