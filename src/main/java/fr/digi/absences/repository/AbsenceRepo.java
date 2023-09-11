package fr.digi.absences.repository;

import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.dto.AbsenceDto;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Employee;
import fr.digi.absences.entity.RTTEmployeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AbsenceRepo extends JpaRepository<Absence, Long> {
    @Query("select a from Absence a where a.employee = :employee and a.dateDebut between :dateDebutAnnee and :dateFinAnnee")
    List<Absence> findByDateDebutBetweenAndEmployee(LocalDate dateDebutAnnee, LocalDate dateFinAnnee, Employee employee);

    List<Absence> findByDateDemandeAndStatus(LocalDate dateDemande, StatutAbsence statut);
    @Query("select a from Absence a where year(a.dateDebut) = :annee and a.employee.email = :email")
    List<Absence> findAllByAnnee(int annee, String email);
    @Query(nativeQuery = true,
        value = "select count(*) as abs from "
            + "(select * from absence a where ?1 between a.date_debut and a.date_fin"
            + " union select * from absence a where ?2 between a.date_debut and a.date_fin"
            + " union select * from absence a where date_debut between ?1 and ?2"
            + " union select * from absence a where date_fin between ?1 and ?2)"
            + " as abs join employee e on abs.employee_id = e.id where e.email = ?3 and not abs.status = 3")
    Integer getNbAbsencesBetweenDateDebutAndDateFin(LocalDate dateDebut, LocalDate dateFin, String email);
    @Query(nativeQuery = true,
            value = "select count(*) as abs from "
                    + "(select * from absence a where ?1 between a.date_debut and a.date_fin and a.id != ?4"
                    + " union select * from absence a where ?2 between a.date_debut and a.date_fin and a.id != ?4"
                    + " union select * from absence a where date_debut between ?1 and ?2 and a.id != ?4"
                    + " union select * from absence a where date_fin between ?1 and ?2 and a.id != ?4)"
                    + " as abs join employee e on abs.employee_id = e.id where e.email = ?3 and not abs.status = 3")
    Integer getNbAbsencesBetweenDateDebutAndDateFinWOAbs(LocalDate dateDebut, LocalDate dateFin, String email, Long id);

    @Query("select a from Absence a where a.employee.departement.id = :departementId")
    List<Absence> getListAbsencesDemandesOfDepartement(Long departementId);

    @Query("select a from Absence a where ?1 between a.dateDebut and a.dateFin")
    List<Absence> findAbsenceMatchRttEmployeur(LocalDate date);
}
