package com.jien217.xiaoYi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;

/**
 * Created by Jien217 on 2016/10/9.
 * 思路：
 * 1.创建界面
 * 2.添加选择文件按钮，为按钮添加文件选择器对话框
 * 3.添加开始重命名按钮，为按钮添加一个执行重命名操作的动作，该动作是一个方法。
 * 4.重命名的方法：
 * a.获取文件选择器选中的文件夹
 * b.判断该文件夹是否是DCIM文件夹，如果是，继续向下运行
 * c.在该DCIM文件夹下创建一个延时素材文件夹，用于存放重命名之后的文件
 * d.获取根文件夹下的所有文件对象并打印输出。
 * e.对这所有的文件对象，判断如果是文件夹，则进一步操作。
 * f.
 * <p>
 * <p>
 * <p>todo 当目标文件夹已存在同名文件时的解决办法：加一个判断，如果存在同名文件，则照片的编号+1 后再进行重命名并移动的尝试。
 * 对延时素材文件夹加上日期
 * 把结果目录设置为与DCIM同级别？
 * <p>
 * <p>
 * 程序编码完成心得：
 */
public class YanShiUI extends JFrame {
    //设置框架大小
    private static final int DEFAULT_WIDTH = 500;        //设置框架宽度
    private static final int DEFAULT_HEIGHT = 150;       //设置框架高度
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();     //通过 Toolkit 工具类获取屏幕尺寸
    final int SCREEN_WIDTH = screenSize.width;                              //获取屏幕宽度
    final int SCREEN_HEIGHT = screenSize.height;                            //获取屏幕高度

    private JFileChooser fileChooser = new JFileChooser();      //定义文件选择器

    //UI 类的构造方法，默认需要设置标题。
    public YanShiUI(String title) throws HeadlessException {
        super(title);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);         //设置窗口大小为默认尺寸
        setLocation((SCREEN_WIDTH - DEFAULT_WIDTH) / 2, (SCREEN_HEIGHT - DEFAULT_HEIGHT) / 2);    //把窗口居中

        JPanel panel = new JPanel();        //新建面板
        JButton chooserButton = new JButton("选择文件夹");     //新建选择文件夹按钮

        //选择按钮
        chooserButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");     //windows 观感
                    SwingUtilities.updateComponentTreeUI(fileChooser);
                } catch (Exception chooserOpenException) {
                    chooserOpenException.printStackTrace();
                }
                fileChooser.showOpenDialog(YanShiUI.this);      //弹出对话框
            }
        });
        panel.add(chooserButton);     //添加按钮到面板

        //开始按钮
        JButton start = new JButton("开始重命名");         //新建开始重命名按钮
        start.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    startButtonPerformed();        //“开始重命名”按钮动作
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        panel.add(start);                               //添加按钮到面板

        add(panel, BorderLayout.NORTH);
    }

    /**
     * 开始重命名按钮的动作
     */
    public void startButtonPerformed() throws InterruptedException {
        //定义文件对象
        File choosedDirectory;      //文中的文件夹或文件
        File resultDirectory;       //存放结果的文件夹
        String rootPath;            //操作的根目录DCIM
        int photoResultOrder = 1;   //结果文件的编号
        long startTime = System.currentTimeMillis();

        //获取操作的根目录
        choosedDirectory = fileChooser.getCurrentDirectory();
        rootPath = choosedDirectory.getAbsolutePath();
        System.out.println("已选文件的目录：" + rootPath);

        if (rootPath.endsWith("\\DCIM")) {      //只对DCIM文件夹进行操作，避免对其他文件误操作。
            //新建存放结果的文件夹
            resultDirectory = new File(rootPath + "\\延时素材");
            resultDirectory.mkdir();

            //获取根目录下的文件夹列表
            String[] fileObjectList = choosedDirectory.list();
            Arrays.sort(fileObjectList);
            System.out.println("操作根目录下的文件对象列表：");
            for (String s : fileObjectList) {
                System.out.println(s);
            }

            for (String s : fileObjectList) {             //获取根目录下的每个文件对象
                //下面是内部循环
                File innerDirectory = new File(rootPath + "\\" + s);        //获取根目录下的每个文件对象的地址

                if (innerDirectory.isDirectory() && s.endsWith("MEDIA")) {  //仅对名称以 MEDIA 为结尾的文件夹进行下一步操作
                    String[] photoList = innerDirectory.list();             //获取每个 1xxMEDIA 文件夹下的照片的文件名列表
                    Arrays.sort(photoList);     //排序

                    /**
                     * 对 1xxMEDIA 文件夹下的具体照片文件进行移动操作
                     */
                    for (String st : photoList) {
                        if (st.startsWith("Y0") && st.endsWith(".jpg")) {   //只对以 Y0 开头的 .jpg 文件进行操作
                            File photoFile = new File(innerDirectory + "\\" + st);        //获取单张照片的文件路径

                            if (photoFile.isFile()) {                       //该文件对象时一个文件时才进行移动并重命名
                                File resultFileName = new File(resultDirectory, "\\YS" + renameOrderHelper(photoResultOrder) + ".jpg");  //这张照片的新地址和名字

                                if (photoFile.renameTo(resultFileName))     //对经过层层筛选的照片文件进行重命名并移动
                                    photoResultOrder++;                     //照片编号自动增加
                            }
                        }
                    }

                    //Thread.sleep((long)100);
                    if (photoList.length == 0) {           //如果文件夹为空(以 MEDIA 为结尾的文件夹)
                        innerDirectory.delete();        //则删除该文件夹
                        System.out.println("\n已删除空文件夹： \"" + s + "\"");
                    }
                }

            }

            if (photoResultOrder == 1) {
                System.out.println("\n没有找到照片文件，请检查文件目录是否正确！\n");      //文件夹选正确了，但里面没有照片文件
                return;
            }

            System.out.println("操作已完成！已移动并重命名照片文件共 " + (photoResultOrder - 1) + " 张。用时 " + (System.currentTimeMillis() - startTime) + " 毫秒\n");                             //程序正常结束

        } else {
            System.out.println("文件夹选择错误！必须以文件夹“DCIM”作为根目录！\n");     //文件夹选择错误
        }
    }

    /**
     * 照片重命名编号的辅助方法。照片命名结果例：YS000001.jpg ~ YS999999.jpg
     *
     * @param order 照片编号
     * @return 已补全前缀0的编号
     */
    public static String renameOrderHelper(int order) {
        if (order > 0 && order < 10) {
            return "00000" + order;
        } else if (order >= 10 && order < 100) {
            return "0000" + order;
        } else if (order >= 100 && order < 1000) {
            return "000" + order;
        } else if (order >= 1000 && order < 10000) {
            return "00" + order;
        } else if (order >= 10000 && order < 100000) {
            return "0" + order;
        } else {
            return "" + order;
        }
    }

    public static void main(String[] args) {

    }

}
