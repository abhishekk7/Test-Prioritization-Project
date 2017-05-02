package com.stvv.priorityTool;

import java.io.BufferedWriter;
import java.io.File;
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
		File file = new File(filename+".txt");
		if(file.exists()) {
			file.delete();
		}
		try (BufferedWriter bw = new BufferedWriter(
				new FileWriter(filename + ".txt", true))) {
			/*bw.write("package org.joda.time;\n");
			bw.write("import java.util.Locale;\n");
			bw.write("import java.util.TimeZone;\n");
			bw.write("import junit.framework.Test;\n");
			bw.write("import junit.framework.TestCase;\n");
			bw.write("import junit.framework.TestSuite;\n");
			bw.write("public class " + filename + " extends TestCase {\n");
			bw.write("public " + filename + "(String testName) {\n");
			bw.write("super(testName);\n");
			bw.write("}\n");
			bw.write("public static Test suite() {\n");
			bw.write("TestSuite suite = new TestSuite();\n");
			bw.write("return suite;\n");
			bw.write("}\n");
			bw.write("public static void main(String args[]) {\n");
			bw.write("TimeZone.setDefault(TimeZone.getTimeZone(\"Asia/Seoul\"));\n");
			bw.write("Locale.setDefault(new Locale(\"th\", \"TH\"));\n");
			bw.write("String[] testCaseName = {\n");
			bw.write(filename + ".class.getName()\n");
			bw.write("};\n");
			bw.write("junit.textui.TestRunner.main(testCaseName);\n");
			bw.write("}\n");
			bw.write("}\n");
*/
			bw.write(line);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addToMap() {
		testCoverageInfos.add(new TestCoverageInfo(className, hashSet));
		hashSet = new HashSet<String>();
	}

	public static PriorityResults printLines() {
		StringBuilder line = new StringBuilder();
		PriorityResults pr = new PriorityResults();

		Collections.sort(testCoverageInfos, priorityComparator);

		getClassPriority();
		Iterator<String> iterator = cpMap.keySet().iterator();
		while (iterator.hasNext()) {
			String className = iterator.next();
			pr.getTotal().add(className);
			line.append(className + "\n");
		}
		writeToFile(line.toString(), "total_prioritization");
		line = new StringBuilder();

		while (cpMap.size() > 0) {
			getAddClassPriority();
			cpMap = sortByComparator(cpMap, false);
		}

		Iterator<ClassCoverageInfo> addIterator = addPriorityList.iterator();
		while (addIterator.hasNext()) {
			ClassCoverageInfo curr = addIterator.next();
			pr.getAdditional().add(curr.getClassName());
			line.append(curr.getClassName() + "\n");
		}

		writeToFile(line.toString(), "additional_prioritization");
		return pr;
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
