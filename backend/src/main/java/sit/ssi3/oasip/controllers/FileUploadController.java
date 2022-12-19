package sit.ssi3.oasip.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sit.ssi3.oasip.storage.StorageException;
import sit.ssi3.oasip.storage.StorageFileNotFoundException;
import sit.ssi3.oasip.storage.StorageService;


import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/api/files")
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

//    @GetMapping("")
//    public String listUploadedFiles(Model model) throws IOException {
//        model.addAttribute("files", storageService.loadAll().map(
//                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
//                                "serveFile", path.getFileName().toString()).build().toUri().toString())
//                .collect(Collectors.toList()));
//
//        return "uploadForm";
//    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> listFileName(@PathVariable Integer eventId) {
        List<String> filenames = storageService.listFileName(eventId);
        return ResponseEntity.ok().body(filenames);
    }

    @GetMapping(path = "/{eventId}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable Integer eventId, @PathVariable String filename, HttpServletRequest request){

        Resource file = storageService.loadAsResource(eventId, filename);

        //ดู type file
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());

        } catch (IOException ex) {
            throw new StorageException("Could not determine file type.", ex);
        }

        //ไม่รู้type = default content type
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
//				.contentType(MediaType.ALL)
                //ทำให้ browser download ไฟล์แทนที่จะเปิดใน browser
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }


    //uploadทีหลัง
    @PostMapping("{eventId}")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable Integer eventId) {
        storageService.store(file, eventId);
        return ResponseEntity.ok("successfully uploaded " + file.getOriginalFilename());
    }

    @DeleteMapping("{eventId}")
    public ResponseEntity<String> deleteById(@PathVariable Integer eventId) {
        storageService.deleteFileById(eventId);
        return ResponseEntity.ok("successfully delete");
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}

