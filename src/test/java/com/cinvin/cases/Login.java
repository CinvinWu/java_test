package com.cinvin.cases;

import com.alibaba.fastjson.JSONObject;
import com.cinvin.constants.Constants;
import com.cinvin.pojo.CaseInfo;
import com.cinvin.pojo.WriteBackData;
import com.cinvin.tools.Authorization;
import com.cinvin.tools.DoExcel;
import com.cinvin.tools.DoHttp;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.parser.JSONParser;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//Author:Cinvin
//标题
public class Login extends BaseCase {
    private static Logger logger=Logger.getLogger(Login.class);
    @Test(dataProvider= "datas")
    public void login(CaseInfo caseInfo) throws Exception {
        //参数化
        paramReplace(caseInfo);
        //添加请求头
        Map<String,String> headers=new HashMap<>();
        headers.putAll(Constants.headers);
        //获取响应体json字符串
        HttpResponse response = DoHttp.call(caseInfo.getMethod(),
                caseInfo.getContentype(),
                caseInfo.getUrl(),
                caseInfo.getParams(),
                headers);
        String body = DoHttp.printResponse(response);
        //将token和member_id存入VAS变量
        Authorization.json2Vars(body,"$.token","${token}");
        System.out.println(Authorization.VAS.get("${token}"));
        //创建WriteBackData对象并存入回写集合
        addWriteBackData(caseInfo.getId(),Constants.response_cellNum,body);
        //响应结果断言
        Boolean assertResponseFlag=assertResponse(caseInfo.getExpect(), body);
        //回写断言结果
        String assertResult=assertResponseFlag?"passed":"failed";
        addWriteBackData(caseInfo.getId(),Constants.assert_cellNum,assertResult);
        Assert.assertEquals(assertResult,"passed");
    }



    @DataProvider
    public Object[] datas() throws Exception {
        Object[] datas = DoExcel.read(sheetIndex, 1, CaseInfo.class);
        return datas;
    }
}
