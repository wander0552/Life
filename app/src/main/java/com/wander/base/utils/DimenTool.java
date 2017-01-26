package com.wander.base.utils;

/**
 * Created by wander on 2016/7/15.
 * * 快速生成适配工具类
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class DimenTool {

    public static void gen() {
        //以此文件夹下的dimens.xml文件内容为初始值参照
        File file = new File("./app/src/main/res/values/dimens.xml");

        BufferedReader reader = null;
        StringBuilder sw320 = new StringBuilder();
        StringBuilder sw480 = new StringBuilder();
        StringBuilder sw540 = new StringBuilder();
        //音响设备
        StringBuilder sw552 = new StringBuilder();
        StringBuilder sw600 = new StringBuilder();
        StringBuilder sw672 = new StringBuilder();
        StringBuilder sw720 = new StringBuilder();
        StringBuilder sw1080 = new StringBuilder();
        StringBuilder sw1440 = new StringBuilder();
        //等同于540
        StringBuilder xlarge = new StringBuilder();
        try {

            System.out.println("生成不同分辨率：");

            reader = new BufferedReader(new FileReader(file));

            String tempString;

            int line = 1;

            // 一次读入一行，直到读入null为文件结束

            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("</dimen>")) {

                    //tempString = tempString.replaceAll(" ", "");

                    String start = tempString.substring(0, tempString.indexOf(">") + 1);

                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    //截取<dimen></dimen>标签内的内容，从>右括号开始，到左括号减2，取得配置的数字
                    Double num = Double.parseDouble
                            (tempString.substring(tempString.indexOf(">") + 1,
                                    tempString.indexOf("</dimen>") - 2));
                    System.out.println(num);

                    //根据不同的尺寸，计算新的值，拼接新的字符串，并且结尾处换行。
                    sw320.append(start).append(getFloat(num * 0.30)).append(end).append("\r\n");
                    sw480.append(start).append(getFloat(num * 0.44)).append(end).append("\r\n");
                    sw540.append(start).append(getFloat(num * 0.5)).append(end).append("\r\n");
                    sw552.append(start).append(getFloat(num * 0.51)).append(end).append("\r\n");
                    sw600.append(start).append(getFloat(num * 0.56)).append(end).append("\r\n");
                    sw672.append(start).append(getFloat(num * 0.56)).append(end).append("\r\n");
                    sw720.append(start).append(new BigDecimal(num * 0.67).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()).append(end).append("\r\n");
                    sw1080.append(start).append(getFloat(num * 1)).append(end).append("\r\n");
                    sw1440.append(start).append(getFloat(num * 1.33)).append(end).append("\r\n");
                    xlarge.append(start).append(getFloat(num * 0.75)).append(end).append("\r\n");


                } else {
                    sw320.append(tempString).append("\r\n");
                    sw480.append(tempString).append("\r\n");
                    sw540.append(tempString).append("\r\n");
                    sw552.append(tempString).append("\r\n");
                    sw600.append(tempString).append("\r\n");
                    sw672.append(tempString).append("\r\n");
                    sw720.append(tempString).append("\r\n");
                    sw1080.append(tempString).append("\r\n");
                    sw1440.append(tempString).append("\r\n");
                    xlarge.append(tempString).append("\r\n");
                }

                line++;

            }

            reader.close();


            System.out.println("<!--  sw720 -->");

            System.out.println(sw720);

            String sw320file = "./app/src/main/res/values-sw320dp";
            String sw480file = "./app/src/main/res/values-sw480dp";
            String sw540file = "./app/src/main/res/values-sw540dp";
            String sw552file = "./app/src/main/res/values-sw552dp";
            String sw600file = "./app/src/main/res/values-sw600dp";
            String sw672file = "./app/src/main/res/values-sw672dp";
            String sw720file = "./app/src/main/res/values-sw720dp";
            String sw1080file = "./app/src/main/res/values-sw1080dp";
            String sw1440file = "./app/src/main/res/values-sw1440dp";
            String xlargefile = "./app/src/main/res/values-xlarge-v4";

            //将新的内容，写入到指定的文件中去
            writeFile(sw320file, sw320.toString());
            writeFile(sw480file, sw480.toString());
            writeFile(sw540file, sw540.toString());
            writeFile(sw552file, sw552.toString());
            writeFile(sw600file, sw600.toString());
            writeFile(sw672file, sw672.toString());
            writeFile(sw720file, sw720.toString());
            writeFile(sw1080file, sw1080.toString());
            writeFile(sw1440file, sw1440.toString());
            writeFile(xlargefile, xlarge.toString());

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            if (reader != null) {

                try {

                    reader.close();

                } catch (IOException e1) {

                    e1.printStackTrace();

                }

            }

        }

    }

    public static float getFloat(double num) {
        return new BigDecimal(num).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }


    /**
     * 写入方法
     */

    public static void writeFile(String file, String text) {
        File dir = new File(file);
        File dimenFile = null;
        if (!dir.exists()) {
            if (dir.mkdir()) {
                dimenFile = new File(dir.getAbsolutePath() + File.separator + "dimens.xml");
            }
        } else {
            dimenFile = new File(dir.getAbsolutePath() + File.separator + "dimens.xml");
        }

        PrintWriter out = null;

        try {

            out = new PrintWriter(new BufferedWriter(new FileWriter(dimenFile)));

            out.println(text);

        } catch (IOException e) {

            e.printStackTrace();

        }


        out.close();

    }

    public static void main(String[] args) {

        gen();

    }

}
