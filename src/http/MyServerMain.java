package http;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 描述:
 *
 * @author WeiGu
 * @create 2019-08-27 14:09
 */
public class MyServerMain {
    public static void main(String[] args) {
        /*
        1.搭建一个tcp服务器
        2.接受客户端连接
        3.接受客户端发来的所有数据,输出到控制台上
         */
        try {
            //1.搭建一个tcp服务器
            ServerSocket ss = new ServerSocket(8888);
            System.out.println("服务器启动了------------");

            //2.接受客户端连接
            Socket s = ss.accept();
            if(s!=null){
                System.out.println("连接成功了---------");
            }

            //3.接受客户端发来的所有数据,输出到控制台上
            /*
            URL:G:\myserver\a.txt
            1.http请求的数据
            2.请求行,请求头,空行,请求体
             */

            //A准备流
            InputStream is = s.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            OutputStream os = s.getOutputStream();

//            String message1 = br.readLine();
//            System.out.println(message1);

            String line=br.readLine();
//            System.out.println(line);
            //按照空格拆分请求行  GET /a.txt HTTP/1.1
            String[] array = line.split(" ");

            //干掉斜杆
            String fileName = array[1].substring(1);

            //判断资源是否存在
            //准备流
            File file = new File("G:\\myserver",fileName);
            boolean flag = file.exists();
            if(flag){
                //如果有,判断是否请求资源
                if("/".equals(array[1])){
                    //返回welcome.html
                    String reply = "HTTP/1.1 200 OK\n";             //响应行
                    reply += "Content-type:text/html\n\n";          //响应头
                    BufferedReader reader = new BufferedReader(new FileReader(new File("G:\\myserver","welcom.html")));
                    String message=null;
                    while ((message=reader.readLine())!=null){
                        reply+=message;
                    }
                    os.write(reply.getBytes("GB2312"));
                }else{
//                System.out.println("我有,不想给你行不行");
                    //返回客户端a.txt文件
                    String reply = "HTTP/1.1 200 OK\n";             //响应行
                    reply += "Content-type:text/html\n\n";        //响应头
                    FileReader fr = new FileReader(file);
                    BufferedReader reader = new BufferedReader(fr);
//                InputStreamReader ISR = new InputStreamReader(new FileInputStream(file), "GBK");
//                BufferedReader reader = new BufferedReader(ISR);
                    String message = null;
                    while ((message = reader.readLine()) != null) {
                        reply += message;
                    }
                    os.write(reply.getBytes("GB2312"));
                    //http响应
                    //响应行
                    //响应头(为空)
                    //空行
                    //消息体(文件)
                }
            }else{
                System.out.println("我没有,想给你都不行");
                String reply = "HTTP/1.1 200 OK\n";             //响应行
                reply += "Content-type:text/html\n\n";          //响应头
                BufferedReader reader = new BufferedReader(new FileReader(new File("G:\\myserver","error.html")));
                String message=null;
                while ((message=reader.readLine())!=null){
                    reply+=message;
                }
                os.write(reply.getBytes("GB2312"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}