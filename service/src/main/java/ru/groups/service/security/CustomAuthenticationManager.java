package ru.groups.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.groups.entity.UserVk;
import ru.groups.service.UserService;
import java.util.*;

@Component
public class CustomAuthenticationManager implements AuthenticationProvider {

    @Autowired UserService userService;

    public Authentication loadUserByUsername(UserVk user){
        Set<String> roles = new HashSet<String>();
        roles.add("ROLE_TEACHER");
        List<GrantedAuthority> authorities = buildUserAuthority(roles);
        SecurityUser customUser = new SecurityUser(user.getId(), user.getUserName(), user.getUserAccessToken(), true, true, true, true, authorities);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(customUser, customUser.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticate(authentication));
        return authentication;
    }

    private List<GrantedAuthority> buildUserAuthority(Set<String> userRoles) {
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
        for (String userRole : userRoles){
            setAuths.add(new SimpleGrantedAuthority(userRole));
        }
        List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(setAuths);
        return result;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authentication;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
