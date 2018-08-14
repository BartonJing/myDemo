package com.barton.io_nio.io.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * create by barton on 2018-8-13
 */
public class IoFileCopy {

    public static void main(String[] args) throws Exception {
        String oldPath = "d://1.txt";
        String newPath = "d://2.txt";
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }

        } catch (Exception e) {
            //logger.info("复制单个文件操作出错");
            e.printStackTrace();
        }


    }
}
