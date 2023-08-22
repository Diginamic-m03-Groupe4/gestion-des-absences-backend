package fr.digi.absences.service.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class MailPerson {
    private String email;
    private String name;
}
