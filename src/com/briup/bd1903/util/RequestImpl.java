package com.briup.bd1903.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述:请求信息类,封装所有请求以及和请求相关的方法
 *
 * @author WeiGu
 * @create 2019-08-29 9:22
 */
public class RequestImpl implements Request{
    private Socket socket;
    private String method;
    private String url;
    private String protocol;
    private Map<String,String> headMap;
    private Map<String,String> bodyMap;

    //接受数据,
    //数据拆分,分别放到不同的数据成员中
    public RequestImpl(Socket socket) {
        this.headMap = new HashMap<>();
        this.bodyMap = new HashMap<>();
        this.socket=socket;
        //1.2
        getInfos();
    }

    private void getInfos() {
        //readLine()读取到请求
        //while(!"".equals(readLine()))
        //post:if(br.ready()){正常读:放到bodyMap中}else{是get方法}
        BufferedReader br=null;
        try{
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = br.readLine();
            System.out.println("请求行:\t"+line);
            //解决 浏览器重复刷新出现的问题
            if(line == null) {
                return;
            }
            String[] larr = line.split(" ");
            if(larr.length != 3) {
                return;
            }
            method = larr[0];
            url = larr[1];
//            url=URLEncoder.encode(url,"UTF-8");
            if(url==null) return;
            protocol = larr[2];

            //第二部分 获取拆分请求头
            String hstr = null;
            while(!"".equals((hstr = br.readLine()))){
                System.out.println("+_+ "+hstr);
                String[] harr = hstr.split(": ");
                if(harr.length == 2) {
                    headMap.put(harr[0], harr[1]);
                }
            }

            //第三部分  拆分请求体
            if(br.ready()) {
                char[] buff = new char[1024];
                int size = br.read(buff);
                String bStr = new String(buff,0,size);
                System.out.println("bStr: "+bStr);
                //name=1&code=1
                //name=&code=
                parseBodyByStr(bStr);
            } else {
                //从url里面 拆分 body值
                String[] arr = url.split("[?]");
                //1. url不存在body
                if(arr.length == 1)
                    return;
                //2. url存在body  /a.txt?name=1&code=1
                //拆分 arr[1](name=1&code=1)
                parseBodyByStr(arr[1]);
                url = arr[0];
            }
        }catch (Exception e){}
    }

    private void parseBodyByStr(String bStr) {
        String[] barr = bStr.split("&");
        for (String s : barr) {
            String[] arr = s.split("=");
            if(arr.length != 2){
                bodyMap.put(arr[0], "");
            }else{
                bodyMap.put(arr[0], arr[1]);
            }
        }
    }


    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getFile() {
        return url;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public Map<String, String> getHead() {
        return headMap;
    }

    @Override
    public Map<String, String> getBody() {
        return bodyMap;
    }
}