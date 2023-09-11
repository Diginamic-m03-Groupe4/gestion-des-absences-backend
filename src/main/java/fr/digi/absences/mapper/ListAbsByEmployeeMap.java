package fr.digi.absences.mapper;

import fr.digi.absences.dto.ListAbsByEmployeeDto;
import fr.digi.absences.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ListAbsByEmployeeMap {

    public ListAbsByEmployeeDto toListAbsByEmployeeDto(HashMap<String, List<LocalDate>> listAbsByEmployee) {
        return ListAbsByEmployeeDto.builder().listAbsByEmployeeDto(listAbsByEmployee).build();
    }

}
