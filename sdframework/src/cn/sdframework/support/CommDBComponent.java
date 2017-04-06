package cn.sdframework.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sdframework.config.ApplicationParam;

public class CommDBComponent {

	private PreparedStatement pstmt = null;
	private Statement stmt = null;
	private ResultSet rs;
	private Connection conn = null;

	public Connection getConntion() {
		try {
			Class.forName(ApplicationParam.driverClass);
			String url = ApplicationParam.url;
			conn = DriverManager.getConnection(url, ApplicationParam.username, ApplicationParam.password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void close() {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public List<Map<String, Object>> executeQueryListMap(String sql) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			conn = this.getConntion();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// pstmt=conn.prepareStatement(sql);
			rs = stmt.executeQuery(sql);// 执行查询
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				rs.getString(1);
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
					map.put(rs.getMetaData().getColumnName(i).toLowerCase(), rs.getString(i));
				list.add(map);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return list;
	}

	public int executeSql(String sql) {
		int result = 0; // 定义保存返回值的变量
		try { // 捕捉异常
			conn = this.getConntion();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			result = stmt.executeUpdate(sql); // 执行更新操作
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return result; // 返回保存返回值的变量
	}
}
