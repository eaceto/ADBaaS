package dev.eaceto.mobile.tools.android.adb.api.security;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

public class APIKeyAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private final String headerName;

    public APIKeyAuthenticationFilter(final String headerName) {
        this.headerName = headerName;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader(headerName);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return null;
    }
}
