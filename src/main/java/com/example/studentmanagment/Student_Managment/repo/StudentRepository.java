package com.example.studentmanagment.Student_Managment.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.studentmanagment.Student_Managment.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @SuppressWarnings({ "null", "unchecked" })
    Student save(Student student);
}
