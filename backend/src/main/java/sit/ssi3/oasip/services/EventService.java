package sit.ssi3.oasip.services;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.ssi3.oasip.dtos.EventDTO;
import sit.ssi3.oasip.dtos.EventEditDTO;
import sit.ssi3.oasip.entities.Event;
import sit.ssi3.oasip.repositories.EventRepository;
import sit.ssi3.oasip.dtos.CreateEventDTO;
import sit.ssi3.oasip.utils.ListMapper;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private EventCategoryService eventCategoryService;

    @Autowired
    private UserService userService;
    @Autowired
    private static final Validator validator =
            Validation.byDefaultProvider()
                    .configure()
                    .messageInterpolator(new ParameterMessageInterpolator())
                    .buildValidatorFactory()
                    .getValidator();

    public List<EventDTO> getEvent(String sortBy) {
        List<Event> eventList = eventRepository.findAll(Sort.by(sortBy).descending());
        if (eventList.size() == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Events not found");
        return listMapper.mapList(eventList, EventDTO.class, modelMapper);
    }

    public EventDTO getEventById(Integer eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Event ID " + eventId + "Does not Exits"
                )
        );
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


    public Event createEvent(CreateEventDTO newEvent) {
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
        return this.eventRepository.saveAndFlush(event); // return success service
    }

    public void cancelEvent(Integer eventId) {
        eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Event ID " + eventId + " Does Not Exits!!!"));
        eventRepository.deleteById(eventId);
    }

    public EventDTO updateEvent(EventEditDTO updateEvent, Integer eventId) {
        Event newEvent = modelMapper.map(updateEvent, Event.class);
        Event event = eventRepository.findById(eventId).map(o -> mapEvent(o, newEvent)).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Event ID " + eventId + " Does Not Exits!!!"));
        event.setOverlapped(false);
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
