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
    private String credit;
    private String studyPeriod;
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

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getStudyPeriod() {
        return studyPeriod;
    }

    public void setStudyPeriod(String studyPeriod) {
        this.studyPeriod = studyPeriod;
    }
}