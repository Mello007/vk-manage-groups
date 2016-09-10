package ru.groups.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired UserService userService;


    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        UserVk user = userService.getUserByName(username);
        return new SecurityUser(user.getId(), user.getUserName(), user.getUserAccessToken(), false, false, false, false, null);
    }

    @Transactional
    public Authentication loadUserByUsername(UserVk user) throws UsernameNotFoundException {
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не определен и не получен по ВК апи");
        }
        Set<String> roles = new HashSet<String>();
        roles.add("ROLE_TEACHER");
        List<GrantedAuthority> authorities = buildUserAuthority(roles);
        SecurityUser customUser = new SecurityUser(user.getId(), user.getUserName(), user.getUserAccessToken(), true, true, true, true, authorities);
        return new UsernamePasswordAuthenticationToken(customUser, customUser.getPassword(), customUser.getAuthorities());
    }

    @Transactional
    private List<GrantedAuthority> buildUserAuthority(Set<String> userRoles) {
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
        for (String userRole : userRoles){
            setAuths.add(new SimpleGrantedAuthority(userRole));
        }
        List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(setAuths);
        return result;
    }
}
