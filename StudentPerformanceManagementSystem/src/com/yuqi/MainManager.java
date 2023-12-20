package com.yuqi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 主界面
 *
 * @author yuqi
 * @version 3.0
 * date 2023/12/16
 */
public class MainManager {
    private final List<Administrator> administrators = new ArrayList<>();
    private static final String ADMINISTRATORS_FILE_PATH = "StudentPerformanceManagementSystem/src/Administrator";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy年MM月dd日 hh时mm分ss秒");
    private final Scanner sc = new Scanner(System.in);
    private Administrator loginAdmin;

    public void start() throws Exception {

        System.out.println("欢迎使用学生成绩管理系统！");
        Thread.sleep(300);
        while (true) {
            System.out.println("==========学生成绩管理系统==========");
            System.out.println("1.管理员登录");
            System.out.println("2.管理员注册");
            System.out.println("3.退出");
            System.out.println("===================================");
            Thread.sleep(300);

            System.out.println("请选择操作：");
            String command = sc.next();
            // 为了提升交互感使用了Thread.sleep(),这就是为什么右边频繁爆黄
            switch (command) {
                case "1" -> logIn();
                case "2" -> signUp();
                case "3" -> {
                    System.out.println("[退出成功]");
                    return;
                }
                default -> {
                    System.out.println("[指令无效，请重新输入]");
                    Thread.sleep(300);
                }
            }
        }
    }

    private void logIn() throws Exception {
        if (FileManager.isFileEmpty(ADMINISTRATORS_FILE_PATH)) {
            System.out.println("[当前系统尚未录入任何管理员账号，请先注册]");
            Thread.sleep(300);
            return;
        }
        System.out.println("============管理员登录=============");
        while (true) {
            System.out.print("请输入账户id：");
            String inputId = sc.next();

            if (!(FileManager.isFind(inputId, ADMINISTRATORS_FILE_PATH))) {
                System.out.println("[账户不存在，请确认账户id并重新输入]");
                Thread.sleep(300);
                continue;
            }

            // debug!!!
            Administrator admin = FileManager.getAdministratorById(inputId);
            if (FileManager.isLocked(inputId)) {
                System.out.println("[ " + admin.getName() + " 账户因密码输入错误次数过多已被锁定，" +
                        "请联系2098285024@qq.com确认身份并解锁。]");
                Thread.sleep(500);
                System.out.println("按任意键退出...");
                sc.next();
                return;
            }

            int count = 0;
            while (true) {
                System.out.print("请输入密码：");
                String inputPassword = sc.next();
                if (count == 4) {
                    FileManager.setLocked(admin.getAdministratorId(), admin);
                    System.out.println("[密码输入错误5次，账户已被锁定。请请联系2098285024@qq.com确认身份并解锁。]");
                    Thread.sleep(500);
                    System.out.println("按任意键退出...");
                    sc.next();
                    return;
                }

                if (!inputPassword.equals(FileManager.getPasswordFromFile(inputId))) {
                    System.out.println("密码错误，请重新输入。d/D退出");
                    String command = sc.next();
                    if (!(Objects.equals(command, "d") || Objects.equals(command, "D"))) {
                        count++;
                        continue;
                    }
                    return;
                }

                //debug!!!!!
                System.out.println(loginAdmin);
                System.out.println(admin);
                loginAdmin = admin;
                System.out.println("[登录成功！]");
                Thread.sleep(200);
                gradeManageMenu();
                return;
            }
        }
    }

