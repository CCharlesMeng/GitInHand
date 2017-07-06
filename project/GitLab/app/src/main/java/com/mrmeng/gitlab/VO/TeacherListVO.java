package com.mrmeng.gitlab.VO;

import java.util.ArrayList;

/**
 * Created by mr.meng on 17/7/5.
 */
public class TeacherListVO {

    private int id;
    private String title;
    private String description;
    private String startAt;
    private String endAt;
    private int course;
    private String status;
    private String currentTime;

    public TeacherListVO(){

    }

    public ArrayList<TeacherListQuestionsVO> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<TeacherListQuestionsVO> questions) {
        this.questions = questions;
    }

    private ArrayList<TeacherListQuestionsVO> questions = new ArrayList<>();


    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}



