package fr.digi.absences.service;

import fr.digi.absences.dto.AbsenceDto;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Departement;
import fr.digi.absences.entity.Employee;
import fr.digi.absences.repository.AbsenceRepo;
import fr.digi.absences.repository.EmployeeRepo;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static fr.digi.absences.consts.StatutAbsence.*;

@Service
public class DemandeSrvc {

    private Departement departement;
    private AbsenceRepo absenceRepo;

    private EmployeeRepo employeeRepo;


    //TO DO Histogramme
    public void getMonthAbsence(Departement dep) {
        List<Employee> depEmployee = employeeRepo.findByDepartement(dep);

            for (Employee employee : depEmployee) {
                List<Absence> employeeAbsence = employee.getAbsences();
                for (Absence absence: employeeAbsence) {
                    if (absence.getStatus().equals(VALIDEE)) {
                        int i = absence.getDateFin().getDayOfYear() - absence.getDateDebut().getDayOfYear();
                        LocalDate countDays = absence.getDateDebut();
                        while (i>0) {
//                            if(countDays.getMonth().equals())
                            if (!countDays.getDayOfWeek().equals(DayOfWeek.SUNDAY) && !countDays.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                                //record day for employee in histogramme
                            }
                            countDays.plusDays(1);
                            i--;
                        }

                    }
                }
            }
    }

}
