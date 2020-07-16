package com.cinvin.constants;

import com.cinvin.tools.DoExcel;

import javax.xml.xpath.XPath;
import java.util.HashMap;
import java.util.Map;

//Author:Cinvin
//标题
public class Constants {
    //EXCEL的路径
    public static final String EXCEL_path=getpath("\\src\\test\\resources\\cases_v1.xls");
    //properties的路径
    public  static final String prop_path=getpath("\\src\\test\\resources\\properties");
    //获取文件的绝对路径
    public static String getpath(String relative_path){
        String property = System.getProperty("user.dir");
        String path=property+relative_path;
        return path;
    }
    //自定义变量headers存放请求头
    public static final Map<String,String> headers=new HashMap<String, String>();
    //自定义响应回写的列号
    public static final int response_cellNum=8;
    //自定义断言回写的列号
    public static final int assert_cellNum=10;
    //数据库URL，USERNAME，PASSWORD
    public static final String JDBC_URL="jdbc:mysql://api.lemonban.com:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
    public static final String JDBC_USERNAME="future";
    public static final String JDBC_PASSWORD="123456";

}
