package cn.sdframework.support;

import java.lang.reflect.Field;

import cn.sdframework.support.dict.ColumnType;
import cn.sdframework.support.dict.SortType;

public class Column {
	public String columnName;
	public Field field;
	public boolean isKey;
	public boolean isDefaultSort;
	public SortType sortType;
	public ColumnType type;
	public int length;
}
