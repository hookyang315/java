package cn.sdframework.config;

import java.util.ResourceBundle;

public class ApplicationParam {
	
	public static String dbType;
	public static String driverClass;
	public static String url;
	public static String username;
	public static String password;
	public static String initialSize;
	public static String maxActive;
	public static String minIdle;
	public static String maxIdle;
	public static String maxWait;
	public static boolean printsql = true;
	public static String test="test";
	
	static{
		ResourceBundle bundle = ResourceBundle.getBundle("app");
		dbType = bundle.getString("app.jdbc.dbType");
		driverClass = bundle.getString("app.jdbc.driverClass");
		url = bundle.getString("app.jdbc.url");
		username = bundle.getString("app.jdbc.username");
		password = bundle.getString("app.jdbc.password");
		initialSize = bundle.getString("app.jdbc.initialSize");
		maxActive = bundle.getString("app.jdbc.maxActive");
		minIdle = bundle.getString("app.jdbc.minIdle");
		maxIdle = bundle.getString("app.jdbc.maxIdle");
		maxWait = bundle.getString("app.jdbc.maxWait");
		printsql = Boolean.valueOf(bundle.getString("data.printsql"));
	}
}
