package cn.sdframework;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import cn.sdframework.support.Column;
import cn.sdframework.support.ConsoleOut;
import cn.sdframework.support.DataPage;
import cn.sdframework.support.dict.ColumnType;

@Repository("pageDao")
public abstract class ExtendedPageDAO<T> extends CrudDao<T> {
	
	public DataPage<T> queryForPage(String whereStr, String orderStr, int currentPage, int pageSize)
			throws Exception {
		String tbname = this.tableName;
		DataPage<T> page = null;
		if(orderStr==null||orderStr.length()==0)
			orderStr = fetchDefaultSort();
		if (dialect.trim().toLowerCase().equals("oracle")) {
			page = this.buildeOraclePage(tbname, whereStr, orderStr, currentPage, pageSize);
		}
		if (dialect.trim().toLowerCase().equals("mysql")) {
			page = this.buildMySqlPage(tbname, whereStr, orderStr, currentPage, pageSize);
		}
		return page;
	}
	
	/**
	 * 执行MYSQL分页语句
	 * 
	 * @param tbname
	 *            分页查询的表名或者视图名
	 * @param whereStr
	 *            分页查询的查询条件语句
	 * @param orderStr
	 *            分页查询的排序语句
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            每页显示条数
	 * @return 分页对象
	 * @throws SQLException
	 */
	private DataPage<T> buildMySqlPage(String tbname, String whereStr, String orderStr, int currentPage, int pageSize)
			throws Exception {
		DataPage<T> result = new DataPage<T>();
		execSql = new StringBuffer("SELECT COUNT(*) FROM ");
		execSql.append(tbname);
		execSql.append(" WHERE 1=1 ");
		if (whereStr != null && whereStr.length() > 0) {
			execSql.append(whereStr);
		}
		Integer totalRecords = this.jdbcTemplate.queryForObject(execSql.toString(), Integer.class);
		result.setTotalRecords(totalRecords);
		// 验证每页条目数
		if (pageSize <= 0) {
			pageSize = 10;
		}
		// 验证总页数
		int totalPages = (totalRecords % pageSize) == 0 ? (totalRecords / pageSize) : (totalRecords / pageSize) + 1;
		result.setTotalPages(totalPages);
		// 验证当前页
		if (currentPage <= 0)
			currentPage = 1;
		if (currentPage > totalPages)
			currentPage = totalPages;
		result.setCurrentPage(currentPage);
		result.setPageSize(pageSize);
		int startRecorder = (currentPage-1) * pageSize;
		if(startRecorder>=0){
			execSql = new StringBuffer("SELECT * FROM ");
			execSql.append(tbname);
			execSql.append(" WHERE 1=1 ");
			if (whereStr != null && whereStr.length() > 0) {
				execSql.append(whereStr);
				execSql.append(" ");
			}
			if (orderStr != null && orderStr.length() > 0) {
				execSql.append("ORDER BY ");
				execSql.append(orderStr);
				execSql.append(" ");
			}
			execSql.append("LIMIT ");
			execSql.append(startRecorder);
			execSql.append(",");
			execSql.append(pageSize);
			ConsoleOut.print("QUERY FOR PAGE:" + execSql.toString());
			result.setOrigData(fetchData(execSql.toString()));
			result.setPageData(fetchOrmData(result.getOrigData()));
		}
		return result;
	}

