package dev.eaceto.mobile.tools.android.adb.api.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

public class APIKeyAuthenticationManager implements AuthenticationManager {

    private final List<String> allowedAPIKeys;

    public APIKeyAuthenticationManager(List<String> allowedAPIKeys) {
        super();
        this.allowedAPIKeys = allowedAPIKeys;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String apiKey = (String) authentication.getPrincipal();

        if (!allowedAPIKeys.contains(apiKey)) {
            throw new BadCredentialsException("The API key was not found or not the expected value.");
        } else {
            authentication.setAuthenticated(true);
            return authentication;
        }
    }
}
