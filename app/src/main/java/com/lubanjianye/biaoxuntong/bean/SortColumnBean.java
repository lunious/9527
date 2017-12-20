package com.lubanjianye.biaoxuntong.bean;

/**
 * 项目名:   Lunious
 * 包名:     com.lubanjianye.biaoxuntong.fragment_column
 * 文件名:   SortColumnBean
 * 创建者:   lunious
 * 创建时间: 2017/11/30  23:28
 * 描述:     TODO
 */

public class SortColumnBean {
    private int id = 0;
    private String name = null;
    private boolean isShowDele = false;
    private boolean isChangeColo = false;

    public boolean isShowDele() {
        return isShowDele;
    }

    public void setShowDele(boolean showDele) {
        isShowDele = showDele;
    }

    public boolean isChangeColo() {
        return isChangeColo;
    }

    public void setChangeColo(boolean changeColo) {
        isChangeColo = changeColo;
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

    @Override
    public String toString() {
        return "SortColumnBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isShowDele=" + isShowDele +
                ", isChangeColo=" + isChangeColo +
                '}';
    }
}
