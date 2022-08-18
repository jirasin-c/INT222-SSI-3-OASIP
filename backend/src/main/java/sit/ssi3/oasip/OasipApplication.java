package sit.ssi3.oasip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class OasipApplication {

    public static void main(String[] args) {
        TimeZone tz = TimeZone.getTimeZone("Asia/Bangkok");
        TimeZone.setDefault(tz);

        SpringApplication.run(OasipApplication.class, args);
    }



}
