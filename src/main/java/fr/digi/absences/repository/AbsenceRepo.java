package fr.digi.absences.repository;

import fr.digi.absences.entity.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AbsenceRepo extends JpaRepository<Absence, Long> {
    Absence findByMotif(String motif);
    @Query(nativeQuery = true,
        value = "select count(*) as abs from "
            + "(select * from absence a where ?1 between a.date_debut and a.date_fin"
            + " union select * from absence a where ?2 between a.date_debut and a.date_fin"
            + " union select * from absence where date_debut between ?1 and ?2"
            + " union select * from absence a where date_fin between ?1 and ?2)"
            + " as abs join employee e on abs.employee_id = e.id where e.email = ?3;")
    Integer getNbAbsencesBetweenDateDebutAndDateFin(LocalDate dateDebut, LocalDate dateFin, String email);
}
