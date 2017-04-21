package com.stvv.priorityTool;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class HashsetUtil {
    private static HashSet<String> hashSet = new HashSet<String>();
    private static String className;
    private static ArrayList<TestCoverageInfo> testCoverageInfos = new ArrayList<>();
    private static ArrayList<ClassCoverageInfo> addPriorityList = new ArrayList<>();
    private static Map<String, ClassCoverageInfo> cpMap = new HashMap<>();
    private static Comparator<TestCoverageInfo> priorityComparator = new Comparator<TestCoverageInfo>() {
        @Override
        public int compare(TestCoverageInfo o1, TestCoverageInfo o2) {
            return o2.getCoverage().size() - o1.getCoverage().size();
        }
    };

    public static void startClass(String text) {
        className = text;
    }

    public static void addLine(String line) {
        hashSet.add(line);
    }

    public static void writeToFile(String line, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename + ".txt", true))) {
            bw.write(line + " \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addToMap() {
        testCoverageInfos.add(new TestCoverageInfo(className, hashSet));
        hashSet = new HashSet<String>();
    }

    public static void printLines() {
        StringBuilder line = new StringBuilder();

        Collections.sort(testCoverageInfos, priorityComparator);

        getClassPriority();
        Iterator<String> iterator = cpMap.keySet().iterator();
        while (iterator.hasNext()) {
            String className = iterator.next();
            line.append(className + " : " + cpMap.get(className).getCoverage().size() + "\n");
        }
        writeToFile(line.toString(), "total");
        line = new StringBuilder();

        while (cpMap.size() > 0) {
            getAddClassPriority();
            cpMap = sortByComparator(cpMap, false);
        }

        Iterator<ClassCoverageInfo> addIterator = addPriorityList.iterator();
        while (addIterator.hasNext()) {
            ClassCoverageInfo curr = addIterator.next();
            line.append(curr.getClassName() + " : " + curr.getCoverage().size() + "\n");
        }

        writeToFile(line.toString(), "additive");
    }

    private static void getAddClassPriority() {
        ClassCoverageInfo curr;
        Iterator<String> strIterator = cpMap.keySet().iterator();
        if (strIterator.hasNext()) {
            curr = cpMap.get(strIterator.next());
            hashSet.addAll(curr.getCoverage());
            addPriorityList.add(curr);
            cpMap.remove(curr.getClassName());
        }
        Iterator<Entry<String, ClassCoverageInfo>> iterator = cpMap.entrySet().iterator();
        while (iterator.hasNext()) {
            curr = iterator.next().getValue();
            curr.getCoverage().removeAll(hashSet);
        }
    }

    private static void getClassPriority() {
        Iterator<TestCoverageInfo> iterator = testCoverageInfos.iterator();
        while (iterator.hasNext()) {
            TestCoverageInfo curr = iterator.next();
            String className = curr.getTestName().split(":")[0];
            ClassCoverageInfo coverageInfo;
            if (cpMap.containsKey(className)) {
                coverageInfo = cpMap.get(className);
                HashSet<String> temp = coverageInfo.getCoverage();
                temp.addAll(curr.getCoverage());
                cpMap.put(className, coverageInfo);
            } else {
                coverageInfo = new ClassCoverageInfo(className, curr.getCoverage());
                cpMap.put(className, coverageInfo);
            }
        }
        cpMap = sortByComparator(cpMap, false);
    }

    private static Map<String, ClassCoverageInfo> sortByComparator(Map<String, ClassCoverageInfo> unsortMap,
                                                                   final boolean order) {

        List<Entry<String, ClassCoverageInfo>> list = new LinkedList<Entry<String, ClassCoverageInfo>>(
                unsortMap.entrySet());

        Integer val1 = new Integer(-1);
        Integer val2 = new Integer(-1);
        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, ClassCoverageInfo>>() {
            public int compare(Entry<String, ClassCoverageInfo> o1, Entry<String, ClassCoverageInfo> o2) {
                Integer val1 = (Integer) o1.getValue().getCoverage().size();
                Integer val2 = (Integer) o2.getValue().getCoverage().size();
                if (order) {
                    return val1.compareTo(val2);
                } else {
                    return val2.compareTo(val1);

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, ClassCoverageInfo> sortedMap = new LinkedHashMap<String, ClassCoverageInfo>();
        for (Entry<String, ClassCoverageInfo> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
