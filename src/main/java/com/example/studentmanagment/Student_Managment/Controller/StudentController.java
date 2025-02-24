package com.example.studentmanagment.Student_Managment.Controller;

import com.example.studentmanagment.Student_Managment.model.Student;
import com.example.studentmanagment.Student_Managment.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping("/")
public class StudentController {

    @Autowired
    private StudentService studentService;
    
    @GetMapping("/")
    public String getAllStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        model.addAttribute("student", new Student());
        return "index";
    }


    @PostMapping("/addStudent")
    public String saveStudent(@ModelAttribute Student student, Model model) {
        // Fetch the meaning of the name
        String meaning = studentService.fetchNameMeaning(student.getName());
        student.setMeaning(meaning); // Set the meaning before saving
        studentService.saveStudent(student); // Save the student
        return "redirect:/"; // Redirect to the list of students
    }



    // Fetch the meaning of the name using an API (Groq AI)
    @GetMapping("/getNameMeaning")
    @ResponseBody
    public String getNameMeaning(@RequestParam String name) {
        return studentService.fetchNameMeaning(name);
    }
    
    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Long id, Model model) {
        Optional<Student> student = studentService.getStudentById(id);
        
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
        } else {
            model.addAttribute("error", "Student not found.");
        }
        
        return "index";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return "redirect:/";
    }

    @GetMapping("/getSimilarResults")
    public String similarResults(@RequestParam String info){
        return studentService.searchSimilarText(info).toString();
    }
}



