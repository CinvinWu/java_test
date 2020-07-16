package com.cinvin.tools;

import bsh.StringUtil;
import com.cinvin.constants.Constants;
import com.cinvin.pojo.CaseInfo;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//Author:Cinvin
//标题
public class Authorization {
    //定义用户变量VAS
    public static Map<String,Object> VAS=new HashMap<String, Object>();

    /**
     * 将响应体里取出需要的数据存在自定义变量Vas里
     * @param body 响应体字符串
     * @param expression Json表达式
     * @param key 存储到VAS中的key
     */
    public static void json2Vars(String body,String expression,String key){
        if(StringUtils.isNotBlank(body)) {
            try {
                Object value = JsonPath.read(body, expression);
                if (value != null) {
                    Authorization.VAS.put(key, value);
                }
            }catch (Exception e){
                System.out.println("");
            }
        }
    }
    public static Map<String, String> getTokenHeaders() {
        //获取token
        Object token = Authorization.VAS.get("${token}");
        Map<String,String> headers=new HashMap<>();
        //添加token请求头到headers
        headers.put("Authorization","JWT "+token);
        //添加默认请求头到headers集合
        headers.putAll(Constants.headers);
        return headers;
    }
}
