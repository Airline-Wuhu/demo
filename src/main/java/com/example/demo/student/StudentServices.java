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

    public void updateStudent(Long studentId, Student student) {
        if (!studentRepository.existsById(studentId)) throw new BadRequestException("Student with id " + studentId + " does not exist");
        Student modified = studentRepository.getReferenceById(studentId);
        boolean change = false;
        if (!student.getEmail().equals(modified.getEmail())) {
            if (studentRepository.selectExistsEmail(student.getEmail())) {
                throw new BadRequestException("this email has been used");
            }
            change = true;
            modified.setEmail(student.getEmail());
        }
        if (!student.getName().equals(modified.getName())) {
            change = true;
            modified.setName(student.getName());
        }
        if (!student.getGender().equals(modified.getGender())) {
            change = true;
            modified.setGender(student.getGender());
        }
        if (!change) throw new BadRequestException("there is no modification made");

        studentRepository.save(modified);
    }
}
