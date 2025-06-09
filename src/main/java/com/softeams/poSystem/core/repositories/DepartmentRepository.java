package com.softeams.poSystem.core.repositories;

import com.softeams.poSystem.core.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByName(String name);
    List<Department> findByIsActiveTrue();
    List<Department> findByNameContainingIgnoreCase(String nameTerm);
}
