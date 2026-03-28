package ma.ens.security.services;

import ma.ens.security.entities.Role;
import ma.ens.security.entities.User;
import ma.ens.security.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        return new org.springframework.security.core.userdetails.User(
                foundUser.getUsername(),
                foundUser.getPassword(),
                foundUser.isActive(),
                true,
                true,
                true,
                convertRoles(foundUser.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> convertRoles(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}