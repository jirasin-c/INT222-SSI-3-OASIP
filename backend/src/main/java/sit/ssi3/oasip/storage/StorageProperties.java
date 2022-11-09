package sit.ssi3.oasip.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//@Component

@ConfigurationProperties("file")
@Configuration
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    public static String location;
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}