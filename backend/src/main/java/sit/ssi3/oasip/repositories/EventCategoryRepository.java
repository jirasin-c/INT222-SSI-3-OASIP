package sit.ssi3.oasip.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.ssi3.oasip.entities.Eventcategory;

public interface EventCategoryRepository extends JpaRepository<Eventcategory, Integer> {
    Eventcategory findByEventCategoryNameEquals(String eventCategoryName);
}
