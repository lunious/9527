package com.lubanjianye.biaoxuntong.bean;

import java.io.Serializable;

/**
 * 项目名:   Lunioussky
 * 包名:     com.lubanjianye.biaoxuntong.bean
 * 文件名:   CollectionListBean
 * 创建者:   lunious
 * 创建时间: 2017/11/29  14:25
 * 描述:     TODO
 */

public class CollectionListBean implements Serializable {

    private int id = 0;
    private String entryName = null;
    private String type = null;
    private String entity = null;
    private int entityId = 0;
    private String sysTime = null;
    private String address = null;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

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

    @Override
    public String toString() {
        return "CollectionListBean{" +
                "id=" + id +
                ", entryName='" + entryName + '\'' +
                ", type='" + type + '\'' +
                ", entity='" + entity + '\'' +
                ", entityId=" + entityId +
                ", sysTime='" + sysTime + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
