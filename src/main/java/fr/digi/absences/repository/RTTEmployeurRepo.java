package fr.digi.absences.repository;

import fr.digi.absences.consts.StatutAbsenceEmployeur;
import fr.digi.absences.entity.RTTEmployeur;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RTTEmployeurRepo extends JpaRepository<RTTEmployeur, Long> {
    boolean existsByDate(LocalDate date);
    List<RTTEmployeur> findByStatutAbsenceEmployeur(StatutAbsenceEmployeur status);
    @Query("select r from RTTEmployeur r where year(r.date) = :annee")
    List<RTTEmployeur> findByAnnee(int annee);
}
