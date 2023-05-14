package ma.enset.hospitals.security.repo;

import ma.enset.hospitals.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole,String> {
}
