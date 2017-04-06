package cn.sdframework.support.dict;

import java.math.BigDecimal;
import java.util.Date;
public enum ColumnType {
	
	Integer(Integer.class),
	INT(int.class),
	Double(Double.class),
	Float(Float.class),
	Boolean(boolean.class),
	Byte(Byte.class),
	BigDecimal(BigDecimal.class),
	Date(Date.class),
	String(String.class),
	Null(Object.class);
	
	private ColumnType(Class<?> clzz){
		this.clzz = clzz;
	}
	private Class<?> clzz;
	public Class<?> getClzz() {
		return clzz;
	}
	public void setClzz(Class<?> clzz) {
		this.clzz = clzz;
	}
	
}
