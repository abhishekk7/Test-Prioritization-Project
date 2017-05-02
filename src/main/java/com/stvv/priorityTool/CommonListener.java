package com.stvv.priorityTool;

/**
 * Created by abhis on 3/18/2017.
 */

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class CommonListener extends RunListener {

	public void testStarted(Description description) {
		HashsetUtil.startClass(description.getClassName() + ":" + description.getMethodName());
	}

	public void testFinished(Description description) {

		HashsetUtil.addToMap();
	}

	public void testRunFinished(Result result) {
		
		PriorityResults pr = HashsetUtil.printLines();
		
		System.out.println("Total Prioritization: ");
		for (String className : pr.getTotal())
			try {
				JUnitCore.runClasses(Class.forName(className));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		System.out.println("Additional Prioritization: ");
		for (String className : pr.getAdditional())
			try {
				JUnitCore.runClasses(Class.forName(className));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}
