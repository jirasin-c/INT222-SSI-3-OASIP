package sit.ssi3.oasip.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sit.ssi3.oasip.dtos.EventDTO;
import sit.ssi3.oasip.entities.Event;
import sit.ssi3.oasip.entities.Eventcategory;
import sit.ssi3.oasip.entities.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByEventCategoryID_Id(Integer eventCategoryId);
    List<Event> findAllByOrderByEventStartTimeDesc();
    List<Event> findAllByBookingEmailOrderByEventStartTimeDesc(String email);
    @Query("select a from Event a where a.eventCategoryID.id in :idCate")
    List<Event> findAllByEventCategory(@Param("idCate") List<Integer> idCate);

}
