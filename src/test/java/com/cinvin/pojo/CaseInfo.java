package com.cinvin.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

//Author:Cinvin
//标题
public class CaseInfo {
    @Excel(name = "CaseId(用例编号)")
    private int id;
    @Excel(name ="Name(接口名)" )
    private String name;
    @Excel(name = "Url(接口地址)")
    private String url;
    @Excel(name = "Type(接口提交类型)")
    private String method;
    @Excel(name = "Params(参数)")
    private String params;
    @Excel(name = "Content-Type")
    private String contentype;
    @Excel(name = "期望结果")
    private String expect;
    @Excel(name = "sql")
    private String sql;

    public CaseInfo() {
    }

    public CaseInfo(int id, String name, String url, String method, String params, String contentype, String expect, String sql) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.method = method;
        this.params = params;
        this.contentype = contentype;
        this.expect = expect;
        this.sql = sql;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getContentype() {
        return contentype;
    }

    public void setContentype(String contentype) {
        this.contentype = contentype;
    }

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return "CaseInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", params='" + params + '\'' +
                ", contentype='" + contentype + '\'' +
                ", expect='" + expect + '\'' +
                ", sql='" + sql + '\'' +
                '}';
    }
}
