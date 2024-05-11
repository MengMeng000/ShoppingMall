package cn.edu.neu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
	// �������ݿ�
	public static Connection getCon() {
		Connection con = null;
		String url = "jdbc:mysql://localhost:3306/eshop_database?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
		String user = "root";
		String password = "limeng";// ����Ϊ������ݿ�����
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
			return con;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	// �ر����ݿ�
	public static void close(ResultSet rs, PreparedStatement ps, Connection con) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	// ���������װ��һ��List
	public static List<Map<String, String>> getListFromRS(ResultSet rs) throws SQLException {
		List<Map<String, String>> list = null;
		// ��ȡԪ����
		ResultSetMetaData rsmd = rs.getMetaData();
		if (rs.next()) {
			list = new ArrayList<Map<String, String>>();
			do {
				Map<String, String> m = new HashMap<String, String>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					// ��ȡ��ǰ�е�i�е�����
					String colName = rsmd.getColumnLabel(i);
					String s = rs.getString(colName);
					if (s != null) {
						m.put(colName, s);
					}
				}
				list.add(m);
			} while (rs.next());
		}
		return list;
	}
}