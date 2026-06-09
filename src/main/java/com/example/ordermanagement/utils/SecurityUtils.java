package com.example.ordermanagement.utils;

import com.example.ordermanagement.config.CustomUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static String getCurrentUserId() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        return principal.getId();
    }
}
