package cn.sdframework.support;

import java.lang.reflect.Field;

import cn.sdframework.support.dict.JoinType;
import cn.sdframework.support.dict.OperateType;

public class JoinAtom {
	public JoinType type;
	public String key;
	public String relKey;
	public OperateType operate;
	public Field field;
}
