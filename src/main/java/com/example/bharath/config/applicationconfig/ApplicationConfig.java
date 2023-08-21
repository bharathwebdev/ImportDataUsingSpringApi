package com.example.bharath.config.applicationconfig;


import com.example.bharath.Filter.JwtAuthFilter;
import com.example.bharath.config.prefixremove.CustomRoleVoter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {
//    @Bean
//    public UserDetailsService userDetailsService()  {
////        UserDetails user = User.builder()
////                .username("user")
////                .password(passwordEncoder().encode("user"))
////                .roles("USER")
////                .build();
////        UserDetails admin = User.builder()
////                .username("admin")
////                .password(passwordEncoder().encode("admin"))
////                .roles("ADMIN")
////                .build();
//        UserDetails user = User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("user"))
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//        InMemoryUserDetailsManager a = new InMemoryUserDetailsManager(user,admin);
//        return new InMemoryUserDetailsManager(user,admin);
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public JwtAuthFilter jwtAuthFilter(){
        return new JwtAuthFilter();
    }

    @Bean
    public RoleVoter roleVoter() {
        return new CustomRoleVoter();
    }



}
