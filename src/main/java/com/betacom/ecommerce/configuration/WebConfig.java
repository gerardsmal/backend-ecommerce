package com.betacom.ecommerce.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig  implements WebMvcConfigurer {
	
	@Value("${app.upload.dir:uploads}")
    private String uploadDir;

	// tutto ciò che sta in uploads/ è raggiungibile via /images/filename.ext.
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        String uploadLocation = "file:" + uploadPath.toString() + "/";

        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadLocation);
    }
}
