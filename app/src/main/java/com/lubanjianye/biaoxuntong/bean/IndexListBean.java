package com.lubanjianye.biaoxuntong.bean;

import java.io.Serializable;

/**
 * 项目名:   Lunious
 * 包名:     com.lubanjianye.biaoxuntong.bean
 * 文件名:   IndexListBean
 * 创建者:   lunious
 * 创建时间: 2017/11/29  23:22
 * 描述:     TODO
 */

public class IndexListBean implements Serializable{

    private int id = 0;
    private String entryName = null;
    private String type = null;
    private String entity = null;
    private int entityId = 0;
    private String sysTime = null;
    private String deadTime = null;
    private String signstauts = null;
    private String address = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public String getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(String deadTime) {
        this.deadTime = deadTime;
    }

    public String getSignstauts() {
        return signstauts;
    }

    public void setSignstauts(String signstauts) {
        this.signstauts = signstauts;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "IndexListBean{" +
                "id=" + id +
                ", entryName='" + entryName + '\'' +
                ", type='" + type + '\'' +
                ", entity='" + entity + '\'' +
                ", entityId=" + entityId +
                ", sysTime='" + sysTime + '\'' +
                ", deadTime='" + deadTime + '\'' +
                ", signstauts='" + signstauts + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
