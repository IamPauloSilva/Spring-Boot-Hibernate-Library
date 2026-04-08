package com.app.library.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final Path uploadPath;

	public WebConfig(@Value("${app.upload.dir:uploads}") String uploadDir) {
		this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		try {
			Files.createDirectories(uploadPath);
		} catch (IOException ignored) {
			// App can still run; upload action will report errors when needed.
		}

		registry.addResourceHandler("/uploads/**")
				.addResourceLocations(uploadPath.toUri().toString());
	}
}

