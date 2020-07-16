package com.cinvin.pojo;

//Author:Cinvin
//标题
public class WriteBackData {
    int rowNUm;
    int cellNum;
    int sheetIndex;
    String content;

    public WriteBackData() {
    }

    public WriteBackData(int rowNUm, int cellNum, int sheetIndex, String content) {
        this.rowNUm = rowNUm;
        this.cellNum = cellNum;
        this.sheetIndex = sheetIndex;
        this.content = content;
    }

    public int getRowNUm() {
        return rowNUm;
    }

    public void setRowNUm(int rowNUm) {
        this.rowNUm = rowNUm;
    }

    public int getCellNum() {
        return cellNum;
    }

    public void setCellNum(int cellNum) {
        this.cellNum = cellNum;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "WriteBackData{" +
                "rowNUm=" + rowNUm +
                ", cellNum=" + cellNum +
                ", sheetIndex=" + sheetIndex +
                ", content='" + content + '\'' +
                '}';
    }
}
