package fr.digi.absences.entity;

import lombok.AllArgsConstructor;

public enum Roles {
    EMPLOYEE("EMPLOYEE"), MANAGER("MANAGER"), ADMINISTRATEUR("ADMIN");
    private String role;

    Roles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
