package com.softeams.poSystem.core.controllers;

import com.softeams.poSystem.core.dtos.department.DepartmentDto;
import com.softeams.poSystem.core.dtos.product.ProductRequest;
import com.softeams.poSystem.core.entities.Department;
import com.softeams.poSystem.core.entities.Product;
import com.softeams.poSystem.core.mappers.DepartmentMapper;
import com.softeams.poSystem.core.services.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;
    //CRUD

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("/create")
    public ResponseEntity<?> createDepartment(
            @Valid
            @RequestBody DepartmentDto department
    ) {
        log.info("[DepartmentController | createDepartment] Creating department: {}", department);
        return ResponseEntity.ok(departmentService.createDepartment(departmentMapper.mapToEntity(department)));
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("/createMany")
    public ResponseEntity<?> createDepartments(
            @Valid
            @RequestBody List<DepartmentDto> departmentDtos
    ) {
        log.info("[DepartmentController | createDepartments] Creating departments");
        List<Department> departments = departmentMapper.mapToEntity(departmentDtos);
        return ResponseEntity.ok(departmentService.createDepartment(departments));
    }

    //CREATE


    @GetMapping("/findAll")
    public ResponseEntity<?> getAllDepartments() {
        log.info("[DepartmentController | getAllDepartments] Fetching all departments");
        return ResponseEntity.ok(departmentMapper.mapToDto(departmentService.getAllDepartments()));
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> getDepartmentById(
            @PathVariable Long id
    ) {
        log.info("[DepartmentController | getDepartmentById] Fetching department by id: {}", id);
        return ResponseEntity.ok(departmentMapper.mapToDto(departmentService.getDepartmentById(id)));
    }

    @GetMapping("/findByName/{name}")
    public ResponseEntity<?> getDepartmentByName(
            @PathVariable String name
    ) {
        log.info("[DepartmentController | getDepartmentByName] Fetching department by name: {}", name);
        return ResponseEntity.ok(departmentMapper.mapToDto(departmentService.getDepartmentByName(name)));
    }

    @GetMapping("/search")
    public ResponseEntity<?> getDepartmentsByName(
            @RequestParam String query
    ) {
        log.info("[DepartmentController | getDepartmentsByName] Fetching departments by name: {}", query);
        return ResponseEntity.ok(departmentMapper.mapToDto(departmentService.search(query)));
    }

    //UPDATE

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDepartment(
            @Valid
            @RequestBody DepartmentDto departmentDto,
            @PathVariable Long id
    ) {
        log.info("[DepartmentController | updateDepartment] Updating department with id: {}", id);
        Department updatedDepartment = departmentService.updateDepartment(id,departmentMapper.mapToEntity(departmentDto));
        return ResponseEntity.ok(departmentMapper.mapToDto(updatedDepartment));
    }

    //Delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDepartment(
            @PathVariable Long id
    ) {
        log.info("[DepartmentController | deleteDepartment] Deleting department with id: {}", id);
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok("Department deleted successfully");
    }
}
