package dev.eaceto.mobile.tools.android.adb.api.configuration;

import dev.eaceto.mobile.tools.android.adb.api.security.APIKeyAuthenticationFilter;
import dev.eaceto.mobile.tools.android.adb.api.security.APIKeyAuthenticationManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.List;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String API_KEY_AUTH_HEADER_NAME = "X-API-Key";

    @Value("${adbaas.api.keys.allowed}")
    private List<String> allowedAPIKeys;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        APIKeyAuthenticationFilter filter = new APIKeyAuthenticationFilter(API_KEY_AUTH_HEADER_NAME);
        filter.setAuthenticationManager(new APIKeyAuthenticationManager(allowedAPIKeys));

        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilter(filter)
                .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest()
                .authenticated();

    }

}