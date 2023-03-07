package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class StudentServices {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    public void addStudent(Student student) {
        // check email taken or not
        Boolean existEmail = studentRepository
                .selectExistsEmail(student.getEmail());
        if (existEmail) {

            throw new BadRequestException(
                    "Email " + student.getEmail() + " taken");


        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(
                    "Student with id " + studentId + " does not exist"
            );
        }
        studentRepository.deleteById(studentId);
    }
}
