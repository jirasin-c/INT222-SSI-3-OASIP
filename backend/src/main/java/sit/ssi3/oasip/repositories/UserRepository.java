package sit.ssi3.oasip.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.ssi3.oasip.entities.Event;
import sit.ssi3.oasip.entities.Eventcategory;
import sit.ssi3.oasip.entities.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
}
