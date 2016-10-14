package com.jien217.xiaoYi;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jien217 on 2016/10/9.
 * 小蚁运动相机延时摄影文件夹辅助
 */
public class XiaoYiYanShiRenamer {
    public static void main(String[] args) {
        //为 GUI 新建一个线程，所有内容在新线程中运行
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                YanShiUI frame = new YanShiUI("小蚁运动相机_延时摄影重命名辅助工具");     //新建一个 JFrame 框架
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  //加入此句关闭窗口时才会自动关闭虚拟机
                frame.setResizable(false);                          //设置不可改变窗口大小

//                //设置窗口图标。首先获取一张图片
//                Image image = new ImageIcon("C:\\Users\\Jien217\\IdeaProjects\\World of AI\\media\\blue-ball.gif").getImage();
//                frame.setIconImage(image);          //为该框架设置一个图标

                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                frame.setVisible(true);                             //显示框架
            }
        });

    }
}
