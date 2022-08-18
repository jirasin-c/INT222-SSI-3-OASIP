package sit.ssi3.oasip.dtos;

import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Data
public class EventEditDTO implements Serializable {
    private  Date eventStartTime;
    private  String eventNotes;
}
