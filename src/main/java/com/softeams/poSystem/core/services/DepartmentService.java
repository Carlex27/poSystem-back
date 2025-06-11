package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.entities.Department;
import com.softeams.poSystem.core.repositories.DepartmentRepository;
import com.softeams.poSystem.core.services.interfaces.IDepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService implements IDepartmentService {
    private final DepartmentRepository departmentRepository;

    //CRUD

    //CREATE
    public Department createDepartment(Department department) {
        log.info("Creating department: {}", department.getName());
        return departmentRepository.save(department);
    }

    public List<Department> createDepartment(List<Department> departments) {
        log.info("Creating departments:");
        return departmentRepository.saveAll(departments);
    }

    //READ

    public List<Department> getAllDepartments() {
        log.info("Fetching all departments");
        return departmentRepository.findByIsActiveTrue();
    }

    public Department getDepartmentById(Long id) {
        log.info("Fetching department by id: {}", id);
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    public Department getDepartmentByName(String name) {
        log.info("Fetching department by name: {}", name);
        return departmentRepository.findByName(name);
    }

    public List<Department> search(String nameTerm) {
        log.info("Fetching departments by name term: {}", nameTerm);
        return departmentRepository.findByIsActiveTrueAndNameContainingIgnoreCase(nameTerm);
    }

    //UPDATE
    @Transactional
    public Department updateDepartment(Long id, Department department) {
        log.info("Updating department with id: {}", id);
        Department existingDepartment = getDepartmentById(id);
        existingDepartment.setName(department.getName());
        return departmentRepository.save(existingDepartment);
    }

    //DELETE
    @Transactional
    public void deleteDepartment(Long id) {
        log.info("Deleting department with id: {}", id);

        if (id == 1L) {
            throw new RuntimeException("Cannot delete the default department with id 1");
        }

        Department existingDepartment = getDepartmentById(id);
        existingDepartment.setActive(false);
        departmentRepository.save(existingDepartment);
    }

}
