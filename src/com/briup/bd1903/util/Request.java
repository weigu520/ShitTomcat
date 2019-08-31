package com.briup.bd1903.util;

import java.util.Map;

/**
 * 描述:
 *
 * @author WeiGu
 * @create 2019-08-29 9:19
 */
public interface Request {
    String getMethod();//获取请求方法
    String getFile();//获取资源文件
    String getProtocol();//获取协议版本
    Map<String,String> getHead();//请求头
    Map<String,String> getBody();//请求正文
}
