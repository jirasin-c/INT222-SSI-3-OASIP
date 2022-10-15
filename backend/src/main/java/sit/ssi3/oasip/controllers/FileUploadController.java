package sit.ssi3.oasip.controllers;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import sit.ssi3.oasip.storage.StorageFileNotFoundException;
import sit.ssi3.oasip.storage.StorageService;

    @Controller
    @RequestMapping("/api/files")
    public class FileUploadController {

        private final StorageService storageService;

        @Autowired
        public FileUploadController(StorageService storageService) {
            this.storageService = storageService;
        }

        @GetMapping("")
        public String listUploadedFiles(Model model) throws IOException {

            model.addAttribute("files", storageService.loadAll().map(
                            path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                    "serveFile", path.getFileName().toString()).build().toUri().toString())
                    .collect(Collectors.toList()));

            return "uploadForm";
        }

        @GetMapping("/{filename:.+}")
        @ResponseBody
        public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
            Resource file = storageService.loadAsResource(filename);
//            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
            return  ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(file);
        }

        @PostMapping("{eventId}")
        public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable Integer eventId, RedirectAttributes redirectAttributes) {
            storageService.store(file, eventId);

            return ResponseEntity.ok("You successfully uploaded " + file.getOriginalFilename() + "!");
//		return "You successfully uploaded " + file.getOriginalFilename() + "!";
        }

        @DeleteMapping("{eventId}")
        public ResponseEntity<String> deleteById(@PathVariable Integer eventId, RedirectAttributes redirectAttributes) {
            storageService.deleteFileById(eventId);
            return ResponseEntity.ok("You successfully delete ");
        }


        @ExceptionHandler(StorageFileNotFoundException.class)
        public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
            return ResponseEntity.notFound().build();
        }
    }

