package by.dmitry_skachkov.taskservice.core.utils;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;


public class SecurityUtils {
    public static UUID getAuthenticatedUserUuid() {
        UserAuth userAuth = (UserAuth) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return UUID.fromString(userAuth.getUUID());
    }
}
