package com.mrmeng.gitlab.VO.TeacherScore;

import java.util.ArrayList;

/**
 * Created by mr.meng on 17/7/5.
 */
public class Questions {
    private QuestionInfo questionInfo;
    private ArrayList<Students> students;

    public Questions(){

    }

    public QuestionInfo getQuestionInfo() {
        return questionInfo;
    }

    public void setQuestionInfo(QuestionInfo questionInfo) {
        this.questionInfo = questionInfo;
    }

    public ArrayList<Students> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Students> students) {
        this.students = students;
    }
}
