package com.stvv.priorityTool;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HashsetUtil {
	private static HashSet<String> hashSet = new HashSet<String>();
	private static Map<String, HashSet<String>> map = new HashMap<>();
	private static String className;
	private static ArrayList<String> addPriorityList = new ArrayList<>();

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
		map.put(className, hashSet);
		hashSet = new HashSet<String>();
	}

	public static void printLines() {
		StringBuilder line = new StringBuilder();
		/*
		 * line.append("***********************************\n");
		 * line.append("Test Prioritization\n");
		 * line.append("***********************************\n");
		 */map = sortByComparator(map, false);
		/*
		 * for (String key : map.keySet()) { line.append(key + " : " +
		 * map.get(key).size() + "\n"); }
		 */
		line.append("***********************************\n");
		line.append("Additive Prioritization\n");
		line.append("***********************************\n");

		while (map.size() > addPriorityList.size()) {
			performAdditivePriority();
			map = sortByComparator(map, false);
		}

		for (String value : addPriorityList) {
			line.append(value + "\n");
		}

		writeToFile(line.toString());
	}

	private static Map<String, HashSet<String>> performAdditivePriority() {
		int count = 0;
		for (String key : map.keySet()) {
			if (hashSet.size() == 0 && map.get(key).size() > 0) {
				hashSet.addAll(map.get(key));
				addPriorityList.add(key);
				map.get(key).clear();
				System.out.println(++count);// map.remove(key);
			} else {
				Iterator<String> superSet = hashSet.iterator();
				while (superSet.hasNext()) {
					String curr = superSet.next();
					if (map.get(key).contains(curr)) {
						map.get(key).remove(curr);
					}
				}
			}
		}
		hashSet.clear();
		return map;
	}

	private static Map<String, HashSet<String>> sortByComparator(Map<String, HashSet<String>> unsortMap,
			final boolean order) {

		List<Entry<String, HashSet<String>>> list = new LinkedList<Entry<String, HashSet<String>>>(
				unsortMap.entrySet());
		/*
		 * Integer val1 = new Integer(-1); Integer val2 = new Integer(-1);
		 */// Sorting the list based on values
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
	}
}
