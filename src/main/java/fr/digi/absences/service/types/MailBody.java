package fr.digi.absences.service.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class MailBody {
    private String name;
    private MailPerson sender;
    private List<MailPerson> to;
    private String subject;
    private String htmlContent;
}
