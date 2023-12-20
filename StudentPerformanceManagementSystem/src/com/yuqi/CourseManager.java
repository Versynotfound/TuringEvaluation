package com.yuqi;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 课程管理类
 *
 * @author yuqi
 * @version 1.0
 * date 2023/12/18
 */
public class CourseManager {
    private final List<Course> courses = new ArrayList<>();
    private final String COURSE_FILE_PATH = "StudentPerformanceManagementSystem/src/CourseInfo";
    private final Scanner sc = new Scanner(System.in);
    private final Pattern courseNamePattern = Pattern.compile("[A-Za-z0-9]");

    public void start() throws Exception {
        System.out.println("============课程信息管理============");
        System.out.println("1.添加课程");
        System.out.println("2.查询课程信息");
        System.out.println("3.修改课程信息");
        System.out.println("4.删除课程");
        System.out.println("5.退出");
        System.out.println("===================================");
        Thread.sleep(300);

        while (true) {
            System.out.println("请选择操作：");
            String command = sc.next();

            switch (command) {
                case "1" -> addCourse();
                case "2" -> queryCourse();
                case "3" -> updateCourseInfo();
                case "4" -> deleteCourse();
                case "5" -> {
                    return;
                }
                default -> {
                    System.out.println("[指令无效，请重新输入]");
                    Thread.sleep(300);
                }
            }
        }
    }

    private void addCourse() throws Exception {
        Course course = new Course();
        System.out.println("==============添加课程==============");
        // 考虑到课程基本是一次性添加多个，所以套两个循环
        // 这里的嵌套有点恐怖了
        while (true) {
            while (true) {
                System.out.print("请输入课程号：");
                String inputId = sc.next();
                // 只检验课程号是否是数字，因为不清楚标准
                if (!(inputId.matches("\\d+"))) {
                    System.out.println("[课程号格式错误，请重试]");
                    Thread.sleep(200);
                    continue;
                }

                if (courses.isEmpty()){
                    course.setCourseId(inputId);
                    break;
                }

                for (Course c : courses) {
                    if (inputId.equals(c.getCourseId())){
                        System.out.println("该课程号已存在，请勿重复录入。任意键返回");
                        sc.next();
                        return;
                    }
                }

                course.setCourseId(inputId);
                break;
            }

            while (true) {
                System.out.print("请输入课程名称：");
                String inputCourseName = sc.next();
                Matcher courseNameMatcher = courseNamePattern.matcher(inputCourseName);
                // 一旦用户输入出现数字或字母则判定格式错误
                if (courseNameMatcher.find()) {
                    System.out.println("[课程名称格式错误，请重试]");
                    Thread.sleep(200);
                    continue;
                }
                course.setName(inputCourseName);
                break;
            }

            while (true) {
                System.out.print("请录入该课学分：");

                // hasNextDouble方法检验用户输入是否是小数
                if (sc.hasNextDouble()) {
                    double inputCredit = sc.nextDouble();

                    // 假设学分最低为0分，最高为5分
                    if (inputCredit > 0 && inputCredit <= 5) {
                        course.setCredit(inputCredit);
                        break;
                    }

                    // 输入如果是整数，强制转换成Double类型
                } else if (sc.hasNextInt()) {
                    int inputCredit = sc.nextInt();

                    if ((double) inputCredit > 0 && (double) inputCredit <= 5) {
                        course.setCredit(inputCredit);
                        break;
                    }
                }
                System.out.println("[格式错误，请重试]");
                Thread.sleep(200);
            }

            while (true) {
                System.out.print("请录入该课学时：");
                String inputPeriod = sc.next();

                // 假设学时是大于0小于40的整数
                if (!(inputPeriod.matches("[1-9]|[1-3][0-9]"))) {
                    System.out.println("[格式错误，请重试]");
                    Thread.sleep(200);
                    continue;
                }

                // 将字符串类型转为整形
                int correctPeriod = Integer.parseInt(inputPeriod);
                course.setStudyPeriod(correctPeriod);

                courses.add(course);

                // 将课程信息写入txt文件
                FileManager.addCourseIntoFile(course);
                System.out.println("课程信息录入成功！y/Y继续录入，任意键返回");
                String reply = sc.next();

                // 如果用户输入不为y或Y则直接返回
                if (!(reply.equals("y") || reply.equals("Y"))) {
                    return;
                }
                break;
            }
        }
    }

    private void queryCourse() throws Exception {
        if (courses.isEmpty()) {
            System.out.println("[当前系统尚未录入任何课程信息]");
            Thread.sleep(300);
            return;
        }

        System.out.println("============查询课程信息============");
        System.out.println("1.通过课程号查询");
        System.out.println("2.通过课程名查询");
        System.out.println("3.返回");
        System.out.println("===================================");
        Thread.sleep(300);

        while (true) {
            System.out.println("请选择指令：");
            String command = sc.next();

            switch (command) {
                case "1" -> queryCourseById();
                case "2" -> queryCourseByName();
                case "3" -> {
                    return;
                }
                default -> {
                    System.out.println("[指令无效，请重新输入]");
                    Thread.sleep(300);
                }
            }
        }
    }

    private void queryCourseById() throws Exception {
        while (true) {
            System.out.print("请输入详细课程号：");
            String inputId = sc.next();

            if (!(inputId.matches("\\d+"))) {
                System.out.println("[课程号格式错误，请重试]");
                Thread.sleep(200);
                continue;
            }

            for (Course course : courses) {
                if (inputId.equals(course.getCourseId())){
                    // 调用方法打印找到的课程信息
                    printCourseInfo(course);
                    System.out.println("任意键返回...");
                    sc.next();
                    return;
                }
            }
        }
    }

    private void queryCourseByName() throws Exception {
        while (true) {
            System.out.print("请输入课程名：");
            String targetName = sc.next();

            Matcher nameMatcher = courseNamePattern.matcher(targetName);

            if (!(nameMatcher.find())) {
                System.out.println("[课程名格式错误，请重试]");
                Thread.sleep(200);
                continue;
            }

            for (Course course : courses) {
                if (targetName.equals(course.getName())){
                    printCourseInfo(course);
                    System.out.println("任意键返回...");
                    sc.next();
                    return;
                }
            }
        }
    }

    private void printCourseInfo(@NotNull Course course) {
        System.out.println("课程号    课程名    学分 学时");
        System.out.println(course.getCourseId() + "\t" + course.getName() + "\t"
                + course.getCredit() +"\t" + course.getStudyPeriod());
    }

    private void updateCourseInfo() throws Exception {
        if (courses.isEmpty()) {
            System.out.println("[当前系统尚未录入任何学生信息，请先录入再进行操作]");
            Thread.sleep(300);
            return;
        }

        System.out.println("==============修改信息==============");
        System.out.println("1.修改学生个人信息");
        System.out.println("2.录入/修改课程信息");
        System.out.println("3.录入/修改学生成绩信息");
        System.out.println("4.返回");
        System.out.println("===================================");
        Thread.sleep(300);
        System.out.println("请选择操作：");

        while (true) {
            String command = sc.next();
            switch (command) {
                case "1" -> {
                    updateStudentInfo();
                    // 修改完成则直接返回到学生信息管理主界面
                    return;
                }
                case "2" -> {
                    updateCourseInfo();
                    return;
                }
                case "3" -> {
                    updateGradeInfo();
                    return;
                }
                case "4" -> {
                    return;
                }
                default -> {
                    System.out.println("[指令不存在，请重试]");
                    Thread.sleep(200);
                }
            }
        }
    }

    private void deleteCourse() {
    }
}

