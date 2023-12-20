package com.yuqi;

import java.util.List;

/**
 * 课程类
 * 课程号、课程名、学分、学时
 *
 * @author yuqi
 * @version 2.0
 * date 2023/12/16
 */
public class Course {
    private String courseId;
    private String name;
    private double credit;
    private int studyPeriod;
    private List<Grade> grades;
    private List<Student> students;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public int getStudyPeriod() {
        return studyPeriod;
    }

    public void setStudyPeriod(int studyPeriod) {
        this.studyPeriod = studyPeriod;
    }
}