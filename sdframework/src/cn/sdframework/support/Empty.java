package cn.sdframework.support;

import java.util.List;

/**
 * 判空工具类
 */
public final class Empty {

	/**
	 * 判断字符串是否为空，长度为0被认为是空字符串.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 判断字符串是否为空，字符串前后空白被截断，截断后长度为0被认为是空字符串.
	 * @param str
	 * @param isTrimed
	 *            是否截去空白
	 * @return
	 */
	public static boolean isEmpty(String str, boolean isTrimed) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 判断列表是否为空，列表没有元素也被认为是空
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(List<?> list) {
		return list == null || list.size() == 0;
	}
	
	/**
	 * 判断数组是否为空
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object [] array){
		return array == null || array.length == 0;
	}
	
	/**
	 * 判断对象是否为空
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		return obj == null;
	}
}
