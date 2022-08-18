package sit.ssi3.oasip.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CreateEventDTO implements Serializable {
    private Integer id;
    private String bookingName;
    private String bookingEmail;
    private Date eventStartTime;
    private Integer eventDuration;
    private String eventNotes;
    private Integer eventCategoryID;
}