    private void signUp() throws Exception {
        Administrator admin = new Administrator();

        // 这里假设操作者拥有注册权限。
        System.out.println("============管理员注册=============");
        while (true) {
            System.out.print("请输入您的账户名称：");
            String name = sc.next();
            if (name.length() > 15) {
                System.out.println("[账户名过长，请重新设置。账户名应不大于15个字符]");
                Thread.sleep(300);
                continue;
            }

            admin.setName(name);
            break;
        }

        while (true) {
            System.out.print("请输入您的账户密码：");
            String password = sc.next();

            if (!(password.matches("[a-zA-Z0-9]{3,}"))) {
                System.out.println("[密码格式错误，请确保您的密码不少于3个字符且仅包含数字和字母]");
                Thread.sleep(300);
                continue;
            }
            System.out.print("请再次输入您的密码：");
            String confirmPassword = sc.next();
            if (confirmPassword.equals(password)) {
                admin.setPassword(password);
                break;
            } else {
                System.out.println("[两次密码不一致，请重试]");
                Thread.sleep(300);
            }
        }

        LocalDateTime createdTime = LocalDateTime.now();

        // 格式化日期
        String result = formatter.format(createdTime);
        admin.setCreateTime(result);

        admin.setAdministratorId(creatId());
        FileManager.addAdministratorIntoFile(admin);
        System.out.println("[管理员账户 " + admin.getName() + " 创建成功]");
        Thread.sleep(200);
        System.out.println("[您的账户id为：" + admin.getAdministratorId() + " ]");
        Thread.sleep(300);
    }

    private String creatId() {
        StringBuilder result = new StringBuilder();
        String id = "";

        while (true) {
            // 生成一个六位的账户id
            for (int i = 0; i < 6; i++) {
                int number = ThreadLocalRandom.current().nextInt(10);
                result.append(number);
                id = result.toString();
            }

            // 如果文件尚未录入任何账号直接返回id
            if (administrators.isEmpty()) {
                return id;
            }

            // 判断该id是否已存在，如果存在则再次执行循环生成id
            if (!(FileManager.isFind(id, ADMINISTRATORS_FILE_PATH))) {
                return id;
            }
        }
    }


    private void gradeManageMenu() throws Exception {
        StudentManager studentManager = new StudentManager();
        CourseManager courseManager = new CourseManager();
        GradeManager gradeManager = new GradeManager();
        System.out.println("管理员" + loginAdmin.getName() + ",欢迎进入学生管理系统。");
        Thread.sleep(300);
        while (true) {
            System.out.println("==========学生成绩管理系统==========");
            System.out.println("1.学生信息管理");
            System.out.println("2.课程信息管理");
            System.out.println("3.成绩信息管理");
            System.out.println("7.个人信息");
            System.out.println("8.退出登录");
            System.out.println("===================================");
            Thread.sleep(300);

            System.out.println("请选择操作：");
            String command = sc.next();

            switch (command) {
                case "1" -> studentManager.start();
                case "2" -> courseManager.start();
                case "3" -> gradeManager.start();
                case "7" -> {
                    // 因为有注销账户和修改密码功能，所以方法定义成boolean类型
                    // 如果返回true则代表注销成功或密码修改成功，返回系统初始界面
                    if (administratorMenu()) {
                        return;
                    }
                }
                case "8" -> {
                    System.out.println("确定要退出登录吗？y/Y确定，任意键取消");
                    String reply2 = sc.next();
                    if (Objects.equals(reply2, "y") || Objects.equals(reply2, "Y")) {
                        System.out.println("[退出成功]");
                        Thread.sleep(200);
                        return;
                    }
                }
                default -> {
                    System.out.println("[指令无效，请重新输入]");
                    Thread.sleep(300);
                }
            }
        }
    }

    private boolean administratorMenu() throws Exception {
        int year = loginAdmin.getCreateLocalDateTime().getYear();
        int month = loginAdmin.getCreateLocalDateTime().getMonthValue();
        int day = loginAdmin.getCreateLocalDateTime().getDayOfMonth();

        while (true) {
            System.out.println("==============个人信息==============");
            System.out.println("账户名：" + loginAdmin.getName());
            System.out.println("账户id:" + loginAdmin.getAdministratorId());
            System.out.println("创建时间：" + year + "年" + month + "月" + day + "日");
            System.out.println();
            System.out.println("1.修改名称");
            System.out.println("2.修改密码");
            System.out.println("3.注销账户");
            System.out.println("===================================");
            Thread.sleep(300);
            System.out.println("请选择指令，或按任意键退出：");
            String command = sc.next();

            if (command.equals("1")) {
                changeName();
                continue;
            }
            if (command.equals("2")) {
                System.out.println("修改密码后需要重新登录系统。确定要修改密码吗？y/Y确定，任意键取消");
                String reply1 = sc.next();
                if (Objects.equals(reply1, "y") || Objects.equals(reply1, "Y")) {
                    changePassword();
                    return true;
                }
                continue;
            }
            if (command.equals("3")) {
                return deleteAccount();
            }
            return false;
        }
    }

