package sit.ssi3.oasip.entities;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EventcategoryownerId implements Serializable {
    private static final long serialVersionUID = -1100962749771764078L;
    @Column(name = "EventCategoryOwnerID", nullable = false)
    private Integer eventCategoryOwnerID;
    @Column(name = "eventCategoryID", nullable = false)
    private Integer eventCategoryID;
    @Column(name = "userID", nullable = false)
    private Integer userID;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getEventCategoryID() {
        return eventCategoryID;
    }

    public void setEventCategoryID(Integer eventCategoryID) {
        this.eventCategoryID = eventCategoryID;
    }

    public Integer getEventCategoryOwnerID() {
        return eventCategoryOwnerID;
    }

    public void setEventCategoryOwnerID(Integer eventCategoryOwnerID) {
        this.eventCategoryOwnerID = eventCategoryOwnerID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventCategoryOwnerID, userID, eventCategoryID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventcategoryownerId entity = (EventcategoryownerId) o;
        return Objects.equals(this.eventCategoryOwnerID, entity.eventCategoryOwnerID) &&
                Objects.equals(this.userID, entity.userID) &&
                Objects.equals(this.eventCategoryID, entity.eventCategoryID);
    }
}