	/**
	 * 执行Oracle 分页语句
	 * 
	 * @param tbname
	 *            分页查询的表名或者视图名
	 * @param whereStr
	 *            分页查询的查询条件语句
	 * @param orderStr
	 *            分页查询的排序语句
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            每页显示条数
	 * @return 分页对象
	 * @throws SQLException
	 */
	private DataPage<T> buildeOraclePage(String tbname, String whereStr, String orderStr, int currentPage, int pageSize)
			throws Exception {
		DataPage<T> result = new DataPage<T>();
		execSql = new StringBuffer("SELECT COUNT(*) FROM ");
		execSql.append(tbname);
		execSql.append(" WHERE 1=1 ");
		if (whereStr != null && whereStr.length() > 0) {
			execSql.append(whereStr);
		}
		Integer totalRecords = this.jdbcTemplate.queryForObject(execSql.toString(), Integer.class);
		result.setTotalRecords(totalRecords);
		// 验证每页条目数
		if (pageSize <= 0) {
			pageSize = 10;
		}
		// 验证总页数
		int totalPages = (totalRecords % pageSize) == 0 ? (totalRecords / pageSize) : (totalRecords / pageSize) + 1;
		result.setTotalPages(totalPages);
		// 验证当前页
		if (currentPage <= 0)
			currentPage = 1;
		if (currentPage > totalPages)
			currentPage = totalPages;
		result.setCurrentPage(currentPage);
		result.setPageSize(pageSize);
		int startRecord = (currentPage-1)*pageSize+1;
		int endRecord = currentPage * pageSize;
		if(startRecord>0){
			execSql = new StringBuffer("SELECT * FROM (SELECT A.*,rownum r FROM ");
			execSql.append("(SELECT * FROM ");
			execSql.append(tbname);
			execSql.append(" WHERE 1=1 ");
			if (whereStr != null && whereStr.length() > 0) {
				execSql.append(whereStr);
				execSql.append(" ");
			}
			if (orderStr != null && orderStr.length() > 0) {
				execSql.append("ORDER BY ");
				execSql.append(orderStr);
				execSql.append(" ");
			}
			execSql.append(") A WHERE rownum <=");
			execSql.append(endRecord);
			execSql.append(")B WHERE r>=");
			execSql.append(startRecord);
			ConsoleOut.print("QUERY FOR PAGE:" + execSql.toString());
			result.setOrigData(fetchData(execSql.toString()));
			result.setPageData(fetchOrmData(result.getOrigData()));
		}
		return result;
	}
	
	
	private List<Map<String, Object>> fetchData(final String sql) throws SQLException{
		final List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		this.jdbcTemplate.query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				ResultSetMetaData md = rs.getMetaData();
				int cols = md.getColumnCount();
				List<String> colNames = new ArrayList<String>();
				for(int i=1;i<=cols;i++){
					colNames.add(md.getColumnName(i));
				}
				do{
					Map<String,Object> rowData = new HashMap<String,Object>();
					for(String _cn : colNames){
						rowData.put(_cn, rs.getObject(_cn));
					}
					results.add(rowData);
				}while(rs.next());
			}
		});
		return results;
	}
	
	@SuppressWarnings("unchecked")
	private List<T> fetchOrmData(List<Map<String, Object>> datas) throws Exception{
		List<T> result = new ArrayList<T>();
		final List<Column> columns = this.fetchColumn();
		if(columns!=null&&columns.size()>0){
			if(datas!=null&&datas.size()>0){
				for(Map<String,Object> rowData:datas){
					if(!rowData.isEmpty()){
						T t = (T) this.classModule.newInstance();
						Iterator<String> keys = rowData.keySet().iterator();
						while(keys.hasNext()){
							String _key = keys.next();
							for(Column column : columns){
								if(column.columnName.toLowerCase().equals(_key.toLowerCase())){
									String methodName = this.parSetName(column.field.getName());
									Method _method = this.classModule.getMethod(methodName,column.type.getClzz());
									Object o = rowData.get(_key);
									if(o==null){
										ColumnType _t = column.type;
										if(_t.equals(ColumnType.BigDecimal))
											o = new BigDecimal(0);
										else if(_t.equals(ColumnType.Boolean))
											o = false;
										else if(_t.equals(ColumnType.Byte))
											o = new Byte("");
										else if(_t.equals(ColumnType.Double))
											o = new Double(0);
										else if(_t.equals(ColumnType.Float))
											o = new Float(0);
										else if(_t.equals(ColumnType.INT)||_t.equals(ColumnType.Integer))
											o = 0;
										else if(_t.equals(ColumnType.String))
											o = "";
									}
									if(o!=null){
										if(boolean.class.equals(column.type.getClzz())){
											if("1".equals(o.toString()))
												o=true;
											else
												o=false;
										}
										_method.invoke(t,o);
										break;
									}
								}
							}
						};
						result.add(t);
					}
				}
			}
		}else{
			throw new RuntimeException("No column Property was configed!");
		}
		return result;
	}
}
