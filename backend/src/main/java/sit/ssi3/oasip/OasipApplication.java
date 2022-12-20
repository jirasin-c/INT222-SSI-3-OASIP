package sit.ssi3.oasip;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
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


