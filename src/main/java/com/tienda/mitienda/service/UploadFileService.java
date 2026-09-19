package com.tienda.mitienda.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadFileService {

    private final String folder = "src/main/resources/static/images//";

    public String guardarImagen(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folder + file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename();
        }
        return "banner.jpg"; // Imagen por defecto si no se sube ninguna
    }

    public void eliminarImagen(String nombre) {
        String ruta = "src/main/resources/static/images/";
        File file = new File(ruta + nombre);
        if (!nombre.equals("banner.jpg") && file.exists()) {
            file.delete();
        }
    }
}