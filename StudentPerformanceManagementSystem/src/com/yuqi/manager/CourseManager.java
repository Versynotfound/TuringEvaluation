package com.yuqi.manager;

import com.yuqi.object.Course;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 课程管理类
 *
 * @author yuqi
 * @version 2.0
 * date 2023/12/18
 */
public class CourseManager {
    private final String COURSE_FILE_PATH = "StudentPerformanceManagementSystem/src/CourseInfo";
    private final Scanner sc = new Scanner(System.in);
    private final Pattern courseNamePattern = Pattern.compile("[A-Za-z0-9]");

    public void start() throws Exception {
        while (true) {
            System.out.println("============课程信息管理============");
            System.out.println("1.添加课程");
            System.out.println("2.查询课程信息");
            System.out.println("3.修改课程信息");
            System.out.println("4.删除课程");
            System.out.println("5.退出");
            System.out.println("===================================");
            Thread.sleep(300);
            
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
        System.out.println("==============添加课程==============");
        String courseId;
        String name;
        String credit;
        String studyPeriod;

        // 考虑到课程基本是一次性添加多个，所以套两个循环
        while (true) {
            while (true) {
                System.out.print("请输入课程号：");
                String inputId = sc.next();

                if (!(isCourseIdValid(inputId))) {
                    continue;
                }

                if (FileManager.isFileEmpty(COURSE_FILE_PATH)) {
                    courseId = inputId;
                    break;
                }

                Course course = FileManager.getCourseById(inputId);
                if (course != null) {
                    System.out.println("该课程号已存在，请勿重复录入。任意键返回");
                    sc.next();
                    return;
                }
                courseId = inputId;
                break;
            }

            while (true) {
                System.out.print("请输入课程名称：");
                String inputName = sc.next();
                Matcher courseNameMatcher = courseNamePattern.matcher(inputName);
                // 一旦用户输入出现数字或字母则判定格式错误
                if (courseNameMatcher.find()) {
                    System.out.println("[课程名称格式错误，请重试]");
                    Thread.sleep(200);
                    continue;
                }
                name = inputName;
                break;
            }

            while (true) {
                System.out.print("请录入该课学分：");

                // hasNextDouble方法检验用户输入是否是小数
                if (sc.hasNextDouble()) {
                    double inputCredit = sc.nextDouble();

                    // 假设学分最低为0分，最高为5分
                    if (inputCredit > 0 && inputCredit <= 5) {
                        credit = String.valueOf(inputCredit);
                        break;
                    }

                    // 输入如果是整数，强制转换成Double类型
                } else if (sc.hasNextInt()) {
                    int inputCredit = sc.nextInt();

                    if ((double) inputCredit > 0 && (double) inputCredit <= 5) {
                        credit = String.valueOf(inputCredit);
                        break;
                    }
                } else {
                    System.out.println("[格式错误，请重试]");
                    Thread.sleep(200);
                }
            }

            while (true) {
                System.out.print("请录入该课学时：");
                String inputPeriod = sc.next();

                if (!(isStudyPeriodValid(inputPeriod))) {
                    continue;
                }

                studyPeriod = inputPeriod;

                Course course = new Course(name, courseId, credit, studyPeriod);

                FileManager.addCourseIntoFile(course);
                System.out.println("课程信息录入成功！y/Y继续录入，任意键返回");
                String reply = sc.next();

                if (!(reply.equals("y") || reply.equals("Y"))) {
                    return;
                }
                break;
            }
        }
    }

    private void queryCourse() throws Exception {
        if (FileManager.isFileEmpty(COURSE_FILE_PATH)) {
            System.out.println("[当前系统尚未录入任何课程信息]");
            Thread.sleep(300);
            return;
        }

        while (true) {
            System.out.println("============查询课程信息============");
            System.out.println("1.通过课程号查询");
            System.out.println("2.通过课程名查询");
            System.out.println("3.展示所有课程信息");
            System.out.println("4.返回");
            System.out.println("===================================");
            Thread.sleep(300);

            System.out.println("请选择指令：");
            String command = sc.next();

            switch (command) {
                case "1" -> queryCourseById();
                case "2" -> queryCourseByName();
                case "3" -> FileManager.showAllCourses();
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

    private void queryCourseById() throws Exception {
        while (true) {
            System.out.print("请输入详细课程号：");
            String inputId = sc.next();

            if (!(isCourseIdValid(inputId))) {
                continue;
            }

            Course course = FileManager.getCourseById(inputId);
            if (inputId.equals(course.getCourseId())) {
                // 调用方法打印找到的课程信息
                System.out.println("课程号    课程名    学分 学时");
                printCourseInfo(course);
                System.out.println("任意键返回...");
                sc.next();
                return;
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

            // 支持模糊查询
            List<Course> courses = FileManager.getCourseByName(targetName);
            if (!(courses.isEmpty())) {
                System.out.println("找到以下符合条件的结果：");
                System.out.println("课程号    课程名    学分 学时");
                for (Course c : courses) {
                    printCourseInfo(c);
                }

                System.out.println("任意键返回...");
                sc.next();
                return;
            }
        }
    }

    private void printCourseInfo(@NotNull Course course) {
        System.out.println(course.getCourseId() + "\t" + course.getName() + "\t"
                + course.getCredit() + "\t" + course.getStudyPeriod());
    }

    private void updateCourseInfo() throws Exception {
        if (FileManager.isFileEmpty(COURSE_FILE_PATH)) {
            System.out.println("[当前系统尚未录入任何课程信息]");
            Thread.sleep(300);
            return;
        }

        System.out.println("=============修改课程信息=============");
        while (true) {
            System.out.print("请输入您要修改的课程的课程号：");
            String inputId = sc.next();

            if (!(isCourseIdValid(inputId))) {
                continue;
            }

            // 如果未从文件里找到该内容：
            if (!(FileManager.isFind(inputId, COURSE_FILE_PATH))) {
                System.out.println("课程不存在，请检查并重新输入。y/Y重新输入，任意键退出");
                String reply = sc.next();
                if (reply.equals("y") || reply.equals(("Y"))) {
                    continue;
                }
                return;
            }

            Course course = FileManager.getCourseById(inputId);

            System.out.println("您要修改课程" + course.getName() + "的：");
            System.out.println("1.学分");
            System.out.println("2.学时");
            Thread.sleep(200);
            System.out.println("请选择：");

            while (true) {
                String command = sc.next();
                switch (command) {
                    case "1" -> {
                        updateCourseCredit(course);
                        return;
                    }
                    case "2" -> {
                        updateCoursePeriod(course);
                        return;
                    }
                    default -> {
                        System.out.println("[指令错误，请重试]");
                        Thread.sleep(200);
                    }
                }
            }
        }
    }

    private synchronized void updateCourseCredit(Course course) throws Exception {
        System.out.println("该课程原学分为" + course.getCredit());
        while (true) {
            System.out.print("您要将其修改成：");

            if (sc.hasNextDouble()) {
                double inputCredit = sc.nextDouble();

                if (inputCredit > 0 && inputCredit <= 5) {
                    course.setCredit(String.valueOf(inputCredit));
                    break;
                }

            } else if (sc.hasNextInt()) {
                int inputCredit = sc.nextInt();

                if ((double) inputCredit > 0 && (double) inputCredit <= 5) {
                    course.setCredit(String.valueOf(inputCredit));
                    break;
                }
            }
            System.out.println("[格式错误，请重试]");
            Thread.sleep(200);
        }

        FileManager.updateCourseInfo(course);

        System.out.println("[修改成功]");
        Thread.sleep(200);
    }

    private synchronized void updateCoursePeriod(Course course) throws Exception {
        System.out.println("该课程原学时为" + course.getCredit());
        while (true) {
            System.out.print("您要将其修改成：");
            String inputPeriod = sc.next();

            if (!(isStudyPeriodValid(inputPeriod))) {
                continue;
            }

            course.setStudyPeriod(inputPeriod);

            FileManager.updateCourseInfo(course);
            System.out.println("[修改成功]");
            Thread.sleep(200);
            break;
        }
    }

    private synchronized void deleteCourse() throws Exception {
        if (FileManager.isFileEmpty(COURSE_FILE_PATH)) {
            System.out.println("[当前系统尚未录入任何课程信息]");
            Thread.sleep(300);
            return;
        }

        System.out.println("===============删除课程===============");
        while (true) {
            System.out.print("请输入您要删除的课程的课程号：");
            String inputId = sc.next();

            Course course = FileManager.getCourseById(inputId);
            System.out.println("确认删除课程 " + course.getName() + " 吗？y/Y确认，任意键取消");
            System.out.println("课程关联到成绩和学生，所以这里不会写了。按任意键返回");
            sc.next();
            return;
        }
    }

    public static boolean isCourseIdValid(@NotNull String inputCourseId) throws Exception {
        if (!(inputCourseId.matches("\\d+"))) {
            System.out.println("[课程号格式错误，请重试]");
            Thread.sleep(200);
            return false;
        }
        return true;
    }

    public static boolean isStudyPeriodValid(@NotNull String inputPeriod) throws Exception {
        // 假设学时是大于0小于40的整数
        if (!(inputPeriod.matches("[1-9]|[1-3][0-9]"))) {
            System.out.println("[格式错误，请重试]");
            Thread.sleep(200);
            return false;
        }
        return true;
    }
}

