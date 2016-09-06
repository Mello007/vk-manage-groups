package ru.groups.service.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.groups.entity.UserVk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



@Service("userDetailsService")
public class MyUserDetailService {

    @Transactional(readOnly=true)
    public void loadUserByUsername(UserVk user) throws UsernameNotFoundException {
        if (user == null) {
            throw new UsernameNotFoundException("Студент с таким именем не найден!");
        }
        Set<String> roles = new HashSet<String>();
        roles.add("ROLE_TEACHER");
        List<GrantedAuthority> authorities = buildUserAuthority(roles);
        Session session = new Session();
        session.setAuthentication(new CustomUser(user.getId(), user.getUserName() + " " + user.getUserLastName(), user.getUserAccessToken(), true, true, true, true, authorities));
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
