package com.anbang.qipai.wenzhoushuangkou.init;

import java.util.HashMap;
import java.util.Map;

public class XianshuCalculatorHelper {

	private static Map<String, Integer> xianshuCount;

	public void calculateXianshuBeishu() {
		long startTime = System.currentTimeMillis();
		System.out.println("开始计算线数倍数：");
		xianshuCount = new HashMap<>();
		long endTime = System.currentTimeMillis();
		System.out.println("线数倍数计算结束:" + (endTime - startTime));
	}
}
