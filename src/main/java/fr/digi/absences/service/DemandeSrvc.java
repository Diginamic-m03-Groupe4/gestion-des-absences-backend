package fr.digi.absences.service;

import fr.digi.absences.dto.ListAbsByEmployeeDto;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Employee;
import fr.digi.absences.exception.BrokenRuleException;
import fr.digi.absences.mapper.ListAbsByEmployeeMap;
import fr.digi.absences.repository.AbsenceRepo;
import fr.digi.absences.repository.EmployeeRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static fr.digi.absences.consts.StatutAbsence.VALIDEE;

@Service
@Slf4j
@AllArgsConstructor
public class DemandeSrvc {

    private EmployeeRepo employeeRepo;

    private ListAbsByEmployeeMap listAbsByEmployeeMap;


    //TO DO Histogramme
    public ListAbsByEmployeeDto getMonthAbsence(long depID, String email) {
        Optional<Employee> manager= employeeRepo.findByEmail(email);
        if(!manager.get().getRole().equals("MANAGER")){
            throw new BrokenRuleException("Vous n'êtes pas manager vous n'avez pas accés à ces informations");
        }

        Collection<Employee> employees = employeeRepo.findByDepartementId(depID);

        HashMap<String, List<LocalDate>> listAbsByEmployee = new HashMap<>();
        List<LocalDate> listDate = new ArrayList<>();

        for (Employee employee : employees) {
            List<Absence> absences = employee.getAbsences();

            for (Absence absence : absences) {
                if (absence.getStatus().equals(VALIDEE)) {
                    int nbJoursAbs = absence.getDateFin().getDayOfYear() - absence.getDateDebut().getDayOfYear();
                    LocalDate countDays = absence.getDateDebut();
                    while (nbJoursAbs > 0) {
                        if (!countDays.getDayOfWeek().equals(DayOfWeek.SUNDAY) && !countDays.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                            //record day for employee in histogramme
                            LocalDate date = countDays.plusDays(1);
                            listDate.add(date);
                            nbJoursAbs--;
                        }
                    }
                }
                listAbsByEmployee.put(employee.getNom(), listDate);
                listDate = new ArrayList<>();
            }
        }

       return listAbsByEmployeeMap.toListAbsByEmployeeDto(listAbsByEmployee);
    }
}
