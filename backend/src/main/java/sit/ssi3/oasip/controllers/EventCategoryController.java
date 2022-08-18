package sit.ssi3.oasip.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.ssi3.oasip.dtos.EventcategoryDTO;
import sit.ssi3.oasip.entities.Eventcategory;
import sit.ssi3.oasip.services.EventCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/event-categories")
public class EventCategoryController {
    @Autowired
    private EventCategoryService eventCategoryService;

    @GetMapping("")
    public List<EventcategoryDTO> getEventCategory(@RequestParam(defaultValue = "id") String sortBy){
        return eventCategoryService.getEventCategory(sortBy);
    }

    @GetMapping("/categoryName")
    private EventcategoryDTO getEventCategoryByName(@RequestParam String categoryName) {
        return eventCategoryService.getEventCategoryByName(categoryName);
    }


    @GetMapping("/{categoryID}")
    private Eventcategory getEventcategoryByID(@PathVariable Integer categoryID) {
        return eventCategoryService.getEventcategoryByID(categoryID);
    }

}

