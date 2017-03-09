package ru.groups.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.UserVk;
import ru.groups.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service("userDetailsService")
public class MyUserDetailService implements UserDetailsService {

    @Autowired private UserService userService;

    @Transactional(readOnly=true)
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

        UserVk userVk = userService.getUserByName(username);
        if (userVk == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Set<String> roles = new HashSet<String>();
        roles.add("ROLE_LOGGED_USER");
        List<GrantedAuthority> authorities = buildUserAuthority(roles);
        return buildUserForAuthentication(userVk, authorities);
    }

    private User buildUserForAuthentication(UserVk userVk,
                                            List<GrantedAuthority> authorities) {
        return new CustomUser(userVk.getId(), userVk.getLogin(), userVk.getPassword(),
               true, true, true, true, authorities);
    }

    private List<GrantedAuthority> buildUserAuthority(Set<String> userRoles) {

        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

        // Build user's authorities
        for (String userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(userRole));
        }
        List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);
        return Result;
    }
}
