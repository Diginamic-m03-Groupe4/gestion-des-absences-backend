package fr.digi.absences.consts;

public enum Roles {
    EMPLOYEE("EMPLOYEE"), MANAGER("MANAGER"), ADMINISTRATEUR("ADMINISTRATEUR");
    private String role;

    Roles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
