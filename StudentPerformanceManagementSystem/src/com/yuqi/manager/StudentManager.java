package com.yuqi.manager;

import com.yuqi.object.Student;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 学生管理类
 *
 * @author yuqi
 * @version 2.0
 * date 2023/12/19
 */
public class StudentManager {
    private final Scanner sc = new Scanner(System.in);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy年MM月dd日 hh时mm分ss秒");
    private final String STUDENT_FILE_PATH = "StudentPerformanceManagementSystem/src/StudentInfo";
    private final Pattern namePattern = Pattern.compile("[A-Za-z0-9]");

    public void start() throws Exception {
        while (true) {
            System.out.println("============学生信息管理============");
            System.out.println("1.添加学生");
            System.out.println("2.查询学生");
            System.out.println("3.修改学生信息");
            System.out.println("4.删除学生");
            System.out.println("5.退出");
            System.out.println("===================================");
            Thread.sleep(300);

            System.out.println("请选择操作：");
            String command = sc.next();

            switch (command) {
                case "1" -> addStudent();
                case "2" -> queryStudent();
                case "3" -> updateStudentInfo();
                case "4" -> removeStudent();
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

    private void addStudent() throws Exception {
        System.out.println("==============添加学生==============");
        String name;
        String studentId = null;
        String email;
        while (true) {
            System.out.print("请输入学生学号：");
            String inputId = sc.next();

            if (!(isStudentIdValid(inputId))) {
                continue;
            }

            if (FileManager.isFileEmpty(STUDENT_FILE_PATH)) {
                studentId = inputId;
                break;
            }

            Student student = FileManager.getStudentById(inputId);
            if (student != null) {
                System.out.println("该学号已存在，是否重新输入？y/Y重新输入，任意键退出");
                String reply = sc.next();
                if (reply.equals("y") || reply.equals("Y")) {
                    continue;
                }
                break;
            }
            studentId = inputId;
            break;
        }

        while (true) {
            System.out.print("请输入学生姓名：");
            String inputName = sc.next();

            if (!(isNameValid(inputName))) {
                continue;
            }
            name = inputName;
            break;
        }

        while (true) {
            System.out.print("请输入学生邮箱：");
            String inputEmail = sc.next();

            if (!(isEmailValid(inputEmail))) {
                continue;
            }
            email = inputEmail;
            break;
        }

        // 拆解学号，获得相应信息
        // 获取入学年（级数）
        String startYear = studentId.substring(0, 4);
        // 获取专业代码...以此类推
        String majorCode = studentId.substring(4, 8);
        String className = studentId.substring(8, 9);
        String genderNumber = studentId.substring(9, 10);
        String classCode = studentId.substring(10);

        String gender = (genderNumber.equals("0")) ? "女" : "男";

        // 通过文档读取专业信息
        String major = FileManager.getMajorInfo(majorCode);

        // 得到学生创建时间
        LocalDateTime createdLocalDateTime = LocalDateTime.now();
        // 将学生创建时间格式化
        String createdTime = formatter.format(createdLocalDateTime);

        Student student = new Student(name, studentId, gender, className, classCode, major, email, startYear, createdTime);

        // 将学生信息注入文档
        FileManager.addStudentIntoFile(student);

        System.out.println("[学生 " + student.getName() + " 添加成功！]");
        Thread.sleep(300);
    }

    private void queryStudent() throws Exception {
        if (FileManager.isFileEmpty(STUDENT_FILE_PATH)) {
            System.out.println("[当前系统尚未录入任何学生信息，请先录入再进行操作]");
            Thread.sleep(300);
            return;
        }

        while (true) {
            System.out.println("=============查询学生信息=============");
            System.out.println("1.通过学号查询");
            System.out.println("2.通过姓名查询");
            System.out.println("3.根据专业号查询");
            System.out.println("4.展示所有学生个人信息");
            System.out.println("5.返回");
            System.out.println("===================================");
            Thread.sleep(300);


            System.out.println("请选择操作：");
            String command = sc.next();

            switch (command) {
                case "1" -> queryStudentById();
                case "2" -> queryStudentByName();
                case "3" -> queryStudentByMajorId();
                case "4" -> FileManager.showAllStudent();
                case "5" -> {
                    return;
                }
                default -> {
                    System.out.println("[指令不存在，请重试]");
                    Thread.sleep(200);
                }
            }
        }
    }

    private void queryStudentById() throws Exception {
        System.out.println("=============通过学号查询=============");
        // 支持模糊查询
        String targetId;
        while (true) {
            System.out.print("请输入学生的学号：");
            targetId = sc.next();

            if (targetId.length() > 12 || !(targetId.matches("\\d+"))) {
                System.out.println("[学号格式有误，请检查并重试]");
                continue;
            }
            break;
        }

        // 模糊查询，遍历集合，将包含该学号的学生放入另一个集合
        List<Student> selectedStudents = FileManager.getStudentsById(targetId);

        if (selectedStudents.isEmpty()) {
            System.out.println("[未找到相关学生的记录]");
            Thread.sleep(300);
        } else {
            // 调用方法打印符合条件的学生的信息
            printStudentInfo(selectedStudents);
        }
        System.out.println("按任意键退出...");
        sc.next();
    }

    private void queryStudentByName() throws Exception {
        System.out.println("=============通过姓名查询=============");
        while (true) {
            System.out.println("请输入学生姓名");
            // 支持模糊查询
            String targetName = sc.next();

            Matcher nameMatcher = namePattern.matcher(targetName);
            if (nameMatcher.find() || targetName.length() > 6) {
                System.out.println("[格式不正确,请检查并重新输入]");
                Thread.sleep(300);
                continue;
            }

            List<Student> selectedStudents = FileManager.getStudentByName(targetName);

            if (selectedStudents.isEmpty()) {
                System.out.println("[未找到相关学生的记录]");
                Thread.sleep(300);
            } else {
                // 调用方法打印符合条件的学生的信息
                printStudentInfo(selectedStudents);
            }
            System.out.println("按任意键退出...");
            sc.next();
            break;
        }
    }

    private void queryStudentByMajorId() throws Exception {
        System.out.println("============通过专业号查询============");
        String majorId;
        String majorName;
        while (true) {
            System.out.print("请输入准确的专业号：");
            String inputId = sc.next();

            if (!(inputId.matches("000[0-6]"))) {
                System.out.println("[专业号格式错误，请重试]");
                Thread.sleep(200);
                continue;
            }

            majorId = inputId;

            majorName = FileManager.getMajorInfo(majorId);
            break;
        }

        List<Student> selectedStudents = FileManager.getStudentsByMajorName(majorName);

        if (selectedStudents.isEmpty()) {
            System.out.println("[未找到相关学生的记录]");
            Thread.sleep(300);
        } else {
            // 调用方法打印符合条件的学生的信息
            printStudentInfo(selectedStudents);
        }
        System.out.println("按任意键退出...");
        sc.next();
    }

    public void printStudentInfo(List<Student> students) {
        System.out.println("共找到 " + students.size() + " 条学生信息 --->");
        System.out.println("学号            姓名   性别      专业      班级  班级序号    邮箱" +
                "              信息建立时间              信息修改时间");
        for (Student stu : students) {
            System.out.println(stu.getStudentId() + "\t" + stu.getName() + "\t" + stu.getGender() +
                    "\t" + stu.getMajor() + "\t\t" + stu.getClassName() + "\t" + stu.getClassCode() +
                    "\t\t" + stu.getEmail() + "\t\t" + stu.getCreatedTime() + "\t" + stu.getUpdatedTime());
        }
    }

    private void updateStudentInfo() throws Exception {
        if (FileManager.isFileEmpty(STUDENT_FILE_PATH)) {
            System.out.println("[当前系统尚未录入任何学生信息，请先录入再进行操作]");
            Thread.sleep(300);
            return;
        }

        System.out.println("==========修改学生个人信息==========");
        while (true) {
            System.out.print("请输入需要修改的学生的学号：");
            String inputId = sc.next();

            if (!(isStudentIdValid(inputId))) {
                continue;
            }

            Student student = FileManager.getStudentById(inputId);
            if (student == null) {
                System.out.println("该生不存在。y/Y重新输入学号，任意键退出");
                String reply = sc.next();
                if ((reply.equals("y") || reply.equals("Y"))) {
                    continue;
                }
                return;
            }

            while (true) {
                System.out.println("您需要修改学生" + student.getName() + "的：");
                System.out.println("1.姓名");
                System.out.println("2.邮箱");
                System.out.println("3.取消");
                Thread.sleep(300);
                System.out.println("请选择操作：");

                String command = sc.next();
                switch (command) {
                    case "1" -> updateStudentName(student);
                    case "2" -> updateStudentEmail(student);
                    case "3" -> {
                        return;
                    }
                    default -> {
                        System.out.println("[指令不存在，请重试]");
                        Thread.sleep(200);
                    }
                }

                System.out.println("[修改成功！]");
                Thread.sleep(300);
                return;
            }
        }
    }

    private synchronized void updateStudentName(Student student) throws Exception {
        while (true) {
            System.out.print("请输入新名字：");
            String newName = sc.next();

            if (!(isNameValid(newName))) {
                continue;
            }

            if (newName.equals(student.getName())) {
                System.out.println("新名字与原名字相同。是否取消更改？y/Y取消更改，任意键重新输入");
                String reply = sc.next();
                if (reply.equals("y") || reply.equals("Y")) {
                    return;
                }
                continue;
            }

            student.setName(newName);

            // 更新修改时间
            LocalDateTime updatedLocalDateTime = LocalDateTime.now();
            String updatedTime = formatter.format(updatedLocalDateTime);
            student.setUpdatedTime(updatedTime);
            // 将更新后信息写入文件
            FileManager.updateStudentInfo(student);
            break;
        }
    }

    private synchronized void updateStudentEmail(Student student) throws Exception {
        while (true) {
            System.out.print("请输入新邮箱：");
            String newEmail = sc.next();

            if (!(isEmailValid(newEmail))) {
                continue;
            }

            student.setEmail(newEmail);

            LocalDateTime updatedTime = LocalDateTime.now();
            String result = formatter.format(updatedTime);
            student.setUpdatedTime(result);
            FileManager.updateStudentInfo(student);
            System.out.println("[修改成功！]");
            Thread.sleep(200);
            break;
        }
    }

    private void removeStudent() throws Exception {
        if (FileManager.isFileEmpty(STUDENT_FILE_PATH)) {
            System.out.println("[当前系统尚未录入任何学生信息，请先录入再进行操作]");
            Thread.sleep(300);
            return;
        }

        System.out.println("==============删除学生==============");
        while (true) {
            System.out.println("请输入需要删除的学生的学号：");
            String studentId = sc.next();

            synchronized ("锁") {
                Student student = FileManager.getStudentById(studentId);

                if (student == null) {
                    System.out.println("该生不存在。y/Y重新输入学号，任意键退出");
                    String reply = sc.next();
                    if (reply.equals("y") || reply.equals("Y")) {
                        continue;
                    }
                    return;
                }

                System.out.println("您确定删除学生 " + student.getName() + " 的相关信息吗？y/Y确认，任意键取消");
                String reply = sc.next();
                if (!(reply.equals("y") || reply.equals("Y"))) {
                    return;
                }

                // 成绩处变化
                FileManager.removeGradesFromFile(student.getStudentId());
                FileManager.removeStudentFromFile(student);
            }

            System.out.println("[删除成功]");
            Thread.sleep(300);
            break;
        }
    }

    public boolean isNameValid(String inputName) throws Exception {
        // 这里拦截了数字和字母但不知道怎么拦截#￥%&等乱七八糟的情况
        Matcher nameMatcher = namePattern.matcher(inputName);
        // 中国公民姓名长度最大为六个汉字
        if (nameMatcher.find() || inputName.length() < 2 || inputName.length() > 6) {
            System.out.println("[输入的姓名格式不正确,请检查并重新输入]");
            Thread.sleep(300);
            return false;
        }
        return true;
    }

    public static boolean isStudentIdValid(String inputId) throws Exception {
        /* 该系统只能录入2000级之后23级及之前的学生信息
         * 专业编号为0000到0006
         * 班级为1到9班
         * 班级序号为1到99
         * */
        if (!inputId.matches("20(0[0-9]|1[0-9]|2[0-3])0{3}[0-6][1-9][01][1-9]\\d")) {
            System.out.println("[该学号不合法，请检查并重新输入正确的学号]");
            Thread.sleep(300);
            return false;
        }
        return true;
    }

    public boolean isEmailValid(String inputEmail) throws Exception {
        // 邮箱@前是两位以上的数字或字母，@后为两位以上二十位以下的数字或字母，加上点以及二位以上十位以下一级或二级域名
        if (!(inputEmail.matches("\\w{2,}@\\w{2,20}(\\.\\w{2,10}){1,2}"))) {
            System.out.println("[邮箱格式有误，请检查并重新输入]");
            Thread.sleep(300);
            return false;
        }
        return true;
    }
}