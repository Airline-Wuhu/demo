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

    @Test
    void updateStudentName() {
        long id = 1;
        String name = "testName";
        String email = "testName@email.com";
        Gender gender = Gender.OTHERS;

        Student s = new Student(id, name, email, gender);
        given(studentRepository.existsById(id)).willReturn(Boolean.TRUE);
        given(studentRepository.getReferenceById(id)).willReturn(new Student(id, name, email, gender));
        //given(studentRepository.selectExistsEmail(s.getEmail())).willReturn(Boolean.FALSE);
        s.setName("updatedName");
        underTest.updateStudent(id, s);
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student captureStudent = studentArgumentCaptor.getValue();
        assertThat(captureStudent).isEqualTo(s);
    }
    @Test
    void updateStudentEmail() {
        long id = 1;
        String name = "testName";
        String email = "testName@email.com";
        Gender gender = Gender.OTHERS;

        Student s = new Student(id, name, email, gender);
        given(studentRepository.existsById(id)).willReturn(Boolean.TRUE);
        given(studentRepository.getReferenceById(id)).willReturn(new Student(id, name, email, gender));
        s.setEmail("new@email.com");
        given(studentRepository.selectExistsEmail(s.getEmail())).willReturn(Boolean.FALSE);
        underTest.updateStudent(id, s);
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student captureStudent = studentArgumentCaptor.getValue();
        assertThat(captureStudent).isEqualTo(s);
    }
    @Test
    void updateStudentGender() {
        long id = 1;
        String name = "testName";
        String email = "testName@email.com";
        Gender gender = Gender.OTHERS;

        Student s = new Student(id, name, email, gender);
        given(studentRepository.existsById(id)).willReturn(Boolean.TRUE);
        given(studentRepository.getReferenceById(id)).willReturn(new Student(id, name, email, gender));
        s.setGender(Gender.MALE);
        underTest.updateStudent(id, s);
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student captureStudent = studentArgumentCaptor.getValue();
        assertThat(captureStudent).isEqualTo(s);
    }
    @Test
    void updateStudentAll() {
        long id = 1;
        String name = "testName";
        String email = "testName@email.com";
        Gender gender = Gender.OTHERS;

        Student s = new Student(id, name, email, gender);
        given(studentRepository.existsById(id)).willReturn(Boolean.TRUE);
        given(studentRepository.getReferenceById(id)).willReturn(new Student(id, name, email, gender));
        s.setGender(Gender.MALE);
        s.setEmail("new@email.com");
        s.setName("newName");
        given(studentRepository.selectExistsEmail(s.getEmail())).willReturn(Boolean.FALSE);

        underTest.updateStudent(id, s);
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student captureStudent = studentArgumentCaptor.getValue();
        assertThat(captureStudent).isEqualTo(s);
        //System.out.println(captureStudent.getEmail());
    }

    @Test
    void canVerifyFalseID() {
        long id = 1;
        String name = "testName";
        String email = "testName@email.com";
        Gender gender = Gender.OTHERS;

        Student s = new Student(id, name, email, gender);
        given(studentRepository.existsById(id)).willReturn(Boolean.FALSE);
        assertThatThrownBy(() -> underTest.updateStudent(id, s))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Student with id " + id + " does not exist");
        verify(studentRepository, never()).save(any());
    }
    @Test
    void canVerifyExistEmail() {
        long id = 1;
        String name = "testName";
        String email = "testName@email.com";
        Gender gender = Gender.OTHERS;

        Student s = new Student(id, name, email, gender);
        given(studentRepository.existsById(id)).willReturn(Boolean.TRUE);
        given(studentRepository.getReferenceById(id)).willReturn(new Student(id, name, "email@email.com", gender));
        given(studentRepository.selectExistsEmail(email)).willReturn(Boolean.TRUE);
        assertThatThrownBy(() -> underTest.updateStudent(id, s))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("this email has been used");
        verify(studentRepository, never()).save(any());
    }
    @Test
    void canVerifyNoneModification() {
        long id = 1;
        String name = "testName";
        String email = "testName@email.com";
        Gender gender = Gender.OTHERS;

        Student s = new Student(id, name, email, gender);
        given(studentRepository.existsById(id)).willReturn(Boolean.TRUE);
        given(studentRepository.getReferenceById(id)).willReturn(s);
        assertThatThrownBy(() -> underTest.updateStudent(id, s))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("there is no modification made");
        verify(studentRepository, never()).save(any());
    }
}