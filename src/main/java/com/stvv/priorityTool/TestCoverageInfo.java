package com.stvv.priorityTool;

import java.util.HashSet;

/**
 * Created by akumar on 4/21/2017.
 */
public class TestCoverageInfo {
    private String testName;
    private HashSet<String> coverage;
    private boolean isDone;

    public TestCoverageInfo() {
        isDone = false;
    }

    public TestCoverageInfo(String testName, HashSet<String> coverage) {
        this.testName = testName;
        this.coverage = coverage;
        isDone = false;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public HashSet<String> getCoverage() {
        return coverage;
    }

    public void setCoverage(HashSet<String> coverage) {
        this.coverage = coverage;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        return this.testName + " : " + this.coverage.size();
    }
}
