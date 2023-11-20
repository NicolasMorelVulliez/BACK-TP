package com.example.demo.model.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_MANTENIMIENTO,
    ROLE_DIOS;

    @Override
    public String getAuthority() {
        return name();
    }
}