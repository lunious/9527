package com.lubanjianye.biaoxuntong.bean;

/**
 * 项目名:   Lunious
 * 包名:     com.lubanjianye.biaoxuntong.bean
 * 文件名:   CompanyXyjcListBean
 * 创建者:   lunious
 * 创建时间: 2017/11/30  14:03
 * 描述:     TODO
 */

public class CompanyXyjcListBean {

    private String publishDate = null;
    private String info = null;

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "CompanyXyjcListBean{" +
                "publishDate='" + publishDate + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
