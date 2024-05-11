package cn.edu.neu.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.neu.dao.DBUtil;
import cn.edu.neu.model.Address;

public class AddressService {
	// 获取登录用户的所有收货地址信息
	public static List<Address> getAllAddress(int userId) {
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		List<Address> addrs_model = null;
		try {
			con = DBUtil.getCon();
			String sql = "select * from t_address where user_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			List<Map<String, String>> list = null;
			// 获取元数据
			ResultSetMetaData rsmd = rs.getMetaData();
			if (rs.next()) {
				list = new ArrayList<Map<String, String>>();
				do {
					Map<String, String> m = new HashMap<String, String>();
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						// 获取当前行第i列的列名
						String colName = rsmd.getColumnLabel(i);
						String s = rs.getString(colName);
						if (s != null) {
							m.put(colName, s);
						}
					}
					list.add(m);
				} while (rs.next());
				addrs_model = new ArrayList<Address>();
				for (Map<String, String> addrs_map : list) {
					Address a = new Address();
					a.setAddrId(Integer.parseInt(addrs_map.get("addr_id")));
					a.setAddrArea(addrs_map.get("addr_area"));
					a.setAddrCity(addrs_map.get("addr_city"));
					a.setAddrContent(addrs_map.get("addr_content"));
					a.setAddrIsdefault(Integer.parseInt(addrs_map.get("addr_isdefault")));
					a.setAddrProvince(addrs_map.get("addr_province"));
					a.setAddrReceiver(addrs_map.get("addr_receiver"));
					a.setAddrTel(addrs_map.get("addr_tel"));
					a.setUserId(Integer.parseInt(addrs_map.get("user_id")));
					addrs_model.add(a);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DBUtil.close(rs, pstmt, con);
		}
		return addrs_model;
	}

	public static void delAddressById(String addrId) {
		PreparedStatement pstmt = null;
		Connection con = null;
		String sql = "delete from t_address where addr_id=?";
		try {
			con = DBUtil.getCon();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, addrId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.close(null, pstmt, con);
		}
	}

	public static Address getAddressById(String addrId) {
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		String sql = "select * from t_address where addr_id=?";
		Address ret = null;
		List<Map<String, String>> list = null;
		try {
			con = DBUtil.getCon();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, addrId);
			rs = pstmt.executeQuery();
			list = DBUtil.getListFromRS(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DBUtil.close(rs, pstmt, con);
		}
		if (list != null && list.size() > 0) {
			Map<String, String> addrs_map = (Map<String, String>) (list.get(0));
			if (addrs_map != null) {
				ret = new Address();
				ret.setAddrId(Integer.parseInt(addrs_map.get("addr_id")));
				ret.setAddrArea(addrs_map.get("addr_area"));
				ret.setAddrCity(addrs_map.get("addr_city"));
				ret.setAddrContent(addrs_map.get("addr_content"));
				ret.setAddrIsdefault(Integer.parseInt(addrs_map.get("addr_isdefault")));
				ret.setAddrProvince(addrs_map.get("addr_province"));
				ret.setAddrReceiver(addrs_map.get("addr_receiver"));
				ret.setAddrTel(addrs_map.get("addr_tel"));
				ret.setUserId(Integer.parseInt(addrs_map.get("user_id")));
			}
		}
		return ret;
	}

	public static int addAddress(Address addr) {
		PreparedStatement pstmt = null;
		Connection con = null;
		int addrId = 0;
		String sql = "insert into t_address values(null,?,?,?,?,?,?,?,0)";
		try {
			con = DBUtil.getCon();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, addr.getUserId() + "");
			pstmt.setString(2, addr.getAddrProvince());
			pstmt.setString(3, addr.getAddrCity());
			pstmt.setString(4, addr.getAddrArea());
			pstmt.setString(5, addr.getAddrContent());
			pstmt.setString(6, addr.getAddrReceiver());
			pstmt.setString(7, addr.getAddrTel());
			pstmt.executeUpdate();
			pstmt = con.prepareStatement("select LAST_INSERT_ID()");
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				addrId = Integer.parseInt(rs.getString(1));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, pstmt, con);
		}

		return addrId;
	}

	public static void updateAddress(Address addr) {
		PreparedStatement pstmt = null;
		Connection con = null;
		String sql = "update t_address set addr_province=?,addr_city=?,addr_area=?,addr_content=?,addr_receiver=?,addr_tel=? where addr_id=?";
		try {
			con = DBUtil.getCon();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, addr.getAddrProvince());
			pstmt.setString(2, addr.getAddrCity());
			pstmt.setString(3, addr.getAddrArea());
			pstmt.setString(4, addr.getAddrContent());
			pstmt.setString(5, addr.getAddrReceiver());
			pstmt.setString(6, addr.getAddrTel());
			pstmt.setString(7, addr.getAddrId() + "");
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, pstmt, con);
		}
	}

	public static boolean setDefaultAddress(String addrId, int userId) {
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean ret = false;
		int result = -1;
		System.out.println("addrId=" + addrId);
		String sql = "update t_address set addr_isdefault=0 where user_id=?";
		try {
			con = DBUtil.getCon();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId + "");
			result = pstmt.executeUpdate();
			if (result > 0) {
				sql = "update t_address set addr_isdefault=1 where addr_id=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, addrId);
				result = pstmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, pstmt, con);
		}
		if (result > 0) {
			ret = true;
		}
		return ret;
	}

}
