package com.cinvin.tools;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.cinvin.constants.Constants;
import com.cinvin.pojo.CaseInfo;
import com.cinvin.pojo.WriteBackData;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

//Author:Cinvin
//标题
public class DoExcel {
    private static Logger logger=Logger.getLogger(DoExcel.class);
    //批量回写集合
    public static List<WriteBackData> wbdList=new ArrayList<>();
    /**
     *用easypoi读取Excel
     * @param sheetIndex Excel sheet页的索引
     * @param sheetNum 一次读取sheet页的数量
     * @param clazz 对应sheet每行数据的对象
     * @return
     */
    public static Object[] read(int sheetIndex,int sheetNum,Class clazz){
        try {
            //输入流
//            InputStream fis=DoExcel.class.getClassLoader().getResourceAsStream("case_v1");
            FileInputStream fis=new FileInputStream(Constants.EXCEL_path);
            ImportParams params = new ImportParams();
            //从第sheetindex读取
            params.setStartSheetIndex(sheetIndex);
            //一次读取sheetNum个sheet
            params.setSheetNum(sheetNum);
            //importExcel文件流，映射字节码对象
            List<CaseInfo> list = ExcelImportUtil.importExcel(fis,clazz,params);
            //list转换为一维数组
            Object[] datas = list.toArray();
            fis.close();
            return datas;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //回写方法
    public static void writeBack(){
        FileInputStream fis=null;
        FileOutputStream fos=null;
        try {
            fis = new FileInputStream(Constants.EXCEL_path);
            Workbook excel = WorkbookFactory.create(fis);
            for (WriteBackData wbd : wbdList) {
                //获取需要回写的sheet页索引
                int sheetIndex=wbd.getSheetIndex();
                //获取需要回写的行号
                int rowNum=wbd.getRowNUm();
                //获取需要回写的列号
                int cellNum=wbd.getCellNum();
                //获取需要回写的内容
                String content=wbd.getContent();
                //选择sheet
                Sheet sheet = excel.getSheetAt(sheetIndex);
                //读取每一行
                Row row = sheet.getRow(rowNum);
                //读取没一个单元格，如果cell为空则返回一个空的cell对象
                Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellValue(content);
            }
            fos= new FileOutputStream(Constants.EXCEL_path);
            excel.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fis!=null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos!=null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        Object[] read = read(1, 1, CaseInfo.class);
        for (Object r : read) {
            System.out.println(r.toString());
        }
    }
}
