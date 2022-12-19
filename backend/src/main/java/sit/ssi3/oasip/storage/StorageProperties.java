package sit.ssi3.oasip.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


// automatically bind the properties declared in the application.properties file.
@ConfigurationProperties("file")
@Configuration
public class StorageProperties {

    public static String location;
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}