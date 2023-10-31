package com.ironhack.studentcatalogservice.controller;
import com.ironhack.studentcatalogservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CatalogController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/catalog/course/{courseCode}")
    @ResponseStatus(HttpStatus.OK)
    public Catalog getCatalog(@PathVariable String courseCode) {

        Course course = restTemplate.getForObject("http://grades-data-service/api/courses/" + courseCode, Course.class);
        Grade Grade = restTemplate.getForObject("http://grades-data-service/api/courses/" + courseCode + "/grades", Grade.class);

        Catalog catalog = new Catalog();
        catalog.setCourseName(course.getCourseName());
        List<StudentGrade> studentGradeList = new ArrayList<>();

        for (Grade grade : course.getGrades()) {
            Student student = restTemplate.getForObject("http://student-info-service/api/students/" + grade.getStudentId(), Student.class);
            studentGradeList.add(new StudentGrade(student.getName(), student.getAge(), grade.getGrade()));
        }

        catalog.setStudentGrades(studentGradeList);
        return catalog;
    }
}
