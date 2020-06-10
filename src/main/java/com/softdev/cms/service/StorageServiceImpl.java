package com.softdev.cms.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import com.softdev.cms.entity.exception.StorageException;
import com.softdev.cms.entity.exception.StorageFileNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


/**
 * FileUpload Service
 * @author zhengkai.blog.csdn.net
 * */
@Service
public class StorageServiceImpl implements StorageService {
	/**从application.yml中读取*/
	@Value("${spring.file.winPath}")
	private String winPath;
	/**从application.yml中读取*/
	@Value("${spring.file.linuxPath}")
	private String linuxPath;
	/**请勿直接使用path,应该用getPath()*/
	private Path path;

	@Override
	public Path getPath() {
		if(path==null) {
			//如果在Window下,用dev路径,如果在其他系统,则用生产环境路径linuxPath by zhengkai.blog.csdn.net
			if(System.getProperty("os.name").toLowerCase().startsWith("win")) {
				path = Paths.get(winPath);
			}else {
				path = Paths.get(linuxPath);
			}
		}
		return path;
	}
	@Override
	public String getPathString() {
		//如果在Window下,用dev路径,如果在其他系统,则用生产环境路径linuxPath by zhengkai.blog.csdn.net
		if(System.getProperty("os.name").toLowerCase().startsWith("win")) {
			return winPath;
		}else {
			return linuxPath;
		}
	}

	@Override
	public void store(MultipartFile file,String filename) {
	    if(StringUtils.isEmpty(filename)) {
             filename=StringUtils.cleanPath(file.getOriginalFilename());
        }
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new StorageException(
						"Cannot store file with relative path outside current directory "
								+ filename);
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, getPath().resolve(filename),
						StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(getPath(), 1)
					.filter(path -> !path.equals(getPath()))
					.map(getPath()::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return getPath().resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(getPath().toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(getPath());
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}

