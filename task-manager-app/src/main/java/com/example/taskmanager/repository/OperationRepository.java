package com.example.taskmanager.repository;

import com.example.taskmanager.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    Operation findByRole(String role);
}