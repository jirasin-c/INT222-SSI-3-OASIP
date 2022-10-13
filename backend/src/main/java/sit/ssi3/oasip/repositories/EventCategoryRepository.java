package sit.ssi3.oasip.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sit.ssi3.oasip.entities.Event;
import sit.ssi3.oasip.entities.Eventcategory;

import java.util.List;

public interface EventCategoryRepository extends JpaRepository<Eventcategory, Integer> {
    Eventcategory findByEventCategoryNameEquals(String eventCategoryName);


    @Query("select a from Event a where a.eventCategoryID.id in :idCate")
    List<Event> findAllByEventCategory(@Param("idCate") List<Integer> idCate);
}
