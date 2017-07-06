package com.mrmeng.gitlab.VO.StudentAnalysis;

import java.util.ArrayList;

/**
 * Created by mr.meng on 17/7/5.
 */
public class AnalysisVO {
    private int studentId;
    private int assignmentId;
    private ArrayList<QuestionResult> questionResults;

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public ArrayList<QuestionResult> getQuestionResults() {
        return questionResults;
    }

    public void setQuestionResults(ArrayList<QuestionResult> questionResults) {
        this.questionResults = questionResults;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
