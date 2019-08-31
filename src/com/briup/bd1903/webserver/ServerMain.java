package com.briup.bd1903.webserver;

import com.briup.bd1903.util.MyRunnable;
import com.briup.bd1903.util.PropFinder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 描述:
 *
 * @author WeiGu
 * @create 2019-08-28 9:32
 */
public class ServerMain{
    public static void main(String[] args) {
        new ServerMain().receive();
    }

    //
    public void receive(){
        try {
            //启动服务器
            ServerSocket ss = new ServerSocket(PropFinder.getServer_Port());
            System.out.println(
                        "----------------服务器开启成功--------------------" +
                            "\n" +
                            "                    _ooOoo_\n" +
                            "                    o8888888o\n" +
                            "                    88\" . \"88\n" +
                            "                    (| -_- |)\n" +
                            "                     O\\ = /O\n" +
                            "                 ____/`---'\\____\n" +
                            "               .   ' \\\\| |// `.\n" +
                            "                / \\\\||| : |||// \\\n" +
                            "              / _||||| -:- |||||- \\\n" +
                            "                | | \\\\\\ - /// | |\n" +
                            "              | \\_| ''\\---/'' | |\n" +
                            "               \\ .-\\__ `-` ___/-. /\n" +
                            "            ___`. .' /--.--\\ `. . __\n" +
                            "         .\"\" '< `.___\\_<|>_/___.' >'\"\".\n" +
                            "        | | : `- \\`.;`\\ _ /`;.`/ - ` : | |\n" +
                            "          \\ \\ `-. \\_ __\\ /__ _/ .-` / /\n" +
                            "  ======`-.____`-.___\\_____/___.-`____.-'======\n" +
                            "                     `=---='\n" +
                            " \n" +
                            "  .............................................\n" +
                            "           佛祖保佑             永无BUG\n");

            while (true) {
                //获取套接字对象
                Socket socket = ss.accept();
                String ip = socket.getInetAddress().getHostAddress();
                Date date = new Date();
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                MyRunnable myRunnable = new MyRunnable(socket);
                new Thread(myRunnable).start();
                System.out.println(sf.format(date)+"\t"+ip+"连接成功了---------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}