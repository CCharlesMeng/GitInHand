package com.mrmeng.gitlab.VO.StudentAnalysis;

import java.util.ArrayList;

/**
 * Created by mr.meng on 17/7/5.
 */
public class TestResult {
    private String git_url;
    private boolean compile_succeeded;
    private boolean tested;
    private ArrayList<TestCasesList> testcases;

    public boolean isCompile_succeeded() {
        return compile_succeeded;
    }

    public void setCompile_succeeded(boolean compile_succeeded) {
        this.compile_succeeded = compile_succeeded;
    }

    public String getGit_url() {
        return git_url;
    }

    public void setGit_url(String git_url) {
        this.git_url = git_url;
    }

    public ArrayList<TestCasesList> getTestcases() {
        return testcases;
    }

    public void setTestcases(ArrayList<TestCasesList> testcases) {
        this.testcases = testcases;
    }

    public boolean isTested() {
        return tested;
    }

    public void setTested(boolean tested) {
        this.tested = tested;
    }
}
