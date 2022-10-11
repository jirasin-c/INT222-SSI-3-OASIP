package sit.ssi3.oasip.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sit.ssi3.oasip.entities.Eventcategoryowner;


import java.util.List;

public interface EventCategoryOwnerRepository extends JpaRepository<Eventcategoryowner, Integer> {

    @Query("select a.eventCategoryID.id from Eventcategoryowner a where a.userID.id = :id ")
    List<Integer> findAllByUserId(@Param("id") int id);
}