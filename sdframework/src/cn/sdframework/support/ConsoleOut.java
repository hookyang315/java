package cn.sdframework.support;

import cn.sdframework.config.ApplicationParam;

public class ConsoleOut {
	
	private static boolean log = ApplicationParam.printsql;
	
	public static void print(String string){
		if(log)
			System.err.println(string);
	}
}
