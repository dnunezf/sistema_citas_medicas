package org.example.sistema_citas_medicas.logica.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ruta absoluta hacia la carpeta donde están tus fotos
        Path uploadDir = Paths.get("uploads/fotos_perfil");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/uploads/fotos_perfil/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}