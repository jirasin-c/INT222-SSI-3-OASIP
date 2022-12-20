package sit.ssi3.oasip.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import sit.ssi3.oasip.dtos.EventDTO;
import sit.ssi3.oasip.dtos.EventEditDTO;
import sit.ssi3.oasip.entities.Event;
import sit.ssi3.oasip.dtos.CreateEventDTO;
import sit.ssi3.oasip.entities.Eventcategory;
import sit.ssi3.oasip.repositories.EventCategoryRepository;
import sit.ssi3.oasip.services.EmailSenderService;
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
    @Autowired
    private EventCategoryRepository eventCategoryRepository;
    @Autowired
    private EmailSenderService emailSenderService;


    @GetMapping("")
    public Object getEvent(@RequestParam(defaultValue = "eventStartTime") String sortBy,@Valid HttpServletRequest request) {
        return eventService.getEvent(sortBy,request);
    }

    @GetMapping("/{eventId}")
    public Object getEventByID(@Valid HttpServletRequest request, @PathVariable Integer eventId) {
        return eventService.getEventById(request, eventId);
    }

    @GetMapping("/upComing")
    public  List<EventDTO> getEventUpComing(@RequestParam(defaultValue = "eventStartTime") String sortBy,HttpServletRequest request){
        return eventService.getEventUpComing(sortBy,request);
    }

    @GetMapping("/past")
    public  List<EventDTO> getEventPast(@RequestParam(defaultValue ="eventStartTime") String sortBy,HttpServletRequest request){
        return eventService.getEventPast(sortBy,request);
    }

    @GetMapping("/day")
    public List<EventDTO> getListDay(@RequestParam("dateEvent") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateEvent, @RequestParam(defaultValue = "eventStartTime") String sortBy,HttpServletRequest request){
        return  eventService.getListDay(dateEvent,sortBy,request);
    }

    //เป็นการอนุญาตให้ฟอร์มส่งข้อมูลได้หลายประเภทพร้อมกัน เช่น ข้อความและรูปภาพ (อัพโหลดรูป)
    @PostMapping(path="",consumes = {"multipart/form-data"})
    public Object create(@Valid HttpServletRequest request, @RequestParam("event") String event, @RequestParam(name = "file", required = false) MultipartFile file) throws JsonProcessingException {
        //converting a JSON String to a Java object using the ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        //provides functionality for finding reading and writing JSON
        objectMapper.findAndRegisterModules();
        //Read Object From JSON File
        //fieldที่กรอกมาในform
        CreateEventDTO newEvent  = objectMapper.readValue(event, CreateEventDTO.class);

        int categoryId = newEvent.getEventCategoryID();
        Eventcategory eventCategory = eventCategoryRepository.findById(categoryId).orElseThrow(()->new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Category id "+ categoryId+
            "Does Not Exist !!!"
        ));

        //email form
        Date startTime = new Date(newEvent.getEventStartTime().getTime());
        Date endTime = new Date(startTime.getTime()+ (newEvent.getEventDuration()*60000));
        String category = eventCategory.getEventCategoryName();

        String header = "[OASIP] " + category + " @ " + startTime + " - " + endTime;
        String body = "Your appointment has been registered successfully. \n \n" +
               "Details" +
               "\n" + "Booking Name : " + newEvent.getBookingName() +
               "\n" + "Event Category : " + eventCategory.getEventCategoryName() +
               "\n" + "When : " + startTime + " - " + endTime +
               "\n" + "Event Notes : " + newEvent.getEventNotes();

        emailSenderService.sendEmail(newEvent.getBookingEmail() , header , body);
        return eventService.createEvent(request, newEvent,file);
    }

    @DeleteMapping("/{eventId}")
    public Object delete(@Valid HttpServletRequest request, @PathVariable Integer eventId) {
        return eventService.cancelEvent(request, eventId);
    }

    @PatchMapping("/{eventId}")
    public Object update(@Valid HttpServletRequest request, @RequestParam(name = "event", required = false) String updateEvent, @RequestParam(name = "file", required = false) MultipartFile file, @PathVariable Integer eventId) throws JsonProcessingException {
        //converting a JSON String to a Java object using the ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        //provides functionality for finding reading and writing JSON
        objectMapper.findAndRegisterModules();
        CreateEventDTO editEvent = null;
        if(updateEvent != null) {
        //Read Object From JSON File
        //fieldที่กรอกมาในform
        editEvent = objectMapper.readValue(updateEvent,CreateEventDTO.class);}

        return eventService.updateEvent(request, editEvent, file, eventId);
    }


}
