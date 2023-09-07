package fr.digi.absences.service;

import fr.digi.absences.dto.AbsenceDto;
import fr.digi.absences.dto.ListAbsByEmployeeDto;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Departement;
import fr.digi.absences.entity.Employee;
import fr.digi.absences.exception.BrokenRuleException;
import fr.digi.absences.mapper.ListAbsByEmployeeMap;
import fr.digi.absences.repository.AbsenceRepo;
import fr.digi.absences.repository.EmployeeRepo;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static fr.digi.absences.consts.StatutAbsence.*;

@Service
public class DemandeSrvc {

    private Departement departement;
    private AbsenceRepo absenceRepo;

    private EmployeeRepo employeeRepo;

    private ListAbsByEmployeeMap listAbsByEmployeeMap;

    //TO DO Histogramme
    public ListAbsByEmployeeDto getMonthAbsence(long depID, String email) {
        Optional<Employee> manager= employeeRepo.findByEmail(email);
        if(!manager.get().getRole().equals("MANAGER")){
            throw new BrokenRuleException("Vous n'êtes pas manager vous n'avez pas accés à ces informations");
        }
        List<Employee> depEmployee = employeeRepo.findByDepartementId(depID);
        HashMap<Employee, List<LocalDate>> listAbsByEmployee = new HashMap<>();
        List<LocalDate> listDate = new ArrayList<>();
        for (Employee employee : depEmployee) {
            List<Absence> employeeAbsence = employee.getAbsences();
            for (Absence absence : employeeAbsence) {
                if (absence.getStatus().equals(VALIDEE)) {
                    int nbJoursAbs = absence.getDateFin().getDayOfYear() - absence.getDateDebut().getDayOfYear();
                    LocalDate countDays = absence.getDateDebut();
                    while (nbJoursAbs > 0) {
//                            if(countDays.getMonth().equals())
                        if (!countDays.getDayOfWeek().equals(DayOfWeek.SUNDAY) && !countDays.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                            //record day for employee in histogramme
                            LocalDate date = countDays.plusDays(1);
                            listDate.add(date);
                        }
                        nbJoursAbs--;
                    }
                }
            }
            listAbsByEmployee.put(employee, listDate);
            listDate = new ArrayList<>();
        }

        return listAbsByEmployeeMap.toListAbsByEmployeeDto(listAbsByEmployee);
    }
}
