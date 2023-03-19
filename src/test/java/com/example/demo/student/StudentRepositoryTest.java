package com.example.demo.student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;



    @Test
    void selectExistsEmail() {
        Student s = new Student("Jay", "jay@gmail.com", Gender.FEMALE);
        underTest.save(s);
        boolean exist = underTest.selectExistsEmail("jay@gmail.com");
        assertThat(exist).isTrue();
    }

    @Test
    void selectNotExistsEmail() {
        boolean notExist = underTest.selectExistsEmail("ja@gmail.com");
        assertThat(notExist).isFalse();
    }
}