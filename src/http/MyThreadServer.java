package http;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 描述:
 *
 * @author WeiGu
 * @create 2019-08-27 17:25
 */
public class MyThreadServer extends Thread{
    public static void main(String[] args) {
        new MyThreadServer().start();
    }
    @Override
    public void run() {
        try {
            //1.搭建一个tcp服务器
            ServerSocket ss = new ServerSocket(8888);
            System.out.println("服务器启动了------------");

            //2.接受客户端连接
            while (true) {
                Socket s = ss.accept();
                if (s != null) {
                    Date date = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String ip = s.getInetAddress().getHostAddress();
                    System.out.println(ft.format(date)+"\t"+ip+"连接成功了---------");
                }
                //A准备流
                InputStream is = s.getInputStream();
                InputStreamReader isr = new InputStreamReader(is/*,"UTF-8"*/);
                BufferedReader br = new BufferedReader(isr);
                OutputStream os = s.getOutputStream();
                PrintStream ps = new PrintStream(os);

                String line=br.readLine();
                //按照空格拆分请求行  GET /a.txt HTTP/1.1
                String[] array = line.split(" ");
                //干掉斜杆
                String fileName = array[1].substring(1);

                //判断资源是否存在
                File file = new File("G:\\myserver",URLDecoder.decode(fileName,"UTF-8"));
                boolean flag = file.exists();
                if(flag){
                    //如果有,判断是否请求资源
                    if("/".equals(array[1])){
                        //返回welcome.html
                        String reply = "HTTP/1.1 200 OK";//响应行
                        ps.println(reply);
                        ps.println();
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream((new File("G:\\myserver\\welcom.html"))));
                        int length=0;
                        byte[] bytes=new byte[1024*2];
                        while ((length = bis.read(bytes)) != -1){
                            ps.write(bytes,0,length);
                        }
                    }else{
                        //返回客户端文件
                        String reply = "HTTP/1.1 200 OK";//响应行
                        ps.println(reply);
                        ps.println();
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                        byte[] bytes=new byte[1024*2];
                        int length=0;
                        while ((length = bis.read(bytes)) != -1) {
                            ps.write(bytes,0,length);
                        }
                        ps.close();
                        os.close();
                        bis.close();
                    }
                }else{
                    String reply = "HTTP/1.1 200 OK";//响应行
                    ps.println(reply);
                    ps.println();
                    ps.println(404);
                }
            }
            }
 catch (IOException e) {
            e.printStackTrace();
        }

    }
}