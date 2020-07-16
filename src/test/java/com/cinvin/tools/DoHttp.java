package com.cinvin.tools;

import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.ClientInfoStatus;
import java.util.*;


//Author:Cinvin
//url带上参数
public class DoHttp {
    private static Logger logger=Logger.getLogger(DoHttp.class);
    /**
     *
     * @param method 请求方式
     * @param content_type 参数类型
     * @param url 请求地址
     * @param params 请求参数
     * @param headers 请求头
     * @throws Exception
     */
    public static HttpResponse call(String method,String content_type,String url,String params,Map<String,String> headers) throws Exception {
        try {
            if ("get".equalsIgnoreCase(method)){
                if ("json".equalsIgnoreCase(content_type)){
                    return DoHttp.get(url,headers);
                }
            }else if ("post".equalsIgnoreCase(method)){
                if("json".equalsIgnoreCase(content_type)){
                    return DoHttp.post(url,params,headers);
                }else if ("x-www-form-urlencoded".equalsIgnoreCase(content_type)){
                    return post(url, jsonkevalue(params), headers);
                }
            }else if ("patch".equalsIgnoreCase(method)){
                return DoHttp.patch(url,params,headers);
            }else{
                System.out.println("method = " + method + ", contentType = " + content_type + ", url = " + url + ", params = " + params);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String jsonkevalue(String jsonstr){
        //把json转成map
        Map<String,String> map = JSONObject.parseObject(jsonstr, Map.class);
        //获取所有的key
        Set<String> keySet = map.keySet();
        String result= "";
        for (String key : keySet) {
            String value=map.get(key);
            result+=key+value+"&";
        }
        //去掉最后的&
        result.substring(0,result.length()-1);
        return result;
    }

    /**
     * 添加请求头
     * @param headers 存放请求头的列表
     * @param request 请求类型
     */
    public static void addHheaders(Map<String,String> headers, HttpRequest request){
        Set<String> keySet = headers.keySet();
        for (String key : keySet) {
            String value = headers.get(key);
            request.addHeader(key,value);
        }
    }

    /**
     *
     * @param url 请求地址，带上参数
     * @return 返回响应体
     * @throws Exception
     */
    public static HttpResponse get(String url,Map headers) throws Exception{
        HttpGet get=new HttpGet(url);//创建get请求
//        get.addHeader("X-Lemonban-Media-Type","lemonban.v1");//添加请求头
//        get.addHeader("content-Type","application/json");
        addHheaders(headers,get);
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(get);
        return response;
    }
    public static HttpResponse  post(String url,String params,Map<String,String> headers) throws IOException {
        HttpPost post=new HttpPost(url);
//        post.addHeader("X-Lemonban-Media-Type","lemonban.v2");//添加请求头
//        post.addHeader("content-Type","application/x-www-form-urlencoded");
//        post.addHeader("Authorization","Bearer"+" "+token);
        addHheaders(headers,post);
        HttpClient client=HttpClients.createDefault();//创建客户端
        StringEntity data=new StringEntity(params,"UTF-8");//请求体对象
        post.setEntity(data);//
        HttpResponse response = client.execute(post);
        return response;
    }

    public static HttpResponse patch(String url,String params,Map<String,String> headers) throws IOException {
        HttpPatch patch=new HttpPatch(url);
//        patch.addHeader("X-Lemonban-Media-Type","lemonban.v2");//添加请求头
//        patch.addHeader("content-Type","application/json");
//        patch.addHeader("Authorization","Bearer"+" "+token);
        addHheaders(headers,patch);
        HttpClient client=HttpClients.createDefault();//创建客户端
        StringEntity data=new StringEntity(params,"UTF-8");//请求体对象
        patch.setEntity(data);//
        HttpResponse response = client.execute(patch);
        return response;
    }
    public static String printResponse(HttpResponse httpResponse) throws IOException {//格式化输出参数
        try {
            Header[] allHeaders = httpResponse.getAllHeaders();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            HttpEntity entity = httpResponse.getEntity();
            String body = EntityUtils.toString(entity);
            logger.info(Arrays.toString(allHeaders));
            logger.info(statusCode);
            logger.info(body);
            return body;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
    public static void main(String[] args) throws Exception {
//        String url="http://api.keyou.site:8000/configures/?page=1&size=1&ordering=1";
//        Map<String,String> headers=new HashMap<>();
//        headers.put("content-Type","application/json");
//        headers.put("Authorization","JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo4LCJ1c2VybmFtZSI6IjEyMzQ1Njc4OTEwIiwiZXhwIjoxNTk0MzU1NTM1LCJlbWFpbCI6IjEwNzI1NzY3MDFAcXEuY29tIn0.4PFsW4nQ4aULUHwYZqZz89nKfgJO5G4L903w2hzJFPQ");
//        HttpResponse response = get(url, headers);
//        printResponse(response);
    }
}
