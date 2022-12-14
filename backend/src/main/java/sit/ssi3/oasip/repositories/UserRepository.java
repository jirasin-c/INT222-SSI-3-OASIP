package sit.ssi3.oasip.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sit.ssi3.oasip.entities.Event;
import sit.ssi3.oasip.entities.Eventcategory;
import sit.ssi3.oasip.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

   User findByEmail(String email);
   User findByName(String name);
   boolean existsByEmail(String email);

   @Transactional
   void deleteUserByName(String name);


}