    private void changeName() throws Exception {
        while (true) {
            System.out.print("输入您要修改成的名称：");
            String newName = sc.next();

            if (newName.length() > 15) {
                System.out.println("[账户名过长，请重新设置。账户名应不大于15个字符]");
                Thread.sleep(300);
                continue;
            }
            if (!(newName.matches("[a-zA-Z0-9]"))) {
                System.out.println("[账户名格式错误，请确保您的账户名仅包含数字和字母]");
                Thread.sleep(300);
                continue;
            }

            loginAdmin.setName(newName);
            System.out.println("[已成功将账户名修改为 " + loginAdmin.getName() + " ]");
            Thread.sleep(300);
            break;
        }
    }

    private boolean deleteAccount() throws Exception {
        System.out.println("==============注销账户==============");
        System.out.println("确定要注销账户吗？y/Y确认，任意键取消");
        String reply1 = sc.next();
        if (!(Objects.equals(reply1, "y") || Objects.equals(reply1, "Y"))) {
            System.out.println("[已取消，即将返回主页面]");
            Thread.sleep(300);
            return false;
        }

        while (true) {
            System.out.print("为确认操作者身份，请输入您的账户密码：");
            String inputPassword = sc.next();
            if (!(inputPassword.equals(loginAdmin.getPassword()))) {
                System.out.println("[密码错误，任意键重试，d/D取消注销]");
                String reply2 = sc.next();
                if ((Objects.equals(reply2, "y") || Objects.equals(reply2, "Y"))) {
                    System.out.println("[取消成功，即将返回主页面]");
                    Thread.sleep(300);
                    return false;
                }
                continue;
            }

            System.out.println("[账户 " + loginAdmin.getName() + " 注销成功，即将回到系统初始界面]");
            FileManager.removeAdminFromFile(loginAdmin);
            return true;
        }
    }

    private void changePassword() throws Exception {
        System.out.println("==============修改密码==============");
        while (true) {
            System.out.print("请输入原密码：");
            String priorPassword = sc.next();
            if (!(priorPassword.equals(loginAdmin.getPassword()))) {
                System.out.println("[密码错误，请检查并重新输入]");
                Thread.sleep(300);
                continue;
            }

            while (true) {
                System.out.print("请输入修改密码：");
                String newPassword = sc.next();
                if (newPassword.equals(loginAdmin.getPassword())) {
                    System.out.println("[修改密码与原密码一致，无效操作]");
                    Thread.sleep(300);
                    System.out.println("是否继续修改密码？y/Y继续，任意键取消");
                    String command = sc.next();
                    if (Objects.equals(command, "y") || Objects.equals(command, "Y")) {
                        continue;
                    }
                    return;
                }

                if (!(newPassword.matches("[a-zA-Z0-9]{3,}"))) {
                    System.out.println("[密码格式错误，请确保您的密码不少于3个字符且仅包含数字和字母]");
                    Thread.sleep(300);
                    continue;
                }

                System.out.print("请再次输入修改密码：");
                String confirmPassword = sc.next();
                if (!(confirmPassword.equals(newPassword))) {
                    System.out.println("[两次密码不一致，请重试]");
                    Thread.sleep(300);
                    continue;
                }
                loginAdmin.setPassword(newPassword);
                FileManager.updateAdministratorFile(loginAdmin);
                System.out.println("修改密码成功！即将退出系统...");
                Thread.sleep(400);
                return;
            }
        }
    }
}