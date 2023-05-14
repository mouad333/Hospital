package ma.enset.hospitals.security.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ma.enset.hospitals.security.entities.AppRole;
import ma.enset.hospitals.security.entities.AppUser;
import ma.enset.hospitals.security.repo.AppRoleRepository;
import ma.enset.hospitals.security.repo.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{
    AppUserRepository appUserRepository;
    AppRoleRepository appRoleRepository;
    PasswordEncoder passwordEncoder;
    @Override
    public AppUser addNewUser(String username, String password, String email, String confirmPassword) {
        AppUser appUser= appUserRepository.findByUsername(username);
        if (appUser!=null) throw new RuntimeException("This user is already exist");
        if (!password.equals(confirmPassword)) throw new RuntimeException("Passwords isn't matching");
        appUser=AppUser.builder()
                .userId(UUID.randomUUID().toString())
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();
        AppUser savedUser=appUserRepository.save(appUser);
        return savedUser;
    }

    @Override
    public AppRole addNewRole(String role) {
        AppRole appRole=appRoleRepository.findById(role).orElse(null);
        if (appRole!=null) throw new RuntimeException("This role is already exist");
        appRole=AppRole.builder()
                .role(role)
                .build();
        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String role) {
        AppUser appUser=appUserRepository.findByUsername(username);
        if (appUser==null) throw new RuntimeException("This user not existed");
        AppRole appRole=appRoleRepository.findById(role).get();
        if (appRole==null) throw new RuntimeException("This role not existed");
        appUser.getRoles().add(appRole);
        //appUserRepository.save(appUser);

    }

    @Override
    public void removeRoleFromUser(String username, String role) {
        AppUser appUser=appUserRepository.findByUsername(username);
        if (appUser==null) throw new RuntimeException("This user not existed");
        AppRole appRole=appRoleRepository.findById(role).get();
        if (appRole==null) throw new RuntimeException("This role not existed");
        appUser.getRoles().remove(role);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }
}
