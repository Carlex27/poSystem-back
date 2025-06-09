package com.softeams.poSystem.core.mappers;

import com.softeams.poSystem.core.dtos.department.DepartmentDto;
import com.softeams.poSystem.core.entities.Department;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DepartmentMapper {
    public Department mapToEntity(DepartmentDto departmentDto) {
        if (departmentDto == null) {
            return null;
        }
        return Department.builder()
                .name(departmentDto.name())
                .isActive(departmentDto.isActive())
                .build();
    }

    public List<Department> mapToEntity(List<DepartmentDto> departmentDtos) {
        if (departmentDtos == null) {
            return null;
        }
        return departmentDtos.stream()
                .map(this::mapToEntity)
                .toList();
    }

    public DepartmentDto mapToDto(Department department) {
        if (department == null) {
            return null;
        }
        return new DepartmentDto(
                department.getName(),
                department.isActive()
        );
    }

    public List<DepartmentDto> mapToDto(List<Department> departments) {
        if (departments == null) {
            return null;
        }
        return departments.stream()
                .map(this::mapToDto)
                .toList();
    }
}
