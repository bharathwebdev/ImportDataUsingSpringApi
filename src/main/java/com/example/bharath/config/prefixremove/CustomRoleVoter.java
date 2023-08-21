package com.example.bharath.config.prefixremove;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomRoleVoter extends RoleVoter {
    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        // Extract all roles from the authentication and remove the "ROLE_" prefix if present
        Collection<GrantedAuthority> authorities = authentication.getAuthorities()
                .stream()
                .map(this::removeRolePrefix)
                .collect(Collectors.toList());

        // Extract the required roles from the attributes and check if any role matches
        for (ConfigAttribute attribute : attributes) {
            if (supports(attribute)) {
                String requiredRole = attribute.getAttribute();
                if (authorities.stream().anyMatch(authority -> authority.getAuthority().equals(requiredRole))) {
                    return ACCESS_GRANTED;
                }
            }
        }

        return ACCESS_DENIED;
    }

    private GrantedAuthority removeRolePrefix(GrantedAuthority authority) {
        String authorityName = authority.getAuthority();
        if (authorityName.startsWith("ROLE_")) {
            String roleWithoutPrefix = authorityName.substring(5);
            return new CustomSimpleGrantedAuthority(roleWithoutPrefix);
        }
        return authority;
    }
}


