package ma.ens.security;

import ma.ens.security.entities.Role;
import ma.ens.security.entities.User;
import ma.ens.security.repositories.UserRepository;
import ma.ens.security.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepo,
                               RoleRepository roleRepo,
                               PasswordEncoder passwordEncoder) {
        return args -> {

            if (roleRepo.count() == 0) {
                Role adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                roleRepo.save(adminRole);

                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("1234"));
                admin.setActive(true);
                admin.addRole(adminRole);

                userRepo.save(admin);
            }
        };
    }
}