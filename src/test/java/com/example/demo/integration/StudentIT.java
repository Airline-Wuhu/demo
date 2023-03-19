package com.example.demo.integration;


import com.example.demo.student.Gender;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
public class StudentIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    private final Faker faker = new Faker();
    @Test
    void registerNewStudent() throws Exception {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        String name = String.format("%s %s", firstName, lastName);
        String email = String.format("%s@demail.com", firstName + lastName);

        Student s = new Student(
                name, email, Gender.OTHERS);
        ResultActions resultActions = mockMvc
                .perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(s)));
        resultActions.andExpect(status().isOk());
        List<Student> students = studentRepository.findAll();
        assertThat(students).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(s);
    }

    @Test
    void deleteStudent() throws Exception {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        String name = String.format("%s %s", firstName, lastName);
        String email = String.format("%s@demail.com", firstName + lastName);

        Student s = new Student(
                name, email, Gender.OTHERS);
        ResultActions resultActions = mockMvc
                .perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(s)));
        resultActions.andExpect(status().isOk());

        MvcResult getStudentResult = mockMvc.perform(get("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = getStudentResult.getResponse().getContentAsString();
        List<Student> students = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
        );

        long id = students.stream().filter(
                        x -> x.getEmail().equals(s.getEmail()))
                .map(Student::getId)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "student withemail: " + email + " not found!")
                );
        ResultActions resultActions1 = mockMvc.perform(delete("/api/v1/students/" + id));

        resultActions1.andExpect(status().isOk());
        boolean exist = studentRepository.existsById(id);
        assertThat(exist).isFalse();
    }
}
