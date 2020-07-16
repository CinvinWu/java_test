package com.cinvin.cases;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//Author:Cinvin
//标题
public class Register extends BaseCase{
    @Test(dataProvider="datas")
    public void register(CaseInfo caseInfo) throws Exception {
        //参数化
        paramReplace(caseInfo);
        //将请默认请求头放入headers集合中
        Map<String,String> headers=new HashMap<>();
        headers.putAll(Constants.headers);
        //调用接口
        HttpResponse response =DoHttp.call(caseInfo.getMethod(), caseInfo.getContentype(), caseInfo.getUrl(), caseInfo.getParams(),headers);
        String body = DoHttp.printResponse(response);
        //回写响应结果
        addWriteBackData(caseInfo.getId(),Constants.response_cellNum,body);
        //响应断言
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
    public static boolean sqlAssert(CaseInfo caseInfo,Object beforeSqlResult,Object afterSqlResult){
        boolean flag=false;
        if (StringUtils.isNotBlank(caseInfo.getSql())){
            if (beforeSqlResult==null||afterSqlResult==null){
                System.out.println("数据库断言失败");
            }else {
                Long c1=(long)beforeSqlResult;
                Long c2=(long)afterSqlResult;
                if (c1==0&&c2==1){
                    System.out.println("数据库断言成功");
                    flag=true;
                }
            }
        }else {
            System.out.println("sql为空");
        }
        return flag;
    }
    @DataProvider
    public Object[] datas() throws Exception {
        return DoExcel.read(0,1, CaseInfo.class);
    }
}
