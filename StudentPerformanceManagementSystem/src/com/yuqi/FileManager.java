package com.yuqi;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 文件管理类
 *
 * @author yuqi
 * @version 1.0
 * date 2023/12/19
 */
public class FileManager {
    private static final String STUDENT_FILE_PATH = "StudentPerformanceManagementSystem/src/StudentInfo";
    private static final String COURSE_FILE_PATH = "StudentPerformanceManagementSystem/src/CourseInfo";
    private static final String GRADE_FILE_PATH = "StudentPerformanceManagementSystem/src/GradeInfo";
    private static final String MAJOR_CODES_FILE_PATH = "StudentPerformanceManagementSystem/src/MajorCodes";
    private static final Scanner SC = new Scanner(System.in);

    public static boolean isFileEmpty(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            return br.readLine() == null;
        } catch (IOException e) {
            e.printStackTrace();
            // 如果发生异常直接返回true
            return true;
        }
    }

    public static boolean isStudentFileEmpty(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE_PATH))){
            return br.readLine() == null;
        } catch (IOException e) {
            e.printStackTrace();
            // 如果发生异常直接返回true
            return true;
        }
    }



    public static Student getMajorInfo(Student student, String majorCode) {
        // 使用的是repository path不知道在你们的电脑上能不能运行
        try (FileReader fr = new FileReader(MAJOR_CODES_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                // 读取一行 把该行两个信息用四个空格分隔 放入一个字符数组
                String[] majorInfo = line.split(" {4}");

                // 一旦字符数组里元素达到两个（即读取了一个专业信息对），便比对专业码是否等于索引1处字符（也就是文件里的专业码），
                // 随后开始下一轮循环直到匹配到合适专业码并获取专业名称
                if (majorInfo.length == 2 && majorInfo[1].equals(majorCode)) {
                    String major = majorInfo[0];
                    student.setMajor(major);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return student;
    }

    public static void addStudentIntoFile(@NotNull Student student) {
        student.setUpdatedTime("尚未被修改");

        try (FileWriter fr = new FileWriter(STUDENT_FILE_PATH, true);
             PrintWriter pw = new PrintWriter(fr);) {
            pw.println(student.getStudentId() + "," + student.getName() + "," + student.getGender() +
                    "," + student.getMajor() + "," + student.getClassName() + "," + student.getClassCode() +
                    "," + student.getEmail() + "," + student.getCreatedTime() + "," + student.getUpdatedTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAllStudent() {
        int count = 0;
        // 按学生录入时间前后展示全部学生信息
        System.out.println("---------------------------------------全部学生-----------------------------------------------");
        System.out.println("学号            姓名   性别      专业      班级  班级序号    邮箱" +
                "              信息建立时间              信息修改时间");
        try (FileReader fr = new FileReader(STUDENT_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while (((line = br.readLine()) != null)) {
                String[] studentInfo = line.split(",");

                if (studentInfo.length == 9) {
                    System.out.println(studentInfo[0] + "\t" + studentInfo[1] + "\t" + studentInfo[2] +
                            "\t" + studentInfo[3] + "\t\t" + studentInfo[4] + "\t" + studentInfo[5] +
                            "\t\t" + studentInfo[6] + "\t\t" + studentInfo[7] + "\t" + studentInfo[8]);
                    count++;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("展示完毕，当前总共录入" + count + "名学生");
        System.out.println("按任意键退出...");
        SC.next();
    }

    public static void updateStudentInfo(Student updatedStudent) {
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_FILE_PATH))) {

            String line;
            List<String> lines = new ArrayList<>();

            // 读取文件内容
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String studentId = parts[0];

                // 判断是否是要更新的学生信息id
                if (studentId.equals(updatedStudent.getStudentId())) {
                    // 更新学生信息
                    line = updatedStudent.getStudentId() + "," +
                            updatedStudent.getName() + "," +
                            updatedStudent.getGender() + "," +
                            updatedStudent.getClassName() + "," +
                            updatedStudent.getEmail() + "," +
                            updatedStudent.getCreatedTime() + "," +
                            updatedStudent.getUpdatedTime();
                }

                lines.add(line);

                // 调用方法清空原文件内容并把新内容写入
                updateFile(lines);
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static void removeStudentFromFile(@NotNull Student studentToDelete) {

        List<String> lines = new ArrayList<>();

        // 读取文件内容到列表
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 寻找并删除要删除的学生
        for (int i = 0; i < lines.size(); i++) {
            String[] studentInfo = lines.get(i).split(",");
            if (studentInfo.length >= 1 && studentInfo[0].equals(studentToDelete.getStudentId())) {
                lines.remove(i);
                break;// 找到后删除，跳出循环
            }
        }

        // 调用方法清空原文件内容并把新内容写入
        updateFile(lines);
    }

    public static void updateFile(List<String> lines) {
        // 参数改为false，清空原文件内容
        try (BufferedWriter clearWriter = new BufferedWriter(new FileWriter(STUDENT_FILE_PATH, false))) {
            clearWriter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 将新内容写入
        try (BufferedWriter newWriter = new BufferedWriter(new FileWriter(STUDENT_FILE_PATH, false))) {
            for (String updatedLine : lines) {
                newWriter.write(updatedLine);
                newWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addCourseIntoFile(@NotNull Course course) {
        try (FileWriter fr = new FileWriter(COURSE_FILE_PATH, true);
             PrintWriter pw = new PrintWriter(fr);) {
            pw.println(course.getCourseId() + "," + course.getName() + "," + course.getCredit() +
                    "," + course.getStudyPeriod());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}