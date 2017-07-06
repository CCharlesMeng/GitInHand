package com.mrmeng.gitlab.VO.TeacherScore;

import java.util.ArrayList;

/**
 * Created by mr.meng on 17/7/5.
 */
public class TeacherScoreVO {
    private int assignmentId;
    private ArrayList<Questions> questions;

    public TeacherScoreVO(){

    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public ArrayList<Questions> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Questions> questions) {
        this.questions = questions;
    }
}
