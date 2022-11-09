package sit.ssi3.oasip.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootLocation);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public void store(MultipartFile file, Integer eventId) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path newPath = newFolder(String.valueOf(eventId));
            Path targetLocation = this.rootLocation.resolve(String.valueOf(eventId) + "/" + fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }  catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    public Path newFolder(String folderName) throws IOException {
        Path folder = this.rootLocation.resolve(folderName);
        Resource resource = new UrlResource(folder.toUri());

        if (!resource.exists()) {
            Path path = Files.createDirectory(folder);
            System.out.println(path);
            return folder;

        }else {
            FileUtils.cleanDirectory(folder.toFile());
            System.out.println(rootLocation + "\\" + folderName);
            return folder;
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(Integer eventId, String filename) {
        return rootLocation.resolve(String.valueOf(eventId)).resolve(filename);
    }

    @Override
    public Resource loadAsResource(Integer eventId, String filename) {
        try {
            Path file = load(eventId, filename);
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
    public List<String> listFileName(Integer eventId) {
        File folder = new File(rootLocation.resolve(String.valueOf(eventId)).toUri());
        List<File> listOfFiles = List.of(folder.listFiles());
        List<String> listOfFilenames = new ArrayList<>();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                listOfFilenames.add(file.getName());
                System.out.println(listOfFilenames);
            }
        }
        return listOfFilenames;
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void deleteFileById(Integer eventId) {
        String dest = eventId.toString();
        FileSystemUtils.deleteRecursively(rootLocation.resolve(dest).toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}

