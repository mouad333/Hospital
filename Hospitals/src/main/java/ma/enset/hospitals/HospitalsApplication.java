package ma.enset.hospitals;

import ma.enset.hospitals.entities.Patient;
import ma.enset.hospitals.repository.PatientRepository;
import ma.enset.hospitals.security.entities.AppRole;
import ma.enset.hospitals.security.entities.AppUser;
import ma.enset.hospitals.security.repo.AppRoleRepository;
import ma.enset.hospitals.security.repo.AppUserRepository;
import ma.enset.hospitals.security.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class HospitalsApplication implements CommandLineRunner {

    @Autowired
    private PatientRepository patientRepository;

    public static void main(String[] args) {

        SpringApplication.run(HospitalsApplication.class, args);
    }
    @Bean
    CommandLineRunner start(PatientRepository patientRepository){
        return args -> {
            /*patientRepository.save(new Patient().builder().id(null).name("Scott").birthday(new Date()).isSick(false).score(345).build());
            patientRepository.save(new Patient().builder().id(null).name("Beesly").birthday(new Date()).isSick(false).score(345).build());
            patientRepository.save(new Patient().builder().id(null).name("Halpert").birthday(new Date()).isSick(false).score(345).build());*/
            //patientRepository.save(new Patient(null, "Scott", new Date(), false, 345));
            Stream.of("Scott", "Beesly", "Halpert")
                    .forEach(name->{
                        Patient patient= Patient.builder()
                                .name(name)
                                .dateNais(new Date())
                                .malade(false)
                                .score((int) (100+Math.random()*100))
                                .build();
                        patientRepository.save(patient);

                    });

        };
    }
    //@Bean
    CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager) {
        PasswordEncoder passwordEncoder;
        return args -> {
            UserDetails u1= jdbcUserDetailsManager.loadUserByUsername("user11");
            if (u1==null)
                jdbcUserDetailsManager.createUser(
                        User.withUsername("user11").password(passwordEncoder().encode("1234")).disabled(false).roles("USER").build()
                );
            UserDetails u2= jdbcUserDetailsManager.loadUserByUsername("user22");
            if (u2==null)
                jdbcUserDetailsManager.createUser(
                        User.withUsername("user22").password(passwordEncoder().encode("1234")).disabled(true).roles("USER").build()
                );
            UserDetails u3= jdbcUserDetailsManager.loadUserByUsername("admin2");
            if (u3==null)
                jdbcUserDetailsManager.createUser(
                        User.withUsername("admin2").password(passwordEncoder().encode("1234")).disabled(false).roles("USER","ADMIN").build()
                );
        };
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    CommandLineRunner commandLineRunnerUserDetails(AccountService accountService, AppUserRepository appUserRepository, AppRoleRepository appRoleRepository){

        return args -> {

            AppRole appRoleUser=appRoleRepository.findById("USER").get();
            if (appRoleUser==null)
                accountService.addNewRole("USER");
            AppRole appRoleAdmin=appRoleRepository.findById("ADMIN").get();
            if (appRoleAdmin==null)
                accountService.addNewRole("ADMIN");

            AppUser appUser1=appUserRepository.findByUsername("user1");
            if (appUser1==null)
                accountService.addNewUser("user1", "1234", "user1@gmail.com", "1234");

            AppUser appUser2=appUserRepository.findByUsername("user2");
            if (appUser2==null)
                accountService.addNewUser("user2", "1234", "user2@gmail.com", "1234");

            AppUser appUserAdmin=appUserRepository.findByUsername("admin");
            if (appUserAdmin==null)
                accountService.addNewUser("admin", "1234", "admin@gmail.com", "1234");

            accountService.addRoleToUser("user1", "USER");
            accountService.addRoleToUser("user2", "USER");
            accountService.addRoleToUser("admin", "USER");
            accountService.addRoleToUser("admin", "ADMIN");
        };
    }

    @Override
    public void run(String... args) throws Exception {

        Patient patient1 = new Patient(null,"Ahmed",new Date(),true,5);
        Patient patient2 = new Patient(null,"Mouad",new Date(),false,8);
        Patient patient3 = new Patient(null,"yousra",new Date(),true,4);
        patientRepository.save(patient1);
        patientRepository.save(patient2);
        patientRepository.save(patient3);
    }
}
