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
 * @version 2.0
 * date 2023/12/19
 */
public class FileManager {
    private static final String STUDENT_FILE_PATH = "StudentPerformanceManagementSystem/src/StudentInfo";
    private static final String ADMINISTRATORS_FILE_PATH = "StudentPerformanceManagementSystem/src/Administrator";
    private static final String COURSE_FILE_PATH = "StudentPerformanceManagementSystem/src/CourseInfo";
    private static final String GRADE_FILE_PATH = "StudentPerformanceManagementSystem/src/GradeInfo";
    private static final String MAJOR_CODES_FILE_PATH = "StudentPerformanceManagementSystem/src/MajorCodes";
    private static final Scanner SC = new Scanner(System.in);

    public static boolean isFileEmpty(String filePath) {
        // 由调用方传入路径
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.readLine() == null;
        } catch (IOException e) {
            e.printStackTrace();
            // 如果发生异常直接返回true
            return true;
        }
    }

    public static boolean isFind(String targetId, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0];

                if (id.equals(targetId)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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

    public static List<Student> getStudentById(String id) {
        // 模糊查询版
        Student stu = new Student();
        List<Student> students = new ArrayList<>();

        try (FileReader fr = new FileReader(STUDENT_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] stuInfo = line.split(",");

                // 如果学号包含输入的id则判定为要找的学生
                if (stuInfo.length == 7 && stuInfo[0].contains(id)) {
                    stu.setStudentId(stuInfo[0]);
                    stu.setName(stuInfo[1]);
                    stu.setGender(stuInfo[2]);
                    stu.setMajor(stuInfo[3]);
                    stu.setClassName(stuInfo[4]);
                    stu.setClassCode(stuInfo[5]);
                    stu.setCreatedTime(stuInfo[6]);
                    stu.setUpdatedTime(stuInfo[7]);
                    students.add(stu);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public static @NotNull Administrator getAdministratorById(String id) {
        Administrator admin = new Administrator();
        try (FileReader fr = new FileReader(ADMINISTRATORS_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] adminInfo = line.split(",");

                // debug!!!
                for (String s : adminInfo) {
                    System.out.println(s);
                }

                if (adminInfo.length == 5 && adminInfo[1].equals(id)) {
                    admin.setAdministratorId(adminInfo[0]);
                    admin.setName(adminInfo[1]);
                    admin.setPassword(adminInfo[2]);
                    admin.setLocked(adminInfo[3]);
                    admin.setCreateTime(adminInfo[4]);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admin;
    }

    public static void addAdministratorIntoFile(Administrator admin) {
        try (FileWriter fr = new FileWriter(ADMINISTRATORS_FILE_PATH, true);
             PrintWriter pw = new PrintWriter(fr)) {
            pw.println(admin.getAdministratorId() + "," + admin.getName() + "," + admin.getPassword() +
                    "," + admin.getLocked() + "," + admin.getCreateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addStudentIntoFile(@NotNull Student student) {
        student.setUpdatedTime("尚未被修改");

        try (FileWriter fr = new FileWriter(STUDENT_FILE_PATH, true);
             PrintWriter pw = new PrintWriter(fr)) {
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

    public static void showAllCourses() {
        int count = 0;
        System.out.println("-------------------课程--------------------");
        System.out.println("课程号    课程名    学分  学时");
        try (FileReader fr = new FileReader(COURSE_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while (((line = br.readLine()) != null)) {
                String[] courseInfo = line.split(",");

                if (courseInfo.length == 4) {
                    System.out.println(courseInfo[0] + "\t" + courseInfo[1] + "\t" + courseInfo[2] +
                            "\t" + courseInfo[3]);
                    count++;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("展示完毕，当前总共录入" + count + "门课程");
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
                updateFile(lines, STUDENT_FILE_PATH);
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
        updateFile(lines, STUDENT_FILE_PATH);
    }

    public static void updateFile(List<String> lines, String filePath) {
        // 参数改为false，清空原文件内容
        try (BufferedWriter clearWriter = new BufferedWriter(new FileWriter(filePath, false))) {
            clearWriter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 将新内容写入
        try (BufferedWriter newWriter = new BufferedWriter(new FileWriter(filePath, false))) {
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

    public static void setLocked(String adminId, Administrator admin) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMINISTRATORS_FILE_PATH))) {
            String line;
            List<String> lines = new ArrayList<>();

            // 读取文件内容
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0];

                // 判断是否是要上锁的管理员id
                if (id.equals(adminId)) {
                    // 如果是，上锁
                    line = admin.getAdministratorId() + "," + admin.getName() + "," +
                            admin.getPassword() + "," +
                            true + "," +
                            admin.getCreateTime();
                }

                lines.add(line);

                // 调用方法清空原文件内容并把新内容写入
                updateFile(lines, ADMINISTRATORS_FILE_PATH);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateAdministratorFile(@NotNull Administrator admin) {
        String adminId = admin.getAdministratorId();
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMINISTRATORS_FILE_PATH))) {
            String line;
            List<String> lines = new ArrayList<>();

            // 读取文件内容
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0];

                if (id.equals(adminId)) {
                    line = admin.getAdministratorId() + "," + admin.getName() + "," +
                            admin.getPassword() + "," +
                            admin.getLocked() + "," +
                            admin.getCreateTime();
                }

                lines.add(line);

                updateFile(lines, ADMINISTRATORS_FILE_PATH);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeAdminFromFile(@NotNull Administrator adminToDelete) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ADMINISTRATORS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 寻找并删除要删除的管理员
        for (int i = 0; i < lines.size(); i++) {
            String[] adminInfo = lines.get(i).split(",");
            if (adminInfo.length >= 1 && adminInfo[0].equals(adminToDelete.getAdministratorId())) {
                lines.remove(i);
                break;// 找到后删除，跳出循环
            }
        }

        // 调用方法清空原文件内容并把新内容写入
        updateFile(lines, ADMINISTRATORS_FILE_PATH);
    }

    public static boolean isLocked(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMINISTRATORS_FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // 找到该id代表管理员所在行 并检查是否上锁 如果上则返回真
                if (parts.length >= 1 && parts[0].equals(id) && parts.length >= 3 && parts[3].equals("true")) {
                    return true;
                }
            }
            // 若不为真则为假
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void removeGradesFromFile(String studentId) {
        List<String> lines = new ArrayList<>();

        // 读取文件内容到列表
        try (BufferedReader reader = new BufferedReader(new FileReader(GRADE_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 寻找并删除要删除的学生
        for (int i = 0; i < lines.size(); i++) {
            String[] gradeInfo = lines.get(i).split(",");
            if (gradeInfo.length == 4 && gradeInfo[0].equals(studentId)) {
                lines.remove(i);
                break;// 找到后删除，跳出循环
            }
        }

        // 调用方法清空原文件内容并把新内容写入
        updateFile(lines, GRADE_FILE_PATH);

    }

    public static List<Student> getStudentByName(String targetName) {
        // 模糊查询版
        Student stu = new Student();
        List<Student> students = new ArrayList<>();

        try (FileReader fr = new FileReader(STUDENT_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] stuInfo = line.split(",");

                // 如果学号包含输入的id则判定为要找的学生
                if (stuInfo.length == 7 && stuInfo[1].contains(targetName)) {
                    stu.setStudentId(stuInfo[0]);
                    stu.setName(stuInfo[1]);
                    stu.setGender(stuInfo[2]);
                    stu.setMajor(stuInfo[3]);
                    stu.setClassName(stuInfo[4]);
                    stu.setClassCode(stuInfo[5]);
                    stu.setCreatedTime(stuInfo[6]);
                    stu.setUpdatedTime(stuInfo[7]);
                    students.add(stu);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public static Course getCourseById(String inputId) {
        // 精准查询
        Course course = new Course();

        try (FileReader fr = new FileReader(COURSE_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] courseInfo = line.split(",");

                // 如果课程号等于输入的id则判定为要找的课程
                if (courseInfo.length == 4 && courseInfo[0].equals(inputId)) {
                    course.setCourseId(courseInfo[0]);
                    course.setName(courseInfo[1]);
                    course.setCredit(courseInfo[2]);
                    course.setStudyPeriod(courseInfo[3]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return course;
    }

    public static List<Course> getCourseByName(String targetName) {
        // 支持模糊查询
        Course course = new Course();
        List<Course> courses = new ArrayList<>();

        try (FileReader fr = new FileReader(COURSE_FILE_PATH);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] courseInfo = line.split(",");

                // 如果课程名包含输入的汉字则判定为要找的课程
                if (courseInfo.length == 4 && courseInfo[1].contains(targetName)) {
                    course.setCourseId(courseInfo[0]);
                    course.setName(courseInfo[1]);
                    course.setCredit(courseInfo[2]);
                    course.setStudyPeriod(courseInfo[3]);
                    courses.add(course);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    public static void updateCourseInfo(Course updateCourse) {
        try (BufferedReader reader = new BufferedReader(new FileReader(COURSE_FILE_PATH))) {
            String line;
            List<String> lines = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 4 && parts[0].equals(updateCourse.getCourseId())) {
                    // 更新课程信息
                    line = updateCourse.getCourseId() + "," +
                            updateCourse.getName() + "," +
                            updateCourse.getCredit() + "," +
                            updateCourse.getStudyPeriod();
                }

                lines.add(line);

                // 调用方法清空原文件内容并把新内容写入
                updateFile(lines, COURSE_FILE_PATH);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addGradeIntoFile(Grade grade) {
        try (FileWriter fr = new FileWriter(GRADE_FILE_PATH, true);
             PrintWriter pw = new PrintWriter(fr)) {
            pw.println(grade.getStudentId() + "," + grade.getCourseId() + "," + grade.getScore() +
                    "," + grade.getScoreLevel());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Grade> readGradesFromFile() {
        List<Grade> grades = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(GRADE_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 文件中每行的格式为学号，课程号，成绩，成绩分段
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Grade grade = new Grade(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3]);
                    grades.add(grade);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return grades;
    }

    public static String getPasswordFromFile(String administratorId) {
        try (BufferedReader br = new BufferedReader(new FileReader(ADMINISTRATORS_FILE_PATH))){
            String line;

            while ((line = br.readLine()) != null) {
                String[] adminInfo = line.split(",");
                if (adminInfo.length == 5 && administratorId.equals(adminInfo[0])){
                    return adminInfo[2];
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        // 因为一定要返回值所以加上了这一句。但是这个方法会直接在上面返回不会到达这里
        return null;
    }
}
