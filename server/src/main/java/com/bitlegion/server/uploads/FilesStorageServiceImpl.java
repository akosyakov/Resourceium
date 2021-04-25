package com.bitlegion.server.uploads;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    private final Path root = Paths.get(".artifacts");

    @Override
    public void init() {
        try {
            // first we test if the upload directory exists, and create it, if it does not
            // exist
            File directory = new File(root.toString());
            if (!directory.exists()) {
                Files.createDirectory(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    // This method creates a folder for the user and uploads the file with a unique
    // slug attached to it. It then returns the new filename.
    @Override
    public String smartSave(MultipartFile file, Integer userID) {
        try {
            File userDirectory = new File(Paths.get(this.root.toString(), "user--" + userID.toString()).toString());
            userDirectory.mkdirs();
            Path destination = Paths.get(userDirectory.toString(), file.getOriginalFilename());
            UUID slug = UUID.randomUUID();
            String fileName = "slug--" + slug.toString() + "--" + file.getOriginalFilename();
            destination = Paths.get(userDirectory.toString(), fileName);
            Files.copy(file.getInputStream(), destination);
            return fileName.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
