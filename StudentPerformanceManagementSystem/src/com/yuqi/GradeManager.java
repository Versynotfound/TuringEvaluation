package com.yuqi;

import java.util.Scanner;

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

    public void start() throws Exception {
        while (true) {
            System.out.println("============成绩信息管理============");
            System.out.println("1.更新学生成绩");
            // 查询成绩包括查询单科全部成绩，平均分，按最高分、最低分排列
            System.out.println("2.查询学生成绩");
            System.out.println("4.删除学生成绩");
            System.out.println("5.退出");
            System.out.println("===================================");
            Thread.sleep(300);
            System.out.println("请选择操作：");
            String command = sc.next();

            switch (command) {
                case "1" -> updateStudentGrade();
                case "2" -> queryStudentGrade();
                case "3" -> deleteStudentGrade();
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

    private void updateStudentGrade() {
        System.out.println("============更新学生成绩============");

    }

    private void queryStudentGrade() {
    }

    private void deleteStudentGrade() {
    }
}
