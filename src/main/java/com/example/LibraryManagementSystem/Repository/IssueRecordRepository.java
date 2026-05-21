package com.example.LibraryManagementSystem.Repository;

import com.example.LibraryManagementSystem.Entity.IssueRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRecordRepository extends JpaRepository<IssueRecord,Long> {
}
