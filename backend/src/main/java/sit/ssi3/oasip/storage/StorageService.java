package sit.ssi3.oasip.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file, Integer eventId);

    Stream<Path> loadAll();

    Path load(Integer eventId, String filename);

    Resource loadAsResource(Integer eventId, String filename);

    List<String> listFileName(Integer eventId);

    void deleteAll();

    void deleteFileById(Integer eventId);

}