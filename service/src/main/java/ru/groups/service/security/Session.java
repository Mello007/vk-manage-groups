package ru.groups.service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class Session {

    public long getLoggedUserId() {
        CustomUser customUser = (CustomUser) getAuthentication().getPrincipal();
        return customUser.getId();
    }

    private Authentication getAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new RuntimeException("Преподователь не найден!");
        }
        return auth;
    }
}