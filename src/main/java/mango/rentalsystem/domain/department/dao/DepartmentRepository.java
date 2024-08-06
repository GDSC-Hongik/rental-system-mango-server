package mango.rentalsystem.domain.department.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import mango.rentalsystem.domain.department.domain.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
