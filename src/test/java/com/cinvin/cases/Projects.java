package com.cinvin.cases;

import com.alibaba.fastjson.JSONPath;
import com.cinvin.constants.Constants;
import com.cinvin.pojo.CaseInfo;
import com.cinvin.tools.Authorization;
import com.cinvin.tools.DoExcel;
import com.cinvin.tools.DoHttp;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Map;

//Author:Cinvin
//标题

/**
 * 接口鉴权流程：
 * 1.自定义一个静态变量VAS（map集合）
 * 2.登录成功后获取token值并存入map集合
 * 3.需要用到token时，从VAS中取出token值
 * 4.然后添加请求头
 */
public class Projects extends BaseCase{
    @Test(dataProvider= "datas")
    public void login(CaseInfo caseInfo) throws Exception {
        //参数化
        paramReplace(caseInfo);
        //获取带token的请求头
        Map<String, String> headers =Authorization.getTokenHeaders();
        //获取响应体json字符串
        HttpResponse response = DoHttp.call(caseInfo.getMethod(),
                caseInfo.getContentype(),
                caseInfo.getUrl(),
                caseInfo.getParams(),
                headers);
        String body = DoHttp.printResponse(response);
        //回写响应结果
        addWriteBackData(caseInfo.getId(),Constants.response_cellNum,body);
        //响应结果断言
        Boolean assertResponseFlag=assertResponse(caseInfo.getExpect(), body);
        //回写断言结果
        String assertResult=assertResponseFlag?"passed":"failed";
        addWriteBackData(caseInfo.getId(),Constants.assert_cellNum,assertResult);
        //报表断言
        Assert.assertEquals(assertResult,"passed");
    }
    @DataProvider
    public Object[] datas() throws Exception {
        Object[] datas = DoExcel.read(sheetIndex, 1, CaseInfo.class);
        return datas;
    }
}
