package sit.ssi3.oasip.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class EventcategoryDTO implements Serializable {
    private Integer id;
    private  String eventCategoryName;
    private  String eventCategoryDescription;
    private Integer eventDuration;
}
