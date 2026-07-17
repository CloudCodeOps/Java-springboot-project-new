package com.datastore.person.controller;

import com.datastore.person.pojo.Student;
import com.datastore.person.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentRepository studentRepository;

    // CREATE
    @PostMapping("/post")
    public ResponseEntity<String> postStudent(@RequestBody Student student,
                                              HttpServletRequest request) {

        studentRepository.save(student);
        logger.info("Posted student to DB : {}", student.getName());

        return ResponseEntity.ok("Student successfully posted.");
    }

    // READ ONE
    @GetMapping("/get/{name}")
    public ResponseEntity<Student> getStudent(@PathVariable String name) {

        logger.info("Getting student : {}", name);

        return studentRepository.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ ALL
    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {

        logger.info("Getting all students");

        return ResponseEntity.ok(studentRepository.findAll());
    }

    // UPDATE
    @PutMapping("/update/{name}")
    public ResponseEntity<String> updateStudent(
            @PathVariable String name,
            @RequestBody Student updatedStudent) {

        return studentRepository.findByName(name)
                .map(student -> {

                    // Update fields
                    student.setName(updatedStudent.getName());

                    // Add these only if they exist in Student.java
                    // student.setAge(updatedStudent.getAge());
                    // student.setAddress(updatedStudent.getAddress());
                    // student.setEmail(updatedStudent.getEmail());

                    studentRepository.save(student);

                    logger.info("Updated student : {}", name);

                    return ResponseEntity.ok("Student updated successfully.");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Student not found."));
    }

    // DELETE
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<String> deleteStudent(@PathVariable String name) {

        return studentRepository.findByName(name)
                .map(student -> {

                    studentRepository.delete(student);

                    logger.info("Deleted student : {}", name);

                    return ResponseEntity.ok("Student deleted successfully.");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Student not found."));
    }
}
