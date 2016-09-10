package ru.groups.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.UserVk;

import java.util.*;

@Component
public class CustomAuthenticationManager implements AuthenticationProvider {

    public Authentication loadUserByUsername(UserVk user){
        Set<String> roles = new HashSet<String>();
        roles.add("ROLE_TEACHER");
        List<GrantedAuthority> authorities = buildUserAuthority(roles);
        SecurityUser customUser = new SecurityUser(user.getId(), user.getUserName(), user.getUserAccessToken(), true, true, true, true, authorities);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(customUser, customUser.getPassword(), customUser.getAuthorities());
        return this.authenticate(authentication);
    }

    private List<GrantedAuthority> buildUserAuthority(Set<String> userRoles) {
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
        for (String userRole : userRoles){
            setAuths.add(new SimpleGrantedAuthority(userRole));
        }
        List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(setAuths);
        return result;
    }

    @Autowired MyUserDetailService userDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (user == null || !user.getUsername().equalsIgnoreCase(username)) {
            throw new BadCredentialsException("Username not found.");
        }
        if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException("Wrong password.");
        }

        authentication.setAuthenticated(true);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
