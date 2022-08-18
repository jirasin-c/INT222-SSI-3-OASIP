package sit.ssi3.oasip.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.ssi3.oasip.dtos.EventDTO;
import sit.ssi3.oasip.dtos.EventcategoryDTO;
import sit.ssi3.oasip.entities.Event;
import sit.ssi3.oasip.entities.Eventcategory;
import sit.ssi3.oasip.repositories.EventCategoryRepository;
import sit.ssi3.oasip.utils.ListMapper;

import java.util.List;

@Service
public class EventCategoryService {
    @Autowired
    private EventCategoryRepository eventCategoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    public List<EventcategoryDTO> getEventCategory(String sortBy) {
        List<Eventcategory> eventCategoryList = eventCategoryRepository.findAll(Sort.by(sortBy).descending());
        //Exception handling
        if (eventCategoryList.size() == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event category not found");
        return listMapper.mapList(eventCategoryList, EventcategoryDTO.class, modelMapper);
    }

    
    public EventcategoryDTO getEventCategoryByName(String categoryName) {
        Eventcategory eventcategory = eventCategoryRepository.findByEventCategoryNameEquals(categoryName);
        //Exception handling
        if (eventcategory == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event category not found");
        return modelMapper.map(eventcategory, EventcategoryDTO.class);

    }


    public Eventcategory getEventcategoryByID(Integer categoryID) {
        Eventcategory eventcategory = eventCategoryRepository.findById(categoryID).orElseThrow(() ->
                new RuntimeException(categoryID + " Does not Exist !!!"));
        return modelMapper.map(eventcategory, Eventcategory.class);
    }

}
