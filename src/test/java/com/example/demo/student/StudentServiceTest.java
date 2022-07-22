package com.example.demo.student;
import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    StudentRepository studentRepository;

    StudentService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest=new StudentService(studentRepository);
    }

    @Test
    void CanGetAllStudents() {
       underTest.getAllStudents();
       verify(studentRepository).findAll();
    }

    @Test
    void CanAddStudent() {
        String email="kira@gmial.com";
        Student student=new Student(
          "kira",
          email,
          Gender.FEMALE
        );
        underTest.addStudent(student);
        ArgumentCaptor<Student> argumentCaptor=ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(argumentCaptor.capture());
        Student capturedValue = argumentCaptor.getValue();
        assertThat(capturedValue).isEqualTo(student);
    }
    @Test
    void ThrowWhenEmailIsTaken() {
        String email="kira@gmial.com";
        Student student=new Student(
                "kira",
                email,
                Gender.FEMALE
        );
        given(studentRepository.selectExistsEmail(student.getEmail())).willReturn(true);
        assertThatThrownBy(()->underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken")
        ;
        verify(studentRepository,never()).save(any());
    }
//if(!studentRepository.existsById(studentId)) {
//        throw new StudentNotFoundException(
//                "Student with id " + studentId + " does not exists");
//    }
//        studentRepository.deleteById(studentId);
    @Test

    void canDeleteStudent() {
        long studentId=1;
        given(studentRepository.existsById(studentId)).willReturn(true);
        underTest.deleteStudent(studentId);
        verify(studentRepository).deleteById(studentId);
    }
    @Test
    void throwWhenStudentNotFound(){
        long studentId=1;
        given(studentRepository.existsById(studentId)).willReturn(false);
        assertThatThrownBy(()->underTest.deleteStudent(studentId)).isInstanceOf(StudentNotFoundException.class).hasMessageContaining("Student with id " + studentId + " does not exists");
        verify(studentRepository,never()).deleteById(any());
    }

}
