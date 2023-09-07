package fr.digi.absences.dto;

import fr.digi.absences.entity.Employee;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Data
@Builder
public class ListAbsByEmployeeDto {

    private HashMap<Employee, List<LocalDate>> listAbsByEmployeeDto;
}
