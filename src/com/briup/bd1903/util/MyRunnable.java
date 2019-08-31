package com.briup.bd1903.util;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;

/**
 * 描述:
 *
 * @author WeiGu
 * @create 2019-08-28 9:35
 */
public class MyRunnable implements Runnable{
    private Socket socket;
    private String path;
    private String errorFile;
    private String welFile;

    public MyRunnable(Socket socket) {
        this.socket = socket;
        this.path = PropFinder.path();
        this.errorFile = PropFinder.errorFile();
        this.welFile = PropFinder.welFile();
    }

    @Override
    public void run() {

        RequestImpl request = new RequestImpl(socket);
        String url = request.getFile();

        if(url==null) return;


        if("/favicon.ico".equals(url)){
            return;
        }

        System.out.println("getMethod:"+request.getMethod());
        System.out.println("getUrl:"+request.getFile());
        System.out.println("getProtocol:"+request.getProtocol());
        System.out.println("getHead:"+request.getHead());
        System.out.println("getBody:"+request.getBody());

//        sendResponse(url);
            ResponseImpl response = new ResponseImpl(url, socket,request);
            try {
                response.sendReqResource();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public String getUrl(){
        String fileName=null;
        try {
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is/*,"UTF-8"*/);
            BufferedReader br = new BufferedReader(isr);
            String line=br.readLine();
//            System.out.println(line);
            //按照空格拆分请求行  GET /a.txt HTTP/1.1
            String[] array = line.split(" ");
            //干掉斜杆
            fileName = array[1];
//            System.out.println(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public void sendResponse(String fileName){
        OutputStream os = null;
        PrintStream ps = null;
        FileInputStream fis =null ;
        BufferedInputStream bis =null ;
        String newFileName = fileName.substring(1);
        try {
            //判断文件是否存在
            File file = new File(path, URLDecoder.decode(newFileName,"UTF-8"));
            boolean flag = file.exists();

            //准备流
            os = socket.getOutputStream();
            ps = new PrintStream(os);

            //准备响应行
            String responseLine = null;
            if (flag) {
                responseLine = "HTTP/1.1 200 OK";
//                String[] split = fileName.split(" ");
                //判断是否为空白资源
                if ("/".equals(newFileName)){
                    file = new File(path, welFile);
                }
            } else {
//                responseLine = "HTTP/1.1 404 Not Found";
                responseLine = "HTTP/1.1 200 ok";
                //System.out.println("没有此文件");
                file = new File(path, errorFile);
                //System.out.println("123213213");
            }
            //发送响应行
            ps.println(responseLine);
            //消息头（为空）
            //空行
            ps.println();

            //消息体
            //额外准备流，提取文件内容
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            byte[] arr = new byte[1024*4*80];
            int size = 0;

            while ((size = bis.read(arr)) != -1) {
                ps.write(arr, 0, size);
            }
            ps.close();
            bis.close();
            socket.close();
        }catch (IOException e){
        }
    }


}