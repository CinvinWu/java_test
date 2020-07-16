package com.cinvin.cases;

import com.alibaba.fastjson.JSONPath;
import com.cinvin.constants.Constants;
import com.cinvin.pojo.CaseInfo;
import com.cinvin.tools.Authorization;
import com.cinvin.tools.DoExcel;
import com.cinvin.tools.DoHttp;
import com.cinvin.tools.SQLUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.*;

import java.math.BigDecimal;
import java.util.HashMap;
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
public class Configures extends BaseCase{
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


    /**
     *
     * @param caseInfo          caseinfo对象、
     * @param beforeSqlResult   sql前置查询结果
     * @param afterSqlResult    sql后置查询结果
     * @return
     */
    public boolean sqlAssert(CaseInfo caseInfo, Object beforeSqlResult, Object afterSqlResult) {
        boolean flag = false;
        if(StringUtils.isNotBlank(caseInfo.getSql())) {
            if (beforeSqlResult == null || afterSqlResult == null) {
                System.out.println("数据库断言失败");
            } else {
                BigDecimal b1 = (BigDecimal) beforeSqlResult;
                BigDecimal b2 = (BigDecimal) afterSqlResult;
                //充值后 - 充值前得到的结果  b2 - b1
                BigDecimal result1 = b2.subtract(b1);
                //jsonpath获取参数
                Object obj = JSONPath.read(caseInfo.getParams(),"$.amount");
                //参数amount
                BigDecimal result2 = new BigDecimal(obj.toString());
                //结果 == 参数amount
                System.out.println(b1);
                System.out.println(b2);
                System.out.println(result1);
                System.out.println(result2);
                if(result1.compareTo(result2) == 0) {
                    flag = true;
                    System.out.println("数据库断言成功");
                }else {
                    System.out.println("数据库断言失败");
                }
            }
        }else {
            System.out.println("sql为空，不需要数据库断言");
        }
        return flag;
    }
    @DataProvider
    public Object[] datas() throws Exception {
        Object[] datas = DoExcel.read(sheetIndex, 1, CaseInfo.class);
        return datas;
    }
}
