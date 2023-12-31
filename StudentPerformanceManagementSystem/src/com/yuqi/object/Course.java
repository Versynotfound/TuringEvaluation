package com.yuqi.object;

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

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", name='" + name + '\'' +
                ", credit='" + credit + '\'' +
                ", studyPeriod='" + studyPeriod + '\'' +
                '}';
    }

    public Course() {
    }

    public Course(String courseId, String name, String credit, String studyPeriod) {
        this.courseId = courseId;
        this.name = name;
        this.credit = credit;
        this.studyPeriod = studyPeriod;
    }

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