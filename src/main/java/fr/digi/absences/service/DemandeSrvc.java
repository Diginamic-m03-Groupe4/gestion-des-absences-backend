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

//    private DepartementRepo departementRepo;


//    public List<Absence> getDepDemande(Long departementId) {
//
//    }

    //Get all departement employee absences
    //get all status ?

//    public
//    List<Absence> demandesAbsences = absenceRepo.getListAbsencesDemandesOfDepartement(departement.getId());

    //Manage Demandes status
    //need to differentiate modifiable or not... How ?
    private void applyBussinessLogic(AbsenceDto absenceDto) {
        if (absenceDto.getStatus().equals(INITIALE)){
            //can modify
        } else {
            //can't
        }
    }


    //to set in method?
    //i hate optional fml
    public void validateAbsence(Absence absence) {
        absence.setStatus(ATTENTE_VALIDATION);
        //need to send to db ?
    }

    public void refuseAbsence(Absence absence) {
        absence.setStatus(REJETEE);
    }



    //Histogramme
    //can't get list, whyyyyyyyy
    //localDate calculate for each day ??
    //void or list ?
    public void getMonthAbsence(long depID) {
        List<Employee> depEmployee = employeeRepo.findByDepartementId(depID);

            for (Employee employee : depEmployee) {
                List<Absence> employeeAbsence = employee.getAbsences();
                for (Absence absence: employeeAbsence) {
                    if (absence.getStatus().equals(VALIDEE)) {
                        int i = absence.getDateFin().getDayOfYear() - absence.getDateDebut().getDayOfYear();
                        LocalDate countDays = absence.getDateDebut();
                        while (i>0) {
                            if (!countDays.getDayOfWeek().equals(DayOfWeek.SUNDAY) && !countDays.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                                //record day for employee in histogramme, how ?
                            }
                            countDays.plusDays(1);
                            i--;
                        }

                    }
                }
            }
    }

}
