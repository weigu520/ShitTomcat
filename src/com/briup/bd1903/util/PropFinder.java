package com.briup.bd1903.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 描述:
 *
 * @author WeiGu
 * @create 2019-08-28 9:35
 */
public class PropFinder {
    public static final Properties pro=new Properties();

    static {
        try {
            pro.load(new FileInputStream("src/config.properties"));
            pro.load(new FileInputStream("src/mime.properties"));
            pro.load(new FileInputStream("src/status_code.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int getServer_Port(){
        return Integer.valueOf(pro.getProperty("server_port"));
    }
    public static String path(){
        return pro.getProperty("path");
    }
    public static String errorFile(){
        return pro.getProperty("errorFile");
    }
    public static String welFile(){
        return pro.getProperty("welFile");
    }

    public static String getMime(String name) {return pro.getProperty(name);
    }

    public static String getStatus_code_mes(int status_code) {
        return (String) pro.get(status_code);
//        return pro.getProperty(status_code+"");
    }
//    public static String ok(){return pro.getProperty("200");}
//    public static String notFound(){return pro.getProperty("404");}
}