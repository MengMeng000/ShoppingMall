package cn.edu.neu.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import cn.edu.neu.dao.DBUtil;
import cn.edu.neu.model.User;

public class UserService {

	public static User getUserByUserId(int userId) {
		PreparedStatement pstmt = null;
		Connection con = null;
		Map<String, String> map = null;
		User user = null;
		String sql = "select * from t_user where user_id=" + userId;
		try {
			con = DBUtil.getCon();
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			List<Map<String, String>> list = DBUtil.getListFromRS(rs);// ¸ù¾ÝRSµÃµ½list
			if (list != null && list.size() > 0) {
				map = (Map<String, String>) (list.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, pstmt, con);
		}
		if (map != null) {
			user = new User();
			user.setUserName(map.get("user_name"));
			user.setUserPass(map.get("user_pass"));
			user.setUserAge(Integer.parseInt(map.get("user_age")));
			user.setUserEmail(map.get("user_email"));
			user.setUserSex(Integer.parseInt(map.get("user_sex")));
		}

		return user;
	}

	public static int UpdateUserInfo(User newu) {
		PreparedStatement pstmt = null;
		Connection con = null;
		String sql = "update t_user set user_sex=?,user_email=?,user_age=? where user_id=?";
		int result = -1;
		try {
			con = DBUtil.getCon();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, newu.getUserSex() + "");
			pstmt.setString(2, newu.getUserEmail());
			pstmt.setString(3, newu.getUserAge() + "");
			pstmt.setString(4, newu.getUserId() + "");
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, pstmt, con);
		}
		return result;
	}

	public static boolean UpdateUserPass(String userName, String newUserPass) {
		PreparedStatement pstmt = null;
		Connection con = null;
		String sql = "update t_user set user_pass=? where user_name=?";
		int result = -1;
		try {
			con = DBUtil.getCon();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, newUserPass);
			pstmt.setString(2, userName);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, pstmt, con);
		}
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean CheckUserPass(String userName, String userPass) {
		User user = HomeService.getCheckUser(userName, userPass);
		if (user == null)
			return false;
		else
			return true;
	}

}