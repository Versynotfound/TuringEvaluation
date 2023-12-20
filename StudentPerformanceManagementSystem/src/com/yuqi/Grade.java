package com.yuqi;

import java.util.ArrayList;
import java.util.List;

/**
 * 成绩类
 * 学号、课程号、成绩、成绩分段
 *
 * @author yuqi
 * @version 1.0
 * date 2023/12/18
 */
public class Grade {
    private String studentId;
    private String courseId;
    private double score;
    private String scoreLevel;

    private List<Student> students = new ArrayList<>();

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getScoreLevel() {
        return scoreLevel;
    }

    public void setScoreLevel(String scoreLevel) {
        this.scoreLevel = scoreLevel;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
