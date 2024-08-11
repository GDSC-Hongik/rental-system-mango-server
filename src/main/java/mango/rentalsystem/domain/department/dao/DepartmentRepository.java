package mango.rentalsystem.domain.department.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mango.rentalsystem.domain.department.domain.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
