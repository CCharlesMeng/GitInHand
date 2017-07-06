package com.mrmeng.gitlab.VO;

/**
 * Created by mr.meng on 17/6/18.
 */
public class ClassVO {
    private String name;
    private int id;

    public ClassVO(String name, int id){
        this.name=name;
        this.id=id;
    }

    public ClassVO(){

    }

    public void setName(String name){
        this.name=name;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }
}
