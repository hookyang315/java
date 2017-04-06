package cn.sdframework.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.sdframework.support.dict.JoinType;
import cn.sdframework.support.dict.OperateType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Join {
	
	public JoinType joinType();
	
	public OperateType operate() default OperateType.NONE;
	
	public String relKey();
	
	public String key();
}
