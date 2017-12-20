package com.lubanjianye.biaoxuntong.bean;

import java.util.List;

/**
 * 项目名:   Lunioussky
 * 包名:     com.lubanjianye.biaoxuntong.bean
 * 文件名:   JsonString
 * 创建者:   lunious
 * 创建时间: 2017/11/6  17:14
 * 描述:     TODO
 */

public class JsonString {

    private List<Object> zyIds = null;
    private String lxId = null;
    private String zcd = null;
    private String djId = null;
    private String provinceCode = null;
    private int entrySign = 0;

    public List<Object> getZyIds() {
        return zyIds;
    }

    public void setZyIds(List<Object> zyIds) {
        this.zyIds = zyIds;
    }

    public String getLxId() {
        return lxId;
    }

    public void setLxId(String lxId) {
        this.lxId = lxId;
    }

    public String getZcd() {
        return zcd;
    }

    public void setZcd(String zcd) {
        this.zcd = zcd;
    }

    public String getDjId() {
        return djId;
    }

    public void setDjId(String djId) {
        this.djId = djId;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getEntrySign() {
        return entrySign;
    }

    public void setEntrySign(int entrySign) {
        this.entrySign = entrySign;
    }
}
