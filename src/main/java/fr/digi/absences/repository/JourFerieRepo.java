package fr.digi.absences.repository;

import fr.digi.absences.entity.JourFerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JourFerieRepo extends JpaRepository<JourFerie,Long>{
    @Query("select jf from JourFerie jf where year(jf.date) = :annee")
    List<JourFerie> findByAnnee(int annee);
}
