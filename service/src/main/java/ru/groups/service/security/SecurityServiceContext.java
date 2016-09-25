package ru.groups.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.groups.entity.UserVk;

import java.util.Collection;
import java.util.Collections;

@Service
public class SecurityServiceContext {
    @Autowired @Qualifier("customAuthenticationManager") AuthenticationProvider authenticationProvider;

    public boolean authUser(SecurityUser userDTO) {
        String login = userDTO.getUserName();
        String password = userDTO.getPassword();
        Collection<? extends GrantedAuthority> authorities = userDTO.getAuthorities();
        Authentication authentication =  new UsernamePasswordAuthenticationToken(login, password, authorities);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticationProvider.authenticate(authentication));
        return true;
    }

    public SecurityUser getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((SecurityUser) auth.getPrincipal());
    };

    public Long getLoggedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((SecurityUser) auth.getPrincipal()).getId(); //get logged in username
    };
}
