package cn.sdframework;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.sdframework.support.Column;
import cn.sdframework.support.ColumnProp;
import cn.sdframework.support.Table;

@Repository("simpleDao")
public abstract class SimpleDao{
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	protected String tableName;
	
	protected Class<?> classModule;
	
	public abstract void configClassModule();
	
	public SimpleDao(){
		this.configClassModule();
		this.fetchTableName();
	}
	
	private void fetchTableName(){
		if(classModule.isAnnotationPresent(Table.class)||classModule.isAnnotationPresent(javax.persistence.Table.class)){
			if(classModule.isAnnotationPresent(Table.class)){
				Table tableAnno = classModule.getAnnotation(Table.class);
				this.tableName = tableAnno.tableName();
			}else{
				javax.persistence.Table tableAnno = classModule.getAnnotation(javax.persistence.Table.class);
				this.tableName = tableAnno.name();
			}
		}else{
			throw new RuntimeException("This class not config the table annotation！");
		}
	}
	
	protected String fetchDefaultSort(){
		String defaultSort = "";
		List<Column> columns = this.fetchColumn();
		if(columns!=null&&columns.size()>0){
			for(int i=0;i<columns.size();i++){
				if(columns.get(i).isDefaultSort){
					defaultSort+=columns.get(i).columnName+" "+columns.get(i).sortType.getType()+",";
				}
			}
		}
		return defaultSort.substring(0,defaultSort.length()-1);
	}
	
	protected List<Column> fetchColumn(){
		Field[] fields = classModule.getDeclaredFields();
		List<Column> toConfigColumns = null;
		if(fields!=null&&fields.length>0){
			toConfigColumns = new ArrayList<Column>();
			for(Field _f : fields){
				if(_f.isAnnotationPresent(ColumnProp.class)){
					Column _c = new Column();
					ColumnProp columnAnno = _f.getAnnotation(ColumnProp.class);
					_c.columnName = columnAnno.columnName();
					_c.field = _f;
					_c.type = columnAnno.type();
					_c.isKey = columnAnno.isKey();
					_c.length = columnAnno.length();
					_c.isDefaultSort = columnAnno.defaultSort();
					_c.sortType = columnAnno.sortType();
					toConfigColumns.add(_c);
				}else{
					continue;
				}
			}
		}
		return toConfigColumns;
	}
	
	protected String parGetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}
	
	protected String parSetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}
	
}
