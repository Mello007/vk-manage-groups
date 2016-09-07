package ru.groups.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired Session session;

    @Transactional(readOnly=true)
    public void loadUserByUsername(UserVk user) throws UsernameNotFoundException {
        if (user == null) {
            throw new UsernameNotFoundException("Студент с таким именем не найден!");
        }
        Set<String> roles = new HashSet<String>();
        roles.add("ROLE_TEACHER");
        List<GrantedAuthority> authorities = buildUserAuthority(roles);
        CustomUser customUser = new CustomUser(user.getId(), user.getUserName() + " " + user.getUserLastName(), user.getUserAccessToken(), true, true, true, true, authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(customUser, customUser.getPassword(), customUser.getAuthorities()));
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
