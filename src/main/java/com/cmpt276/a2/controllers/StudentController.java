package com.cmpt276.a2.controllers;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.cmpt276.a2.models.Student;
import com.cmpt276.a2.models.StudentRepository;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {

     // Constants for height and weight bounds
    private static final double MAX_HEIGHT = 250;
    private static final double MIN_HEIGHT = 40;
    private static final double MAX_WEIGHT = 600;
    private static final double MIN_WEIGHT = 30;

    @Autowired
    private StudentRepository studentRepository;

    // Handler method for displaying the list of students
    @GetMapping("/view")
    public String listStudents(Model model) {
        List<Student> studentList = studentRepository.findAll();
        model.addAttribute("students", studentList);
        return "users/view-students";
    }

    // Handler method for displaying the add student form
    @GetMapping("/add")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "users/add-student";
    }

    // Method to add student from form submission
    @PostMapping("/add")
    public String addStudent(@ModelAttribute("student") Student student, Model model) {

    // Handle errors for out of bounds height and weight
    if (student.getHeight() > MAX_HEIGHT || student.getHeight() < MIN_HEIGHT) {
        model.addAttribute("heightError", "Height is out of bounds! Max = 250, Min = 40");
        return "users/add-student";
    }
    
    if (student.getWeight() > MAX_WEIGHT || student.getWeight() < MIN_WEIGHT) {
        model.addAttribute("weightError", "Weight is out of bounds! Max = 600, Min = 30");
        return "users/add-student";
    }
    // Save the student to the database and redirect
        studentRepository.save(student);
        return "redirect:/students/view";
    }

    // Handler for displaying the edit student form
    @GetMapping("/edit/{id}")
    public String showEditStudentForm(@PathVariable("id") Long id, Model model) {
        // Retrieve student object from database with id 
        Optional<Student> optionalStudent = studentRepository.findById(id);
        Student student = optionalStudent.get();
        model.addAttribute("student", student);
        return "users/edit-student";
    }

    // Method to update students in database form form submission
    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable("id") Long id, @ModelAttribute("student") Student updatedStudent, Model model) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        Student existingStudent = optionalStudent.get();

        // Update the student's attributes
        existingStudent.setName(updatedStudent.getName());
        existingStudent.setWeight(updatedStudent.getWeight());
        existingStudent.setHeight(updatedStudent.getHeight());
        existingStudent.setHairColor(updatedStudent.getHairColor());
        existingStudent.setGpa(updatedStudent.getGpa());
        existingStudent.setAge(updatedStudent.getAge());
        existingStudent.setEmail(updatedStudent.getEmail());

        // Height and weight bounds check
        if (existingStudent.getHeight() > MAX_HEIGHT || existingStudent.getHeight() < MIN_HEIGHT) {
            model.addAttribute("heightError", "Height is out of bounds! Max = 250, Min = 40");
            model.addAttribute("student", existingStudent);
            return "users/edit-student";
        }
        if (existingStudent.getWeight() > MAX_WEIGHT || existingStudent.getWeight() < MIN_WEIGHT) {
            model.addAttribute("weightError", "Weight is out of bounds! Max = 600, Min = 30");
            model.addAttribute("student", existingStudent);
            return "users/edit-student";
        }
        studentRepository.save(existingStudent);
        return "redirect:/students/view";
    }

    // Handler method for deleting a student
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id, Model model) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            studentRepository.deleteById(id);
            return "redirect:/students/view";
        } 
        else {
            model.addAttribute("errorMessage", "Invalid student ID");
            return "users/view-students";
        }
    }
}


