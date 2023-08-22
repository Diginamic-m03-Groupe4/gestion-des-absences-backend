package fr.digi.absences.repository;

import fr.digi.absences.consts.StatutAbsence;
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
    @Query("select a from Absence a where year(a.dateDebut) = :annee")
    List<Absence> findAllByAnnee(int annee);
    @Query(nativeQuery = true,
        value = "select count(*) as abs from "
            + "(select * from absence a where ?1 between a.date_debut and a.date_fin"
            + " union select * from absence a where ?2 between a.date_debut and a.date_fin"
            + " union select * from absence where date_debut between ?1 and ?2"
            + " union select * from absence a where date_fin between ?1 and ?2)"
            + " as abs join employee e on abs.employee_id = e.id where e.email = ?3;")
    Integer getNbAbsencesBetweenDateDebutAndDateFin(LocalDate dateDebut, LocalDate dateFin, String email);

    @Query("select a from Absence a where ? between a.dateDebut and a.dateFin")
    List<Absence> findAbsenceMatchRttEmployeur(LocalDate date);
}
