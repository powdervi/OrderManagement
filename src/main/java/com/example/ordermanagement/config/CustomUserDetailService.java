package com.example.ordermanagement.config;

import com.example.ordermanagement.common.UserRole;
import com.example.ordermanagement.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        com.example.ordermanagement.entity.User user = userRepo.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("sai username");
        }

        UserRole role = user.getRole();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+role.name());
        return new User(username, user.getPasswordHash(), List.of(authority));
    }

}
