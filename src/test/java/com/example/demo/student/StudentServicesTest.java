package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class StudentServicesTest {

    @Mock private StudentRepository studentRepository;
    private StudentServices underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentServices(studentRepository);
    }

    @Test
    void getAllStudents() {
        underTest.getAllStudents();
        verify(studentRepository).findAll();

    }

    @Test
    void addStudent() {
        Student s = new Student("bc", "a.sad@gmail.com", Gender.FEMALE);
        underTest.addStudent(s);
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student captureStudent = studentArgumentCaptor.getValue();
        assertThat(captureStudent).isEqualTo(s);
    }
    @Test
    void addStudentTaken() {
        Student s = new Student("bc", "a.sad@gmail.com", Gender.FEMALE);
        given(studentRepository.selectExistsEmail(s.getEmail())).willReturn(true);
        assertThatThrownBy(() -> underTest.addStudent(s))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + "a.sad@gmail.com" + " taken");
        verify(studentRepository, never()).save(any());
    }



    @Test
    void deleteStudentDoesNotExist() {
        List<Student> students = underTest.getAllStudents();
        long id = students.size() + 1; // this id should not be available
        assertThatThrownBy(() -> underTest.deleteStudent(id))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + id + " does not exist");
        verify(studentRepository, never()).deleteById(any());
    }
}