package com.softdev.cms.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.alibaba.fastjson.JSON;
import com.softdev.cms.entity.exception.*;
import com.softdev.cms.service.StorageService;
import com.softdev.cms.util.ReturnT;


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
	// public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file,
	// 		RedirectAttributes redirectAttributes) {
	// 	//存储文件
	// 	storageService.store(file);
	// 	//返回成功消息
	// 	redirectAttributes.addFlashAttribute("message",
	// 			"恭喜你,文件" + file.getOriginalFilename() + "上传成功!");
	// 	return new ModelAndView("redirect:/cms/files");
	// }

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}

