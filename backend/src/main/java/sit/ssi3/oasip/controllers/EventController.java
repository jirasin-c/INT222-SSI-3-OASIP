package sit.ssi3.oasip.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.ssi3.oasip.dtos.EventDTO;
import sit.ssi3.oasip.dtos.EventEditDTO;
import sit.ssi3.oasip.entities.Event;
import sit.ssi3.oasip.dtos.CreateEventDTO;
import sit.ssi3.oasip.services.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("")
//    public List<EventDTO> getEvent(@RequestParam(defaultValue = "eventStartTime") String sortBy){
//        return eventService.getEvent(sortBy);
//    }
    public List<EventDTO> getEvent(@RequestParam(defaultValue = "eventStartTime") String sortBy,@Valid HttpServletRequest request) {
        return eventService.getEvent(sortBy,request);
    }


    @GetMapping("/{eventId}")
//    public EventDTO getEventByID(@PathVariable Integer eventId){
//        return eventService.getEventById(eventId);
//    }

    public Object getEventByID(@Valid HttpServletRequest request, @PathVariable Integer eventId) {
        return eventService.getEventById(request, eventId);
    }

    @GetMapping("/")
    public List<EventDTO> getEventByCategoryId(@RequestParam("categoryId") Integer categoryId){
        return eventService.getEventByCategoryId(categoryId);
    }

    @GetMapping("/upComing")
    public  List<EventDTO> getEventUpComing(@RequestParam(defaultValue = "eventStartTime") String sortBy){
        return eventService.getEventUpComing(sortBy);
    }

    @GetMapping("/past")
    public  List<EventDTO> getEventPast(@RequestParam(defaultValue ="eventStartTime") String sortBy){
        return eventService.getEventPast(sortBy);
    }

    @GetMapping("/day")
    public List<EventDTO> getListDay(@RequestParam("dateEvent") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateEvent, @RequestParam(defaultValue = "eventStartTime") String sortBy){
        return  eventService.getListDay(dateEvent,sortBy);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<Event> createEvent(@RequestBody CreateEventDTO newEvent) {
//        Event newEventSaved = eventService.createEvent(newEvent);
//        return new ResponseEntity<Event>(newEventSaved, HttpStatus.OK);
//    }

    public Object create(@Valid HttpServletRequest request, @Valid @RequestBody CreateEventDTO newEvent) {
        return eventService.createEvent(request, newEvent);
    }

    @DeleteMapping("/{eventId}")
//    public void cancelEvent(@PathVariable Integer eventId){
//        eventService.cancelEvent(eventId);
//    }
    public Object delete(@Valid HttpServletRequest request, @PathVariable Integer eventId) {
        return eventService.cancelEvent(request, eventId);
    }

    @PutMapping("/{eventId}")
//    public EventDTO updateEvent(@RequestBody EventEditDTO updateEvent, @PathVariable Integer eventId) {
//        return eventService.updateEvent(updateEvent, eventId);
//
//    }

    public Object update(@Valid HttpServletRequest request, @Valid @RequestBody EventEditDTO updateEvent, @PathVariable Integer eventId) {
        return eventService.updateEvent(request, updateEvent, eventId);
    }


}
