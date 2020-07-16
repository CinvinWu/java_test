package com.cinvin.cases;

import com.alibaba.fastjson.JSONObject;
import com.cinvin.constants.Constants;
import com.cinvin.pojo.CaseInfo;
import com.cinvin.pojo.WriteBackData;
import com.cinvin.tools.Authorization;
import com.cinvin.tools.DoExcel;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

//Author:Cinvin
//标题
public class BaseCase {
    private static Logger logger=Logger.getLogger(BaseCase.class);
    public int sheetIndex;
    @BeforeSuite
    public void init(){
//        Constants.headers.put("X-Lemonban-Media-Type","lemonban.v2");
        Constants.headers.put("content-Type","application/json");
//        Authorization.VAS.put("${register_mb}","13788888989");
//        Authorization.VAS.put("${login_mb}","13788888989");
//        Authorization.VAS.put("${amount}","13788888989");
        //获取properties里的参数并存到VAS里
        Properties prop= new Properties();
        FileInputStream fis= null;
        try {
            fis = new FileInputStream(Constants.prop_path);
            prop.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Authorization.VAS.putAll((Map) prop);
        System.out.println(Authorization.VAS);
    }
    @BeforeClass
    @Parameters({"sheetIndex"})//测试class之前执行，传入读取excel的索引sheet
    public void beforeClass(int sheetIndex){
        this.sheetIndex=sheetIndex;
    }
    
    @AfterSuite
    public void finish(){
        DoExcel.writeBack();
        logger.info("========================Finish========================");
    }

    /**
     *
     * @param rowNum 行号
     * @param cellNum 回写列号
     * @param body 回写内容
     */
    public void addWriteBackData(int rowNum, int cellNum,String body) {
        WriteBackData wbd= new WriteBackData(rowNum, cellNum,sheetIndex,body);
        DoExcel.wbdList.add(wbd);
    }

    /**
     *
     * @param expect 期望结果
     * @param body 响应体字符串
     */
    public static Boolean assertResponse(String expect, String body) {
        //转换成map集合
        Map<String,Object> map = JSONObject.parseObject(expect, Map.class);
        //获取所有的key
        Set<String> keySet = map.keySet();
        Boolean assertResponseFlag=true;
        for (String expression : keySet) {
            //根据key获取期望值
            Object expectvalue=map.get(expression);
            //根据期望结果的key也就是jsonpath表达式从body取出实际结果
            Object actualValue= JsonPath.read(body,expression);
            //比较实际值和期望值
            if (expectvalue==null&&actualValue!=null) {
                assertResponseFlag = false;
                break;
            }
            if (expectvalue==null&&actualValue==null){
                assertResponseFlag=false;
                continue;
            }
            if (!expectvalue.equals(actualValue)){
                assertResponseFlag=false;
                break;
            }
        }
        System.out.println(assertResponseFlag);
        //返回断言结果
        return assertResponseFlag;
    }

    /**
     * 参数化替换方法
     * @param caseInfo
     */
    public void paramReplace(CaseInfo caseInfo) {
        Set<String> keySet = Authorization.VAS.keySet();
        for (String key : keySet) {
            String value = Authorization.VAS.get(key).toString();
            //替换URL
            if (StringUtils.isNotBlank(caseInfo.getUrl())){
                String url = caseInfo.getUrl().replace(key, value);
                caseInfo.setUrl(url);
            }
            //替换params
            if (StringUtils.isNotBlank(caseInfo.getParams())){
                String params = caseInfo.getParams().replace(key, value);
                caseInfo.setParams(params);
            }
            //替换sql
            if (StringUtils.isNotBlank(caseInfo.getSql())){
                String sql = caseInfo.getSql().replace(key, value);
                caseInfo.setSql(sql);
            }
            //替换expect
            if (StringUtils.isNotBlank(caseInfo.getExpect())){
                String expect = caseInfo.getExpect().replace(key, value);
                caseInfo.setExpect(expect);
            }
        }
    }
}
