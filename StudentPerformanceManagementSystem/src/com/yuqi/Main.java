package com.yuqi;

import com.yuqi.manager.MainManager;

/**
 * 主类
 *
 * @author yuqi
 * @version 1.0
 * date 2023/12/16
 */
public class Main {
    public static void main(String[] args) throws Exception {
        MainManager mainManager = new MainManager();
        mainManager.start();
    }
}