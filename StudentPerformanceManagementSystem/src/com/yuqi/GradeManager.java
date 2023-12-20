package com.yuqi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 成绩管理类
 *
 * @author yuqi
 * @version 1.0
 * date 2023/12/18
 */
public class GradeManager {
    private final Scanner sc = new Scanner(System.in);
    private final String GRADE_FILE_PATH = "StudentPerformanceManagementSystem/src/GradeInfo";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy年MM月dd日 hh时mm分ss秒");

    public void start() throws Exception {
        while (true) {
            System.out.println("============成绩信息管理============");
            System.out.println("1.更新学生成绩");
            // 查询成绩包括查询单科全部成绩，平均分，按最高分、最低分排列
            System.out.println("2.查询学生成绩");
            System.out.println("5.退出");
            System.out.println("===================================");
            Thread.sleep(300);
            System.out.println("请选择操作：");
            String command = sc.next();

            switch (command) {
                case "1" -> updateStudentGrade();
                case "2" -> queryStudentGrade();
                case "4" -> {
                    return;
                }
                default -> {
                    System.out.println("[指令无效，请重新输入]");
                    Thread.sleep(300);
                }
            }
        }
    }

    private void updateStudentGrade() throws Exception {
        System.out.println("============更新学生成绩============");
        Student student = new Student();
        Grade grade = new Grade();
        while (true) {
            System.out.print("请输入学生学号：");
            String studentId = sc.next();

            if (!studentId.matches("20(0[0-9]|1[0-9]|2[0-3])0{3}[0-6][1-9][01][1-9]\\d")) {
                System.out.println("[学号格式错误，请检查并重新输入正确的学号]");
                Thread.sleep(300);
            }

            List<Student> students = FileManager.getStudentById(studentId);
            if (students.isEmpty()) {
                System.out.println("未找到该学生。y/Y检查学号并重新输入，任意键返回");
                String reply = sc.next();
                if (reply.equals("y") || reply.equals("Y")) {
                    return;
                }
                continue;
            }
            student = students.get(1);

            grade.setStudentId(studentId);
            break;
        }

        while (true) {
            System.out.print("请输入你要录入/更新成绩的课程号:");
            String courseId = sc.next();

            if (!(courseId.matches("\\d+"))) {
                System.out.println("[课程号格式错误，请重试]");
                Thread.sleep(200);
                continue;
            }

            grade.setCourseId(courseId);
            break;
        }

        while (true) {
            System.out.print("请输入该生成绩");
            Double inputScore = sc.nextDouble();
            if (inputScore < 0 || inputScore > 100) {
                System.out.println("[成绩有误，请重试]");
                Thread.sleep(200);
                continue;
            }

            grade.setScore(inputScore);
            if (inputScore >= 90) {
                grade.setScoreLevel("优秀");
            } else if (inputScore >= 75) {
                grade.setScoreLevel("良好");
            } else if (inputScore >= 60) {
                System.out.println("及格");
            } else {
                System.out.println("不及格");
            }
            break;
        }

        LocalDateTime updatedTime = LocalDateTime.now();
        String result = formatter.format(updatedTime);
        student.setUpdatedTime(result);
        FileManager.updateStudentInfo(student);

        FileManager.addGradeIntoFile(grade);
        System.out.println("[更新成功！]");
        Thread.sleep(200);
    }

    private void queryStudentGrade() throws Exception {
        if (FileManager.isFileEmpty(GRADE_FILE_PATH)) {
            System.out.println("[当前系统尚未录入任何成绩信息]");
            Thread.sleep(300);
            return;
        }
        System.out.println("============查询学生成绩============");
        System.out.println("1.按科目查询");
        System.out.println("2.查询单个学生成绩");
        System.out.println("3.返回");
        System.out.println("====================================");
        Thread.sleep(300);
        System.out.println("请选择：");
        String command = sc.next();

        switch (command) {
            case "1" -> querySubjectGrades();
            case "2" -> queryGradesByStudent();
            case "3" -> {
                return;
            }
            default -> {
                System.out.println("[指令无效，请重新输入]");
                Thread.sleep(300);
            }
        }
    }

    private void querySubjectGrades() throws Exception {
        Course course = new Course();
        List<Grade> grades = new ArrayList<>();
        System.out.println("==============按科目查询==============");

        while (true) {
            System.out.print("请输入你要查询的课程的课程号：");
            String courseId = sc.next();

            if (!(courseId.matches("\\d+"))) {
                System.out.println("[课程号格式错误，请重试]");
                Thread.sleep(200);
                continue;
            }

            // 读取学生成绩
            List<Grade> g = FileManager.readGradesFromFile();
            // Stream流收集该课程号下学生成绩
            List<Grade> gradesForCourse = g.stream().filter(grade -> grade.getCourseId().equals(courseId))
                    .collect(Collectors.toList());

            if (gradesForCourse.isEmpty()) {
                System.out.println("该课程下没有学生的成绩信息。任意键返回...");
                sc.next();
                return;
            }

            grades = gradesForCourse;
            course = FileManager.getCourseById(courseId);
            break;
        }

        System.out.println("您想要查询科目 " + course.getName() + " 的：");
        // 写完之后才意识到既然可以从高到低排序那又有谁会展示毫无意义的全部学生成绩...anyWay写都写了
        System.out.println("1.成绩按从高到低排序");
        System.out.println("2.平均分");
        System.out.println("3.各成绩段学生");
        System.out.println("4.返回");
        System.out.println("====================================");
        Thread.sleep(300);

        while (true) {
            System.out.println("请输入指令：");
            String command = sc.next();
            switch (command) {
                case "1" -> selectStudentByGradeLevel(grades);
                case "2" -> averageScore(course.getCourseId(), grades);
                case "3" -> sortGrades(grades);
                case "4" -> {
                    return;
                }
                default -> {
                    System.out.println("[指令无效，请重新输入]");
                    Thread.sleep(300);
                }
            }
        }
    }

