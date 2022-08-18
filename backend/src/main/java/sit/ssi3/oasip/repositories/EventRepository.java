package sit.ssi3.oasip.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.ssi3.oasip.dtos.EventDTO;
import sit.ssi3.oasip.entities.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByEventCategoryID_Id(Integer eventCatecoryId);
}