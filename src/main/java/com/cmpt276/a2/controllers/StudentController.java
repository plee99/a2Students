package com.cmpt276.a2.controllers;

// import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmpt276.a2.models.Student;
import com.cmpt276.a2.models.StudentRepository;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class StudentController {

    @Autowired
    private StudentRepository studentRepo;

    @GetMapping("/students/view")
    public String getAllStudents(Model model) {
        System.out.println("get students");

        // TODO: get all students from database
        List<Student> students = studentRepo.findAll();

        // students.add(new Student("Sarah", 120, 180, "black", 3.5));
        // students.add(new Student("Bobby", 150, 17, "green", 3.1));
        // students.add(new Student("Peter", 75, 200, "blonde", 3.2));
        // students.add(new Student("Jack", 120, 176, "red", 2.4));

        //end of database call

        model.addAttribute("std", students);
        return "students/showAll";
    }

    @PostMapping("/students/add")
    public String addStudent(@RequestParam Map<String, String> newStudent, HttpServletResponse response) {
        System.out.println("ADD student");
        String newName = newStudent.get("name");
        int newWeight = Integer.parseInt(newStudent.get("weight"));
        int newHeight = Integer.parseInt(newStudent.get("height"));
        String newHair = newStudent.get("hairColor");
        double newGPA = Double.parseDouble(newStudent.get("gpa"));
        studentRepo.save(new Student(newName, newWeight, newHeight, newHair, newGPA));
        response.setStatus(201);;
        return "students/addedStudent";

    }
}
