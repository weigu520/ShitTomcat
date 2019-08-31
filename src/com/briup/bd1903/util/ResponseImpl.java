package com.briup.bd1903.util;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;

/**
 * 描述:
 *
 * @author WeiGu
 * @create 2019-08-30 9:18
 */
public class ResponseImpl implements Response{
    private String url;
    private Socket socket;
    private PrintStream ps;
    private RequestImpl request;

    public ResponseImpl(String url, Socket socket,RequestImpl request) {
        try {
            this.request=request;
        this.url = url;
        this.socket = socket;
        OutputStream os = socket.getOutputStream();
        this.ps = new PrintStream(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getResLine() {
        //准备响应行 "HTTP/1.1 200 OK";
//        String responseLine = null;

//        //协议
//        RequestImpl request = new RequestImpl(socket);
        String protocol = this.request.getProtocol();
        //空格
        String nul=" ";

        //获取状态码
        int statusCode = getStatusCode(url);// /a.txt

        //状态码信息
        String statu_code_mes = PropFinder.getStatus_code_mes(statusCode);

        return protocol+nul+statusCode+nul+statu_code_mes;
    }

    private int getStatusCode(String url){
//        String newFileName = url.substring(1);
        File f = null;
        this.url=url.substring(1);
        f = new File(PropFinder.path(), this.url);

        if(f.exists()) {
            //a.txt   / == welcome.html
            if("/".equals(this.url))
                this.url = PropFinder.welFile();

            return 200;
        }

        this.url = PropFinder.errorFile();
        return 404;
    }

    @Override
    public String getResHeader(String attr, String value) {
        String[] arr = url.split("[.]");
        //a.txt .txt
        String name = arr[arr.length-1];

        if(arr.length < 2) {
            System.out.println("length<2");
            return null;
        }

        //根据后缀名获取 文件传输类型
        String typeValue = PropFinder.getMime(name);

        if(typeValue == null){
            System.out.println("getMime is null!");
            return null;
        }
        System.out.println("getMime: "+ typeValue);

        return "Content-Type: " + typeValue;
    }
    //空行,ps.println()
    @Override
    public void sendResBody() throws Exception {
        File file = new File(PropFinder.path(),URLDecoder.decode(url,"UTF-8"));
        BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(file));
        byte[] array = new byte[1024*4*80];
        int size = 0;
        //读取数据然后发送到浏览器
        while((size = bis.read(array)) != -1) {
            ps.write(array, 0, size);
            ps.flush();
            if(size==-1) return;
        }
        bis.close();
    }


    @Override
    public void sendReqResource() throws Exception {
        //发送响应行
        String resLine = getResLine();
        ps.println(resLine);

        //发送响应头
        String resHeader = getResHeader("", "");
        ps.println(resHeader);

        //发送空行
        ps.println();
        ps.flush();

        //发送响应体
        sendResBody();
    }
}