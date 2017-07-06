package com.mrmeng.gitlab.VO.TeacherScore;

/**
 * Created by mr.meng on 17/7/5.
 */
public class QuestionInfo {
    private int id;
    private String title;
    private String description;
    private String type;

    public QuestionInfo(){

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
