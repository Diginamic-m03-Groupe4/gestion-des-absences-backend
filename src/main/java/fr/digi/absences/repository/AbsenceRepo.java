package fr.digi.absences.repository;

import fr.digi.absences.entity.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceRepo extends JpaRepository<Absence, Long> {
    Absence findByMotif(String motif);
}
