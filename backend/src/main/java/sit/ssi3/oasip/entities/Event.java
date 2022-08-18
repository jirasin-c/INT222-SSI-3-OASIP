package sit.ssi3.oasip.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
@Data
@Table(name = "event", indexes = {
        @Index(name = "fk_Event_EventCategory_idx", columnList = "eventCategoryID")
})
@Entity
public class Event {
    @Id
    @Column(name = "eventID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotEmpty(message = "A name must not be empty!")
    @Size(max = 100, message = "A name must be between 1 and 100")
    @Column(name = "bookingName", nullable = false)
    private String bookingName;

    @Email(message = "An email must be well-formed email address", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @NotEmpty(message = "An email address must not be empty!")
    @Size(max = 100, message = "An email address must be between 1 and 100")
    @Column(name = "bookingEmail", nullable = false)
    private String bookingEmail;


    @NotNull(message = "A start time must not be empty")
    @JsonFormat( timezone = "Asia/Bangkok")
    @Future(message = "A start time must be a future date")
    @Column(name = "eventStartTime", nullable = false)
    private Date eventStartTime;


    @Column(name = "eventDuration", nullable = false)
    private Integer eventDuration;


    @Size(max = 500, message = "A note must be between 0 and 500")
    @Column(name = "eventNotes", length = 500)
    private String eventNotes;

    @NotNull(message = "A category ID must not be empty")
    @ManyToOne(optional = false)
    @JoinColumn(name = "eventCategoryID", nullable = false)
    private Eventcategory eventCategoryID;

    @Transient
    @AssertFalse(message = "This event is overlapped with other events")
    private boolean isOverlapped;

}

