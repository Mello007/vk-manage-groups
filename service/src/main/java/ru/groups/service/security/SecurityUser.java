package ru.groups.service.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.groups.entity.UserVk;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class SecurityUser extends UserVk implements UserDetails {
    private long id;

    public SecurityUser(UserVk user) {
        this.setId(user.getId());
        this.setUserName(user.getUserName());
        this.setUserAccessToken(user.getUserAccessToken());
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        return roles;
    }

    @Override
    public String getPassword() {
        return super.getUserAccessToken();
    }

    @Override
    public String getUsername() {
        return super.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}