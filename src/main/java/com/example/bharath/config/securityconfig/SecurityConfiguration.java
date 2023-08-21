package com.example.bharath.config.securityconfig;
import com.example.bharath.Filter.JwtAuthFilter;
import com.example.bharath.customentrypoint.CustomAuthenticationEntryPoint;
import com.example.bharath.roles.Roles;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration  {

    @Autowired
    private final JwtAuthFilter jwtAuthFilter;

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
    };
   private static final String[] PERMITTEDWHITELIST = {

    "/api/v2/check-auth",



    };

private static  final String[] REGEXWHITELIST = {
        "/api/[^/]+/getallfiles",
        "/api/[^/]+/register",
        "/api/[^/]+/authenticate",
        "/api/[^/]+/authority",
        "/api/[^/]+/",
        "/api/[^/]+/check-roles",
        "/api/[^/]+/login",
        "/api/[^/]+/check-auth",
        "/api/[^/]+/logout",
};
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
        http .
                csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/**/excelToPostgres")).hasRole(Roles.ADMIN)
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/**/getData")).hasAnyRole(Roles.USER,Roles.ADMIN)
                                .requestMatchers(PERMITTEDWHITELIST).permitAll()
                                .requestMatchers(new RegexRequestMatcher(REGEXWHITELIST[0],null)).permitAll()
                                .requestMatchers(new RegexRequestMatcher(REGEXWHITELIST[1],null)).permitAll()
                                .requestMatchers(new RegexRequestMatcher(REGEXWHITELIST[2],null)).permitAll()
                                .requestMatchers(new RegexRequestMatcher(REGEXWHITELIST[3],null)).permitAll()
                                .requestMatchers(new RegexRequestMatcher(REGEXWHITELIST[4],null)).permitAll()
                                .requestMatchers(new RegexRequestMatcher(REGEXWHITELIST[5],null)).permitAll()
                                .requestMatchers(new RegexRequestMatcher(REGEXWHITELIST[6],null)).permitAll()
                                .requestMatchers(new RegexRequestMatcher(REGEXWHITELIST[7],null)).permitAll()
                                .requestMatchers(new RegexRequestMatcher(REGEXWHITELIST[8],null)).permitAll()
                                .requestMatchers(SWAGGER_WHITELIST).permitAll()

                )

                .sessionManagement(a->
                        a.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(a->a.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("https://localhost:3000"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        BasicAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();
        entryPoint.setRealmName("my Own");
        return entryPoint;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }



    @Bean
    public RoleHierarchy customRoleHierarchy() {
        return new CustomRoleHierarchy();
    }


}

