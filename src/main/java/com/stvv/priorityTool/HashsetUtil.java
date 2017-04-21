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
    private static ArrayList<TestCoverageInfo> addPriorityList = new ArrayList<>();
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

    public static void writeToFile(String line) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("stmt-cov.txt", true))) {
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

        line.append("***********************************\n");
        line.append("Test Prioritization\n");
        line.append("***********************************\n");
        Collections.sort(testCoverageInfos, priorityComparator);
        Iterator<TestCoverageInfo> iterator = testCoverageInfos.iterator();
        while (iterator.hasNext()) {
            line.append(iterator.next() + "\n");
        }

        line.append("***********************************\n");
        line.append("Additive Prioritization\n");
        line.append("***********************************\n");

        while (testCoverageInfos.size() > 0) {
            performAdditivePriority();
            Collections.sort(testCoverageInfos, priorityComparator);
        }

        for (TestCoverageInfo value : addPriorityList) {
            line.append(value + "\n");
        }

        writeToFile(line.toString());
    }

    private static void performAdditivePriority() {
        TestCoverageInfo curr = testCoverageInfos.get(0);
        testCoverageInfos.remove(0);
        addPriorityList.add(curr);
        hashSet.addAll(curr.getCoverage());
        Iterator<TestCoverageInfo> iterator = testCoverageInfos.iterator();
        while (iterator.hasNext()) {
            curr = iterator.next();
            curr.getCoverage().removeAll(hashSet);
        }
    }

    /*private static Map<String, HashSet<String>> sortByComparator(Map<String, HashSet<String>> unsortMap,
                                                                 final boolean order) {

        List<Entry<String, HashSet<String>>> list = new LinkedList<Entry<String, HashSet<String>>>(
                unsortMap.entrySet());
		*//*
         * Integer val1 = new Integer(-1); Integer val2 = new Integer(-1);
		 *//*// Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, HashSet<String>>>() {
            public int compare(Entry<String, HashSet<String>> o1, Entry<String, HashSet<String>> o2) {
                Integer val1 = (Integer) o1.getValue().size();
                Integer val2 = (Integer) o2.getValue().size();
                if (order) {
                    return val1.compareTo(val2);
                } else {
                    return val2.compareTo(val1);

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, HashSet<String>> sortedMap = new LinkedHashMap<String, HashSet<String>>();
        for (Entry<String, HashSet<String>> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }*/
}