    private void averageScore(String courseId, List<Grade> grades) {
        double sum = 0;
        int count = 0;
        for (Grade grade : grades) {
            sum += grade.getScore();
            count++;
        }

        double averageScore = sum / count;
        Course course = FileManager.getCourseById(courseId);
        System.out.println("科目 " + course.getName() + " 的平均分为 " + averageScore + " 分。");
        System.out.println("任意键返回...");
        sc.next();
    }

    private void selectStudentByGradeLevel(List<Grade> grades) throws Exception {
        System.out.println("您需要查询哪个成绩段的学生？");
        System.out.println("1.优秀");
        System.out.println("2.良好");
        System.out.println("3.及格");
        System.out.println("4.不及格");

        Thread.sleep(300);

        while (true) {
            System.out.println("请输入指令：");
            String command = sc.next();
            switch (command) {
                case "1" -> queryGradesOfLevel(grades, "优秀");
                case "2" -> queryGradesOfLevel(grades, "良好");
                case "3" -> queryGradesOfLevel(grades, "及格");
                case "4" -> queryGradesOfLevel(grades, "不及格");
                default -> {
                    System.out.println("[指令无效，请重新输入]");
                    Thread.sleep(300);
                }
            }
        }
    }

    private void queryGradesOfLevel(List<Grade> grades, String level) {
        List<Grade> gradesInLevel = grades.stream().filter(grade -> grade.getScoreLevel().equals(level))
                .collect(Collectors.toList());

        if (gradesInLevel.isEmpty()) {
            System.out.println("没有学生在分段 " + level + "下。");
            System.out.println("任意键返回...");
            sc.next();
            return;
        }

        // 使用冒泡排序使学生成绩按从高到低顺序输出
        for (int i = 0; i < gradesInLevel.size() - 1; i++) {
            for (int j = 0; j < gradesInLevel.size() - 1 - i; j++) {
                Grade temp = gradesInLevel.get(j);
                // 使用集合的set方法交换位置
                gradesInLevel.set(j, gradesInLevel.get(j + 1));
                gradesInLevel.set(j + 1, temp);
            }
        }

        System.out.println("在分段 " + level + " 中的学生成绩如下-->");
        System.out.println("姓名    成绩");
        for (Grade grade : gradesInLevel) {
            Student student = FileManager.getStudentById(grade.getStudentId()).get(1);
            String name = student.getName();
            System.out.println(name + "\t" + grade.getScore());
        }
    }

    private void sortGrades(List<Grade> grades) {
        // 冒泡排序
        for (int i = 0; i < grades.size() - 1; i++) {
            for (int j = 0; j < grades.size() - 1 - i; j++) {
                Grade temp = grades.get(j);
                // 使用集合的set方法交换位置
                grades.set(j, grades.get(j + 1));
                grades.set(j + 1, temp);
            }
        }

        int count = 0;
        System.out.println("学生    成绩  成绩分段");
        for (Grade grade : grades) {
            Student student = FileManager.getStudentById(grade.getStudentId()).get(1);
            System.out.println(student.getName() + "\t" + grade.getScore() + "\t" + grade.getScoreLevel());
            count++;
        }

        System.out.println("展示完毕，共 " + count + " 名学生。任意键退出...");
        sc.next();
    }

    private void queryGradesByStudent() throws Exception {
        Student student = new Student();
        while (true) {
            System.out.print("请输入学生学号：");
            String studentId = sc.next();

            if (!studentId.matches("20(0[0-9]|1[0-9]|2[0-3])0{3}[0-6][1-9][01][1-9]\\d")) {
                System.out.println("[学号格式错误，请检查并重新输入正确的学号]");
                Thread.sleep(300);
            }

            List<Student> students = FileManager.getStudentById(studentId);
            student = students.get(1);
            break;
        }

        List<Grade> grades = FileManager.readGradesFromFile();
        String studentId = student.getStudentId();
        // Stream流收集该学号号下学生成绩
        List<Grade> gradesForStudentId = grades.stream().filter(grade -> grade.getStudentId().equals(studentId))
                .collect(Collectors.toList());

        if (gradesForStudentId.isEmpty()) {
            System.out.println("该学生尚未录入任何成绩。任意键返回...");
            sc.next();
            return;
        }

        String name = student.getName();
        System.out.println("学生 " + name + " 的成绩信息-->");
        System.out.println("课程    成绩    成绩分段    学分");
        for (Grade grade : gradesForStudentId) {
            Course course = FileManager.getCourseById(grade.getCourseId());
            System.out.println(course + "\t" + grade.getScore() + "\t"
                    + grade.getScoreLevel() + "\t" + course.getCredit());
        }
    }
}
