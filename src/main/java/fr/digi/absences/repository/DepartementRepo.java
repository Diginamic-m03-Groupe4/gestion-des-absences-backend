package fr.digi.absences.repository;

import fr.digi.absences.entity.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartementRepo extends JpaRepository<Departement, Long> {
}
