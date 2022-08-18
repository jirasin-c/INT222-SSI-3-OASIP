package sit.ssi3.oasip.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "eventcategory", indexes = {
        @Index(name = "eventCategoryName_UNIQUE", columnList = "eventCategoryName", unique = true)
})
@Entity
public class Eventcategory {
    @Id
    @Column(name = "eventCategoryID", nullable = false)
    private Integer id;

    @Column(name = "eventCategoryName", nullable = false, length = 100)
    private String eventCategoryName;

    @Column(name = "eventCategoryDescription", length = 500)
    private String eventCategoryDescription;

    @Column(name = "eventDuration", nullable = false)
    private Integer eventDuration;

}
