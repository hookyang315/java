package cn.sdframework;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import cn.sdframework.support.Column;
import cn.sdframework.support.JoinAtom;
import cn.sdframework.support.dict.ColumnType;
import cn.sdframework.support.util.ConsoleOut;
import cn.sdframework.support.util.DateUtil;

@Repository("crudDao")
public abstract class CrudDao<T> extends SimpleDao {

	protected final String dialect = "mysql";

	private String tbname = this.tableName;
	
	public String getTbname() {
		return tbname;
	}

	public void setTbname(String tbname) {
		this.tbname = tbname;
	}

	protected StringBuffer execSql = null;

	public void add(final T t) throws Exception {
		if (tableName != null && tableName.length() > 0) {
			execSql = new StringBuffer("");
			final List<Column> columns = fetchColumn();
			if (columns != null && columns.size() > 0) {
				final int _length = columns.size();
				execSql.append("INSERT INTO " + tableName);
				execSql.append("(");
				for (int i = 0; i < _length; i++) {
					Column _c = columns.get(i);
					if (i != _length - 1) {
						execSql.append(_c.columnName + ",");
					} else {
						execSql.append(_c.columnName + ")");
					}
				}
				execSql.append("VALUES(");
				for (int i = 1; i <= _length; i++) {
					if (i != _length) {
						execSql.append("?,");
					} else {
						execSql.append("?");
					}
				}
				execSql.append(")");
				ConsoleOut.print("INSERT SQL:" + execSql.toString());
				jdbcTemplate.update(execSql.toString(), new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps) throws SQLException {
						for (int i = 1; i <= _length; i++) {
							Column _c = columns.get(i - 1);
							String mName = parGetName(_c.field.getName());
							Method _m;
							try {
								_m = t.getClass().getMethod(mName);
								if (_c.type.equals(ColumnType.String)) {
									ps.setString(i, (String) _m.invoke(t));
									continue;
								}
								if (_c.type.equals(ColumnType.BigDecimal)) {
									ps.setBigDecimal(i, (BigDecimal) _m.invoke(t));
									continue;
								}
								if (_c.type.equals(ColumnType.Boolean)) {
									ps.setBoolean(i, (Boolean) _m.invoke(t));
									continue;
								}
								if (_c.type.equals(ColumnType.Byte)) {
									ps.setByte(i, (Byte) _m.invoke(t));
									continue;
								}
								if (_c.type.equals(ColumnType.Date)) {
									ps.setString(i, DateUtil.format((Date) _m.invoke(t), DateUtil.FORMAT_LONG));
									continue;
								}
								if (_c.type.equals(ColumnType.Double)) {
									ps.setDouble(i, (Double) _m.invoke(t));
									continue;
								}
								if (_c.type.equals(ColumnType.Float)) {
									ps.setFloat(i, (Float) _m.invoke(t));
									continue;
								}
								if (_c.type.equals(ColumnType.Integer)||_c.type.equals(ColumnType.INT)) {
									ps.setInt(i, (Integer) _m.invoke(t));
									continue;
								}
								if (_c.type.equals(ColumnType.Null)) {
									ps.setNull(i, Types.JAVA_OBJECT);
									continue;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
			} else {
				throw new RuntimeException("No column property was annotated to the field!");
			}
		} else {
			throw new RuntimeException("Orm not annotate table name!");
		}
	}

	public void modify(final T t) throws Exception {
		if (tableName != null && tableName.length() > 0) {
			execSql = new StringBuffer("");
			final List<Column> columns = this.fetchColumn();
			if (columns != null && columns.size() > 0) {
				Column _k = null;
				for (Column _c : columns) {
					if (_c.isKey) {
						_k = _c;
						columns.remove(_c);
						break;
					}
				}
				if (_k != null) {
					final Column keyColumn = _k;
					final int _length = columns.size();
					execSql.append("UPDATE " + tableName + " SET ");
					for (int i = 0; i < _length; i++) {
						if (i != _length - 1)
							execSql.append(columns.get(i).columnName + "=?,");
						else
							execSql.append(columns.get(i).columnName + "=? ");
					}
					execSql.append("WHERE " + keyColumn.columnName + "=?");
					ConsoleOut.print("UPDATE SQL:" + execSql.toString());
					ColumnType keyType = keyColumn.type;
					if (keyType.getClzz().equals(String.class) || keyType.getClzz().equals(Integer.class)) {
						jdbcTemplate.update(execSql.toString(), new PreparedStatementSetter() {
							public void setValues(PreparedStatement ps) throws SQLException {
								try {
									for (int i = 1; i <= _length; i++) {
										Column _c = columns.get(i - 1);
										String mName = parGetName(_c.field.getName());
										Method _m;
										_m = t.getClass().getMethod(mName);
										if (_c.type.equals(ColumnType.String)) {
											ps.setString(i, (String) _m.invoke(t));
											continue;
										}
										if (_c.type.equals(ColumnType.BigDecimal)) {
											ps.setBigDecimal(i, (BigDecimal) _m.invoke(t));
											continue;
										}
										if (_c.type.equals(ColumnType.Boolean)) {
											ps.setBoolean(i, (Boolean) _m.invoke(t));
											continue;
										}
										if (_c.type.equals(ColumnType.Byte)) {
											ps.setByte(i, (Byte) _m.invoke(t));
											continue;
										}
										if (_c.type.equals(ColumnType.Date)) {
											ps.setString(i, DateUtil.format((Date) _m.invoke(t), DateUtil.FORMAT_LONG));
											continue;
										}
										if (_c.type.equals(ColumnType.Double)) {
											ps.setDouble(i, (Double) _m.invoke(t));
											continue;
										}
										if (_c.type.equals(ColumnType.Float)) {
											ps.setFloat(i, (Float) _m.invoke(t));
											continue;
										}
										if (_c.type.equals(ColumnType.Integer)||_c.type.equals(ColumnType.INT)) {
											ps.setInt(i, (Integer) _m.invoke(t));
											continue;
										}
										if (_c.type.equals(ColumnType.Null)) {
											ps.setNull(i, Types.JAVA_OBJECT);
											continue;
										}
									}
									String mName = parGetName(keyColumn.field.getName());
									Method _m = t.getClass().getMethod(mName);
									if (keyColumn.type.equals(ColumnType.String))
										ps.setString(_length + 1, (String) _m.invoke(t));
									else
										ps.setInt(_length + 1, (Integer) _m.invoke(t));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					} else {
						throw new RuntimeException("Just String or Integer type can be set to key column!");
					}
				} else {
					throw new RuntimeException("No key column property was annotated to the field!");
				}
			} else {
				throw new RuntimeException("No column property was annotated to the field!");
			}
		} else {
			throw new RuntimeException("Orm not annotate table name!");
		}
	}

	public void delete(Object key) throws Exception {
		if (tableName != null && tableName.length() > 0) {
			execSql = new StringBuffer("DELETE FROM " + tableName);
			final List<Column> columns = this.fetchColumn();
			if (columns != null && columns.size() > 0) {
				Column _k = null;
				for (Column _c : columns) {
					if (_c.isKey) {
						_k = _c;
						continue;
					}
				}
				final List<JoinAtom> joins = this.fetchJoin();
				if(joins!=null&&joins.size()>0){
					
				}
				if (_k != null) {
					execSql.append(" WHERE " + _k.columnName + "=?");
					ConsoleOut.print("DELETE SQL:" + execSql.toString());
					jdbcTemplate.update(execSql.toString(), new Object[] { key });
				} else {
					throw new RuntimeException("No key column property was annotated to the field!");
				}
			}
		} else {
			throw new RuntimeException("Orm not annotate table name!");
		}
	}

	@SuppressWarnings("unchecked")
	public T findOne(Object key) throws Exception {
		Class<?> clazz = Class.forName(this.classModule.getCanonicalName());
		T t = (T) clazz.newInstance();
		if (tableName != null && tableName.length() > 0) {
			execSql = new StringBuffer("SELECT * FROM " + tableName + " ");
			final List<Column> columns = this.fetchColumn();
			if (columns != null && columns.size() > 0) {
				Column _k = null;
				for (Column _c : columns) {
					if (_c.isKey) {
						_k = _c;
						break;
					}
				}
				if (_k != null) {
					execSql.append("WHERE " + _k.columnName + "=?");
					ConsoleOut.print("QUERY FINDONE SQL:" + execSql.toString());
					final Map<String, Object> rowData = new HashMap<String, Object>();
					jdbcTemplate.query(execSql.toString(), new Object[] { key }, new RowCallbackHandler() {
						@Override
						public void processRow(ResultSet rs) throws SQLException {
							ResultSetMetaData md = rs.getMetaData();
							int cols = md.getColumnCount();
							List<String> colNames = new ArrayList<String>();
							for (int i = 1; i <= cols; i++) {
								colNames.add(md.getColumnName(i));
							}
							for (String _cn : colNames) {
								rowData.put(_cn, rs.getObject(_cn));
							}
						}
					});
					if (!rowData.isEmpty()) {
						Iterator<String> keys = rowData.keySet().iterator();
						while (keys.hasNext()) {
							String _key = keys.next();
							for (Column column : columns) {
								if (column.columnName.toLowerCase().equals(_key.toLowerCase())) {
									String methodName = this.parSetName(column.field.getName());
									Method _method = this.classModule.getMethod(methodName, column.type.getClzz());
									Object o = rowData.get(_key);
									if (o != null) {
										if (boolean.class.equals(column.type.getClzz())) {
											if ("1".equals(o.toString()))
												o = true;
											else
												o = false;
										}
										_method.invoke(t, o);
										break;
									}else{
										break;
									}
								}
							}
						}
						;
					} else {
						throw new RuntimeException("Not found the object");
					}
				} else {
					throw new RuntimeException("No key column property was annotated to the field!");
				}
			}
		} else {
			throw new RuntimeException("Orm not annotate table name!");
		}
		return t;
	}

	@SuppressWarnings("unchecked")
	public T findOneWith(String queryKey) throws Exception {
		Class<?> clazz = Class.forName(this.classModule.getCanonicalName());
		T t = (T) clazz.newInstance();
		if (tableName != null && tableName.length() > 0) {
			execSql = new StringBuffer("SELECT * FROM " + tableName + " ");
			final List<Column> columns = this.fetchColumn();
			if (columns != null && columns.size() > 0) {
				Column _k = null;
				for (Column _c : columns) {
					if (_c.isKey) {
						_k = _c;
						break;
					}
				}
				if (_k != null) {
					execSql.append("WHERE " + queryKey);
					ConsoleOut.print("QUERY FINDONEWITH SQL:" + execSql.toString());
					final Map<String, Object> rowData = new HashMap<String, Object>();
					jdbcTemplate.query(execSql.toString(), new RowCallbackHandler() {
						@Override
						public void processRow(ResultSet rs) throws SQLException {
							ResultSetMetaData md = rs.getMetaData();
							int cols = md.getColumnCount();
							List<String> colNames = new ArrayList<String>();
							for (int i = 1; i <= cols; i++) {
								colNames.add(md.getColumnName(i));
							}
							for (String _cn : colNames) {
								rowData.put(_cn, rs.getObject(_cn));
							}
						}
					});
					if (!rowData.isEmpty()) {
						Iterator<String> keys = rowData.keySet().iterator();
						while (keys.hasNext()) {
							String _key = keys.next();
							for (Column column : columns) {
								if (column.columnName.toLowerCase().equals(_key.toLowerCase())) {
									String methodName = this.parSetName(column.field.getName());
									Method _method = this.classModule.getMethod(methodName, column.type.getClzz());
									Object o = rowData.get(_key);
									if (o != null) {
										if (boolean.class.equals(column.type.getClzz())) {
											if ("1".equals(o.toString()))
												o = true;
											else
												o = false;
										}
										_method.invoke(t, o);
										break;
									}else{
										break;
									}
								}
							}
						}
						;
					} else {
						throw new RuntimeException("Not found the object");
					}
				} else {
					throw new RuntimeException("No key column property was annotated to the field!");
				}
			}
		} else {
			throw new RuntimeException("Orm not annotate table name!");
		}
		return t;
	}

	@SuppressWarnings("unchecked")
	public List<T> findList(String queryKey) throws Exception {
		List<T> result = new ArrayList<T>();
		Class<?> clazz = Class.forName(this.classModule.getCanonicalName());
		if (tableName != null && tableName.length() > 0) {
			execSql = new StringBuffer("SELECT * FROM " + tableName + " ");
			final List<Column> columns = this.fetchColumn();
			if (columns != null && columns.size() > 0) {
				Column _k = null;
				for (Column _c : columns) {
					if (_c.isKey) {
						_k = _c;
						break;
					}
				}
				if (_k != null) {
					execSql.append("WHERE " + queryKey);
					ConsoleOut.print("QUERY FINDLIST SQL:" + execSql.toString());
					final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
					jdbcTemplate.query(execSql.toString(), new RowCallbackHandler() {
						@Override
						public void processRow(ResultSet rs) throws SQLException {
							ResultSetMetaData md = rs.getMetaData();
							int cols = md.getColumnCount();
							List<String> colNames = new ArrayList<String>();
							Map<String, Object> rowData = new HashMap<String, Object>();
							for (int i = 1; i <= cols; i++) {
								colNames.add(md.getColumnName(i));
							}
							for (String _cn : colNames) {
								rowData.put(_cn, rs.getObject(_cn));
							}
							datas.add(rowData);
						}
					});
					if (datas != null && datas.size() > 0) {
						for (Map<String, Object> rowData : datas) {
							if (!rowData.isEmpty()) {
								T t = (T) clazz.newInstance();
								Iterator<String> keys = rowData.keySet().iterator();
								while (keys.hasNext()) {
									String _key = keys.next();
									for (Column column : columns) {
										if (column.columnName.toLowerCase().equals(_key.toLowerCase())) {
											String methodName = this.parSetName(column.field.getName());
											Method _method = this.classModule.getMethod(methodName,
													column.type.getClzz());
											Object o = rowData.get(_key);
											if (o != null) {
												if (boolean.class.equals(column.type.getClzz())) {
													if ("1".equals(o.toString()))
														o = true;
													else
														o = false;
												}
												_method.invoke(t, o);
												break;
											}else{
												break;
											}
										}
									}
								}
								;
								result.add(t);
							} else {
								throw new RuntimeException("Not found the object");
							}
						}
					}

				} else {
					throw new RuntimeException("No key column property was annotated to the field!");
				}
			}
		} else {
			throw new RuntimeException("Orm not annotate table name!");
		}
		return result;
	}
}
