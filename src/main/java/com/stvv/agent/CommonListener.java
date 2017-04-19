package com.stvv.agent;

/**
 * Created by abhis on 3/18/2017.
 */

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class CommonListener extends RunListener {

	public void testStarted(Description description) {

		HashsetUtil.startClass("[TEST] " + description.getClassName() + ":" + description.getMethodName());

	}

	public void testFinished(Description description) {

		HashsetUtil.addToMap();
	}
	
	public void testRunFinished(Result result) {
		HashsetUtil.printLines();
	}
}
