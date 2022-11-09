package sit.ssi3.oasip.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.ssi3.oasip.config.JwtTokenUtil;
import sit.ssi3.oasip.configJwts.JwtUserDetailsService;
import sit.ssi3.oasip.dtos.EventDTO;
import sit.ssi3.oasip.dtos.EventcategoryDTO;
import sit.ssi3.oasip.entities.Event;
import sit.ssi3.oasip.entities.Eventcategory;
import sit.ssi3.oasip.entities.User;
import sit.ssi3.oasip.repositories.EventCategoryOwnerRepository;
import sit.ssi3.oasip.repositories.EventCategoryRepository;
import sit.ssi3.oasip.repositories.UserRepository;
import sit.ssi3.oasip.utils.ListMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventCategoryService {
    @Autowired
    private EventCategoryRepository eventCategoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventCategoryOwnerRepository eventCategoryOwnerRepository;

    private final JwtTokenUtil jwtTokenUtill;
    private final JwtUserDetailsService jwtUserDetailsService;

    public EventCategoryService(JwtTokenUtil jwtTokenUtill, JwtUserDetailsService jwtUserDetailsService) {
        this.jwtTokenUtill = jwtTokenUtill;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    public User getUserFromRequest(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null) {
            String token = request.getHeader("Authorization").substring(7);
            String userEmail = jwtTokenUtill.getUsernameFromToken(token);
            return  userRepository.findByEmail(userEmail);
        }
        return null;
    }

    public List<EventcategoryDTO> getEventCategory(String sortBy,HttpServletRequest request) {
        User userOwner = getUserFromRequest(request);
        List<Eventcategory> eventCategoryList = eventCategoryRepository.findAll(Sort.by(sortBy).descending());

        List<Eventcategory> eventCategoryFilter = new ArrayList<>();

        if (userOwner != null) {
            if (userOwner.getRole().equals("lecturer")){
                System.out.println("เข้า lecturer");
                List<Integer> categoriesId = eventCategoryOwnerRepository.findAllByUserId(userOwner.getId());
                for(Eventcategory category : eventCategoryList){
                    if (categoriesId.contains(category.getId())) {
                        eventCategoryFilter.add(category);
                    }
                }
            } else {
                eventCategoryFilter = eventCategoryList;
            }
        } else {
            eventCategoryFilter = eventCategoryList;
        }

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
