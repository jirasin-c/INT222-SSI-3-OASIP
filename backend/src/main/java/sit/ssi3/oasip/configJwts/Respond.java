package sit.ssi3.oasip.configJwts;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class Respond {
    private HttpStatus httpStatus;
    private String message;

    public static ResponseEntity<Object> respond(HttpStatus httpStatus, String message){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("timestamp", new Date());
        map.put("status", httpStatus.value());
        map.put("error", httpStatus.toString().replaceAll("\\d","").trim());
        map.put("message", message);
        return new ResponseEntity<Object>(map,httpStatus);
    }
}