package cn.sdframework.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.sdframework.support.dict.ColumnType;
import cn.sdframework.support.dict.SortType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnProp {
	
	public String columnName();
	
	public ColumnType type() default ColumnType.String;
	
	public boolean isKey() default false;
	
	public boolean defaultSort() default false;
	
	public SortType sortType() default SortType.NULL;
	
	public int length() default 0;
	
}
