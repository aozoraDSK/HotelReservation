package com.example.privatepr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.privatepr.models.Role;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientAllRoleDto {
    private String login;
    private Set<Role> roles;
}
