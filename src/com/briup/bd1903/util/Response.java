package com.briup.bd1903.util;

/**
 * 描述:
 *
 * @author WeiGu
 * @create 2019-08-30 9:16
 */
public interface Response {
    String getResLine();//响应行
    String getResHeader(String attr,String value);//响应头
    void sendResBody() throws Exception;
    void sendReqResource() throws Exception;
}
