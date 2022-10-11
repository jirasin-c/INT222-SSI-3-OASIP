package sit.ssi3.oasip.services;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import sit.ssi3.oasip.Enum.RoleEnum;
import sit.ssi3.oasip.config.JwtTokenUtil;
import sit.ssi3.oasip.dtos.EventDTO;
import sit.ssi3.oasip.dtos.EventEditDTO;
import sit.ssi3.oasip.entities.Event;
import sit.ssi3.oasip.entities.User;
import sit.ssi3.oasip.exceptions.ValidationHandler;
import sit.ssi3.oasip.repositories.EventCategoryOwnerRepository;
import sit.ssi3.oasip.repositories.EventRepository;
import sit.ssi3.oasip.dtos.CreateEventDTO;
import sit.ssi3.oasip.repositories.UserRepository;
import sit.ssi3.oasip.utils.ListMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventCategoryOwnerRepository eventCategoryOwnerRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private EventCategoryService eventCategoryService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private static final Validator validator =
            Validation.byDefaultProvider()
                    .configure()
                    .messageInterpolator(new ParameterMessageInterpolator())
                    .buildValidatorFactory()
                    .getValidator();



    public User getUserFromRequest(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null) {
            String token = request.getHeader("Authorization").substring(7);
            String userEmail = jwtTokenUtil.getUsernameFromToken(token);
            return  userRepository.findByEmail(userEmail);
        }
        return null;
    }

    public List<EventDTO> getEvent(String sortBy,HttpServletRequest request) {
        User userOwner = getUserFromRequest(request);
        RoleEnum userRole = userOwner.getRole();
//        List<Event> eventList = new ArrayList<>();
        List<Event> eventList = eventRepository.findAll(Sort.by(sortBy).descending());

//        if (userOwner.getRole().equals("admin")){
        if (userRole.equals(RoleEnum.admin)){
            eventList = eventRepository.findAll(Sort.by(sortBy).descending());

        } else if (userRole.equals(RoleEnum.student)) {
//            eventList = eventRepository.findAllByOwner(userOwner.getEmail());
               eventList = eventRepository.findAllByBookingEmail(userOwner.getEmail());


        } else if (userRole.equals(RoleEnum.lecturer)){

            List<Integer> categoriesId = eventCategoryOwnerRepository.findAllByUserId(userOwner.getId());
            System.out.println(categoriesId);
            eventList = eventRepository.findAllByEventCategory(categoriesId);
        }

        if (eventList.size() == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Events not found");
        return listMapper.mapList(eventList, EventDTO.class, modelMapper);

//        User userOwner = getUserFromRequest(request);
//        System.out.println(userOwner.getEmail());
//        List<Event> eventList = new ArrayList<>();

//        if (userOwner.getRole().equals("admin")){
//            System.out.println("เข้า admin");
//            eventList = eventRepository.findAllByOrderByEventStartTimeDesc();
//        } else if (userOwner.getRole().equals("student")) {
//            System.out.println("เข้า student");
//            eventList = eventRepository.findAllByOwner(userOwner.getEmail());
//        }
//        return listMapper.mapList(eventList, EventDTO.class, modelMapper);


    }



    public Object getEventById(HttpServletRequest request, Integer eventId) {
//        Event event = eventRepository.findById(eventId).orElseThrow(() ->
//                new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "Event ID " + eventId + "Does not Exits"
//                )
//        );
//        return modelMapper.map(event, EventDTO.class);
//    }

    User userOwner = getUserFromRequest(request);
    RoleEnum userRole = userOwner.getRole();
    Event event = eventRepository.findById(eventId)
            .orElseThrow(()->new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Event ID "+ eventId+
                    "Does Not Exist !!!"
            ));

        if (userRole.equals(RoleEnum.student)) {
        if (!userOwner.getEmail().equals(event.getBookingEmail())) {
            return ValidationHandler.showError(HttpStatus.FORBIDDEN, "You not have permission this event");
        }
    }
        return modelMapper.map(event, EventDTO.class);
}

    public List<EventDTO> getEventByCategoryId(Integer categoryId) {
        List<Event> eventList = eventRepository.findByEventCategoryID_Id(categoryId);
        //Exception handling
        if (eventList.size() == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Events not found");
        return listMapper.mapList(eventList, EventDTO.class, modelMapper);
    }

    public List<EventDTO> getEventUpComing(String sortBy) {
        Date currentDate = new Date();
        // find all event
        List<Event> eventList = eventRepository.findAll(Sort.by(sortBy).ascending());

        // check event
        eventList = eventList.stream().filter(oldEvent -> {
            Date startTime = new Date(oldEvent.getEventStartTime().getTime());

            if (currentDate.compareTo(startTime) <= 0) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if (eventList.size() == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Events not found");
        return listMapper.mapList(eventList, EventDTO.class, modelMapper);

    }

    public List<EventDTO> getEventPast(String sortBy) {
        Date currentDate = new Date();

        //Find all event
        List<Event> eventList = eventRepository.findAll(Sort.by(sortBy).descending());

        //Check event
        eventList = eventList.stream().filter(allEvent -> {
            Date allStartTime = new Date(allEvent.getEventStartTime().getTime());

            if (currentDate.compareTo(allStartTime) == 1) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        //Exception handling
        if (eventList.size() == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Events not found");
        return listMapper.mapList(eventList, EventDTO.class, modelMapper);

    }

    public List<EventDTO> getListDay(Date dateEvent, String sortBy) {
        if (dateEvent == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dateEvent is null");


        Date specifiedDate = new Date();
        specifiedDate.setTime(dateEvent.getTime());
        Date endTime = new Date();
        endTime.setTime(specifiedDate.getTime() + 86400000 );

        // find all event
        List<Event> eventList = eventRepository.findAll(Sort.by(sortBy).ascending());

        // check event
        eventList = eventList.stream().filter((allEvent) -> {
            Date allStartTime = new Date(allEvent.getEventStartTime().getTime());
            return ((allStartTime.compareTo(specifiedDate) > 0) && (allStartTime.compareTo(endTime) < 0));
        }).collect(Collectors.toList());

        //Exception handling
        if (eventList.size() == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Events not found");
        return listMapper.mapList(eventList, EventDTO.class, modelMapper);
    }


    public Object createEvent(HttpServletRequest request, CreateEventDTO newEvent) {
        User userOwner = getUserFromRequest(request);
        RoleEnum userRole = userOwner.getRole();
        if (userOwner != null) {
            if (userRole.equals(RoleEnum.student)) {
                if (!userOwner.getEmail().equals(newEvent.getBookingEmail())) {
                    return ValidationHandler.showError(HttpStatus.BAD_REQUEST, "the booking email must be the same as student's email");
                }
            }
        }
        // map event dto request to event
        Event event = new Event();
        event.setId(null);
        event.setBookingName(newEvent.getBookingName());
        event.setEventDuration(newEvent.getEventDuration() == null ? newEvent.getEventCategoryID() == null
                ? null : eventCategoryService.getEventcategoryByID(newEvent.getEventCategoryID()).getEventDuration() : newEvent.getEventDuration());
        event.setEventNotes(newEvent.getEventNotes());
        event.setBookingEmail(newEvent.getBookingEmail());
        event.setEventCategoryID(newEvent.getEventCategoryID() == null ? null : eventCategoryService.getEventcategoryByID(newEvent.getEventCategoryID()));
        event.setEventStartTime(newEvent.getEventStartTime());
        event.setUser(userService.getUserByID(newEvent.getUserID()));
        event.setOverlapped(false);

        // find all event
        List<Event> eventList = this.eventRepository.findByEventCategoryID_Id(newEvent.getEventCategoryID() );
        if (newEvent.getEventStartTime() != null && event.getEventDuration() != null) {

            // check event overlapped
            eventList = eventList.stream().filter(oldEvent -> {
                Date startTime = new Date(oldEvent.getEventStartTime().getTime());
                Date newEventStartTime = event.getEventStartTime();
                Date endTime = new Date((startTime.getTime() + (oldEvent.getEventDuration() * 60000)));
                Date newEventEndTime = new Date((newEventStartTime.getTime() + (event.getEventDuration() * 60000)));

                if (((startTime.compareTo(newEventStartTime) <= 0) && (newEventStartTime.compareTo(endTime) < 0))
                        || ((startTime.compareTo(newEventEndTime) < 0) && (newEventEndTime.compareTo(endTime) <= 0))
                        || ((newEventStartTime.compareTo(startTime) < 0) && (endTime.compareTo(newEventEndTime) < 0))
                        || ((startTime.compareTo(newEventStartTime) < 0) && (newEventEndTime.compareTo(endTime) < 0))) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList());

        }
        // check overlapped
        if (eventList.size() > 0 && newEvent.getEventStartTime() != null && event.getEventDuration() != null) {
            event.setOverlapped(true);
        }
        // validate event field
        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        for (ConstraintViolation<Event> violation : violations) {
            System.out.println(violation.getMessage());
        }
        // return when error message contains
        if (violations.size() > 0) throw new ConstraintViolationException(violations);
        // custom error response
        this.eventRepository.saveAndFlush(event); // return success service
        return null;
    }

    public Object cancelEvent(@Valid HttpServletRequest request, @PathVariable Integer eventId) {
        User userOwner = getUserFromRequest(request);
        RoleEnum userRole = userOwner.getRole();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Event ID " + eventId + " Does Not Exits!!!"));

        if (userRole.equals(RoleEnum.student)) {
            if (!userOwner.getEmail().equals(event.getBookingEmail())) {
                return ValidationHandler.showError(HttpStatus.FORBIDDEN, "You not have permission this event");
            }

        }
        eventRepository.deleteById(eventId);
        return  null;
    }

    public Object updateEvent(HttpServletRequest request, EventEditDTO updateEvent, Integer eventId) {
        User userOwner = getUserFromRequest(request);
        RoleEnum userRole = userOwner.getRole();
        Event newEvent = modelMapper.map(updateEvent, Event.class);
        Event event = eventRepository.findById(eventId).map(o -> mapEvent(o, newEvent)).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Event ID " + eventId + " Does Not Exits!!!"));
        event.setOverlapped(false);

        if (userRole.equals(RoleEnum.student)) {
            if (!userOwner.getEmail().equals(event.getBookingEmail())) {
                return ValidationHandler.showError(HttpStatus.FORBIDDEN, "You not have permission this event");
            }

        }

        eventRepository.saveAndFlush(event);

        // validate event field
        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        for (ConstraintViolation<Event> violation : violations) {
            System.out.println(violation.getMessage());
        }
        // return when error message contains
        if (violations.size() > 0) throw new ConstraintViolationException(violations);
        return modelMapper.map(event, EventDTO.class);
    }

    private Event mapEvent(Event existingEvent, Event updateEvent) {
        if (updateEvent.getEventNotes() != null)
            existingEvent.setEventNotes(updateEvent.getEventNotes());

        if (updateEvent.getEventStartTime() != null)
            existingEvent.setEventStartTime(updateEvent.getEventStartTime());
        return existingEvent;
    }


}
