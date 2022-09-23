package sit.ssi3.oasip.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sit.ssi3.oasip.dtos.EventDTO;
import sit.ssi3.oasip.entities.Event;
import sit.ssi3.oasip.entities.User;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByEventCategoryID_Id(Integer eventCatecoryId);

//    @Modifying
//    @Query("delete from Event e where e.user.id = ?1")
//    void deleteEventsByUserId(Integer id);
    @Transactional
    void deleteAllByUser(User user);
}
