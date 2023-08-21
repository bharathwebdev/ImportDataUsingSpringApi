package com.example.bharath.config.securityconfig;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomRoleHierarchy implements RoleHierarchy {
    @Override
    public Collection<? extends GrantedAuthority> getReachableGrantedAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<GrantedAuthority> reachableAuthorities = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            String authorityName = authority.getAuthority().toUpperCase();
            if (!authorityName.startsWith("ROLE_")) {
                reachableAuthorities.add(new SimpleGrantedAuthority("ROLE_" + authorityName));
            } else {
                reachableAuthorities.add(authority);
            }
        }
        return reachableAuthorities;
    }
}