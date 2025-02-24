package com.example.studentmanagment.Student_Managment.service;


import com.example.studentmanagment.Student_Managment.model.Student;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
//import com.example.model.Student;
import java.util.List;
import java.util.Optional;

@Service
public interface StudentService {
    

    // com.example.studentmanagment.Student_Managment.service.Student saveStudent(Student student); // Add a new student

    
    Student saveStudent(Student student);
    List<Student> getAllStudents(); // Get all students
    Optional<Student> getStudentById(Long id); // Get student by ID
    Student updateStudent(Long id, Student updatedStudent); // Update student
    void deleteStudentById(Long id); // Delete student
    // ✅ New Method: Fetch name meaning & save it
    String fetchNameMeaning(String name);

     List<Document> searchSimilarText(String info);
} 
