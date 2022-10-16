package sit.ssi3.oasip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sit.ssi3.oasip.services.EmailSenderService;
import sit.ssi3.oasip.storage.StorageProperties;

import java.io.IOException;
import java.util.TimeZone;



@SpringBootApplication
public class OasipApplication {


    public static void main(String[] args) throws IOException {
        TimeZone tz = TimeZone.getTimeZone("Asia/Bangkok");
        TimeZone.setDefault(tz);

        SpringApplication.run(OasipApplication.class, args);

    }

    @Bean
    public Argon2PasswordEncoder argon2PasswordEncoder() {return new Argon2PasswordEncoder();}




}


