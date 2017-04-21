package com.stvv.priorityTool;

import java.util.HashSet;

/**
 * Created by akumar on 4/21/2017.
 */
public class ClassCoverageInfo {
    private String className;
    private HashSet<String> coverage;

    public ClassCoverageInfo() {

    }

    public ClassCoverageInfo(String className, HashSet<String> coverage) {
        this.className = className;
        this.coverage = coverage;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public HashSet<String> getCoverage() {
        return coverage;
    }

    public void setCoverage(HashSet<String> coverage) {
        this.coverage = coverage;
    }
}
