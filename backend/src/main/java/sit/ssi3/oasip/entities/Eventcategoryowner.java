package sit.ssi3.oasip.entities;

import javax.persistence.*;

@Entity
@Table(name = "eventcategoryowner")
public class Eventcategoryowner {
    @EmbeddedId
    private EventcategoryownerId id;

    @MapsId("eventCategoryID")
    @ManyToOne(optional = false)
    @JoinColumn(name = "eventCategoryID", nullable = false)
    private Eventcategory eventCategoryID;

    @MapsId("userID")
    @ManyToOne(optional = false)
    @JoinColumn(name = "userID", nullable = false)
    private User userID;

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    public Eventcategory getEventCategoryID() {
        return eventCategoryID;
    }

    public void setEventCategoryID(Eventcategory eventCategoryID) {
        this.eventCategoryID = eventCategoryID;
    }

    public EventcategoryownerId getId() {
        return id;
    }

    public void setId(EventcategoryownerId id) {
        this.id = id;
    }
}