package ma.enset.hospitals.security.repo;

import ma.enset.hospitals.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser,String> {

    AppUser findByUsername(String username);

}
