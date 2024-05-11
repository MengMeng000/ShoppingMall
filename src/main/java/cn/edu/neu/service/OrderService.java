package cn.edu.neu.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import cn.edu.neu.dao.DBUtil;
import cn.edu.neu.model.Order;
import cn.edu.neu.model.OrderDetail;

public class OrderService {
	public static int addOrder(Order order, List<OrderDetail> orderDetails) {
		PreparedStatement pstmt = null;
		Connection con = null;
		int orderId = 0;
		String sql = "insert into t_order values(null,?,?,1,?,?,?,null,null)";
		Date d = new Date();
		java.sql.Date d1 = new java.sql.Date(d.getTime());
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA);
		String str = sf.format(d);
		Random r = new Random();
		int i = r.nextInt(100);
		String s = "";
		if (i < 10) {
			s = "00" + i;
		} else if (i >= 10 && i <= 99) {
			s = "0" + i;
		}
		String orderCode = str + s;
		order.setOrderCode(orderCode);
		try {
			con = DBUtil.getCon();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, order.getOrderCode());
			pstmt.setString(2, order.getUserId() + "");
			pstmt.setString(3, order.getOrderAddress());
			pstmt.setString(4, order.getOrderPostalfee() + "");
			pstmt.setString(5, d1 + "");
			pstmt.executeUpdate();
			pstmt = con.prepareStatement("select LAST_INSERT_ID()");
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				orderId = Integer.parseInt(rs.getString(1));
				sql = "insert into t_orderdetail values(null,?,?,?,?,?,?)";
				pstmt = con.prepareStatement(sql);
				for (OrderDetail od : orderDetails) {
					pstmt.setString(1, orderId + "");
					pstmt.setString(2, od.getGoodsId() + "");
					pstmt.setString(3, od.getOdetailName());
					pstmt.setString(4, od.getOdetailPrice() + "");
					pstmt.setString(5, od.getOdetailNum() + "");
					pstmt.setString(6, od.getOdetailPic());
					pstmt.executeUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, pstmt, con);
		}
		return orderId;
	}

	public static void payOrder(String orderId) {
		System.out.println("payOrder orderId" + orderId);
		PreparedStatement pstmt = null;
		Connection con = null;
		String sql = "update t_order set order_status=2 where order_id=?";
		try {
			con = DBUtil.getCon();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, orderId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, pstmt, con);
		}
	}

	public static List<Order> getAllOrders(int userId, String status) {
		List<Order> myorders = new ArrayList<Order>();
		PreparedStatement pstmt = null;
		Connection con = null;
		String sql = "select * from t_order where user_id=?";
		if (status != null)
			sql = sql + " and order_status='" + status + "'";
		try {
			con = DBUtil.getCon();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId + "");
			ResultSet rs = pstmt.executeQuery();
			List<Map<String, String>> orders = DBUtil.getListFromRS(rs);// 据RS得到list
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			if (orders != null) {
				for (Map<String, String> m : orders) {
					Order o = new Order();
					o.setOrderId(Integer.parseInt(m.get("order_id")));
					o.setUserId(userId);
					o.setOrderAddress(m.get("order_address"));
					o.setOrderDate(getFormatDate(m.get("order_date")));
					o.setOrderCode(m.get("order_code"));
					o.setOrderStatus(Integer.parseInt(m.get("order_status")));
					o.setOrderPostalfee(Float.parseFloat(m.get("order_postalfee")));
					String sql1 = "select * from t_orderdetail where order_id=?";
					pstmt = con.prepareStatement(sql1);
					pstmt.setString(1, o.getOrderId() + "");
					rs = pstmt.executeQuery();
					List<Map<String, String>> od = DBUtil.getListFromRS(rs);// 根据RS得到list
					List<OrderDetail> odetails = new ArrayList<OrderDetail>();
					// 将map的商品详情转为orderDetail的详情
					if (od != null) {
						for (Map<String, String> mod : od) {
							OrderDetail ord = new OrderDetail();
							ord.setGoodsId(Integer.parseInt(mod.get("goods_id")));
							ord.setOdetailName(mod.get("odetail_name"));
							ord.setOdetailNum(Integer.parseInt(mod.get("odetail_num")));
							ord.setOdetailPrice(Float.parseFloat(mod.get("odetail_price")));
							ord.setOdetailPic(mod.get("odetail_pic"));
							odetails.add(ord);
						}
					}
					o.setOdetails(odetails);
					myorders.add(o);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, pstmt, con);
		}
		return myorders;
	}

	public static int changeOrderStatus(String orderId, String status) {
		PreparedStatement pstmt = null;
		Connection con = null;
		String sql = "update t_order set order_status=? where order_id=?";
		int result = -1;
		try {
			con = DBUtil.getCon();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, status);
			pstmt.setString(2, orderId);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, pstmt, con);
		}
		return result;
	}

	public static void delOrder(String orderId) {
		PreparedStatement pstmt = null;
		Connection con = null;
		String sql1 = "delete  from t_orderdetail where order_id=?";
		String sql2 = "delete from t_order where order_id=?";
		try {
			con = DBUtil.getCon();
			pstmt = con.prepareStatement(sql1);
			pstmt.setString(1, orderId);
			pstmt.executeUpdate();
			pstmt = con.prepareStatement(sql2);
			pstmt.setString(1, orderId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, pstmt, con);
		}
	}

	public static Order getOrderDetailById(String orderId) {
		Order order = null;
		List<OrderDetail> odetails = null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		String sql = "select * from t_order where order_id=?";
		List<Map<String, String>> list = null;
		try {
			con = DBUtil.getCon();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, orderId);
			rs = pstmt.executeQuery();
			list = DBUtil.getListFromRS(rs);
			if (list != null) {
				Map<String, String> order_map = (Map<String, String>) (list.get(0));
				sql = "select * from t_orderdetail where order_id=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, orderId);
				rs = pstmt.executeQuery();
				List<Map<String, String>> odetails_list = DBUtil.getListFromRS(rs);
				if (odetails_list != null) {
					odetails = new ArrayList<OrderDetail>();
					for (Map<String, String> mod : odetails_list) {
						OrderDetail ord = new OrderDetail();
						ord.setGoodsId(Integer.parseInt(mod.get("goods_id")));
						ord.setOdetailName(mod.get("odetail_name"));
						ord.setOdetailNum(Integer.parseInt(mod.get("odetail_num")));
						ord.setOdetailPrice(Float.parseFloat(mod.get("odetail_price")));
						ord.setOdetailPic(mod.get("odetail_pic"));
						odetails.add(ord);
					}
					order = new Order();
					order.setOdetails(odetails);
					order.setOrderId(Integer.parseInt(order_map.get("order_id")));
					order.setUserId(Integer.parseInt(order_map.get("user_id")));
					order.setOrderAddress(order_map.get("order_address"));
					String dateStr = order_map.get("order_date");
					order.setOrderDate(getFormatDate(dateStr));
					order.setOrderCode(order_map.get("order_code"));
					order.setOrderStatus(Integer.parseInt(order_map.get("order_status")));
					order.setOrderPostalfee(Float.parseFloat(order_map.get("order_postalfee")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DBUtil.close(rs, pstmt, con);
		}
		return order;
	}

	public static Date getFormatDate(String dateStr) {
		String[] dateArr = dateStr.split("-");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, Integer.parseInt(dateArr[0]));
		c.set(Calendar.MONTH, Integer.parseInt(dateArr[1]) - 1);
		c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArr[2]));
		Date date = c.getTime();
		return date;
	}
	/*
	 * public static int payOrder(String orderId List<Order> getAllOrders) { return
	 * orderId; }
	 */

}