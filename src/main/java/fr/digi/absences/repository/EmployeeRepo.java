package fr.digi.absences.repository;

import fr.digi.absences.consts.Roles;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Departement;
import fr.digi.absences.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepo extends JpaRepository<Employee,Long>{

    @Query("select e from Employee e join Departement d on e.departement = d where d = :departement and e.role = :roles")
    List<Employee> findManagers(Departement departement, Roles roles);
    List<Employee> findByDepartement(Departement departement);
    Optional<Employee> findByEmail(String email);
}
