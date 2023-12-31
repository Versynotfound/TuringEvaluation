package com.yuqi.object;

/**
 * 学生类
 * 姓名、学号、性别、班级、邮箱、信息建立时间、最后修改时间
 *
 * @author yuqi
 * @version 2.0
 * date 2023/12/16
 */
public class Student {
    private String name;
    private String studentId;
    private String gender;
    private String className;
    private String classCode;
    private String major;
    private String email;
    private String startYear;
    private String createdTime;
    private String updatedTime = "尚未被修改";

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", studentId='" + studentId + '\'' +
                ", gender='" + gender + '\'' +
                ", className='" + className + '\'' +
                ", classCode='" + classCode + '\'' +
                ", major='" + major + '\'' +
                ", email='" + email + '\'' +
                ", startYear='" + startYear + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", updatedTime='" + updatedTime + '\'' +
                '}';
    }

    public Student() {
    }

    public Student(String name, String studentId, String gender, String className, String classCode,
                   String major, String email, String startYear, String createdTime) {
        this.name = name;
        this.studentId = studentId;
        this.gender = gender;
        this.className = className;
        this.classCode = classCode;
        this.major = major;
        this.email = email;
        this.startYear = startYear;
        this.createdTime = createdTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }
}