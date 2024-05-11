package cn.edu.neu.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.neu.model.Category;
import cn.edu.neu.model.Goods;
import cn.edu.neu.model.Order;
import cn.edu.neu.model.OrderDetail;
import cn.edu.neu.model.User;
import cn.edu.neu.service.HomeService;
import cn.edu.neu.service.OrderService;

/**
 * Servlet implementation class OrderAction
 */
@WebServlet(urlPatterns = { "/order/addOrder.action", "/order/getOrderDetailById.action", "/order/payOrder.action",
		"/order/getMyOrders.action", "/order/delOrder.action", "/order/cancelOrder.action" })
public class OrderController extends HttpServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OrderController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getServletPath();
		req.setCharacterEncoding("utf-8");
		List<Category> catelist = HomeService.getCateList();
		req.setAttribute("catelist", catelist);
		Object ob = req.getSession().getAttribute("goodslist");
		User logu = (User) req.getSession().getAttribute("_LOGIN_USER_");
		if (logu == null) {
			req.setAttribute("msg", "登录已过期，请重新登录！！");
			req.getRequestDispatcher("/usercenter/index.jsp").forward(req, resp);
			// 生成订单
		} else if (url.equals("/order/addOrder.action")) {
			if (ob == null) {
				req.setAttribute("msg", "购物车为空，请先选购商品！！！");
				req.getRequestDispatcher("/usercenter/index.jsp").forward(req, resp);
			} else {
				String address = req.getParameter("address");
				Order order = new Order();
				order.setUserId(logu.getUserId());
				order.setOrderAddress(address);
				List<Goods> goodslist = new ArrayList<Goods>();
				goodslist = (List<Goods>) ob;
				List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
				float postalfee = goodslist.get(0).getGoodsPostalfee();
				for (Goods g : goodslist) {
					OrderDetail od = new OrderDetail();
					od.setGoodsId(g.getGoodsId());
					od.setOdetailName(g.getGoodsName());
					od.setOdetailPrice(g.getGoodsPrice());
					od.setOdetailNum(g.getGoodsSales());
					od.setOdetailPic(g.getGoodsPic());
					float temp = g.getGoodsPostalfee();
					if (postalfee > temp)
						postalfee = temp;
					orderDetails.add(od);
				}
				order.setOrderPostalfee(postalfee);
				req.setAttribute("orderId", OrderService.addOrder(order, orderDetails));
				req.getSession().removeAttribute("goodslist");
				req.getRequestDispatcher("/order/payOrders.jsp").forward(req, resp);
			}
		}
		// 取消订单
		else if (url.equals("/order/cancelOrder.action")) {
			String orderId = req.getParameter("orderId");
			int result = OrderService.changeOrderStatus(orderId, "3");
//			req.getRequestDispatcher("/order/getMyOrders.action").forward(req, resp);
			PrintWriter out = resp.getWriter();
			if (result > 0) {
				out.print("{\"success\":true}");
			} else {
				out.print("{\"success\":false}");
			}

			out.flush();
			// 支付订单
		} else if (url.equals("/order/payOrder.action")) {
			String orderId = req.getParameter("orderId");
			OrderService.payOrder(orderId);
			req.getRequestDispatcher("/order/getMyOrders.action").forward(req, resp);
		}
		// 删除订单
		else if (url.equals("/order/delOrder.action")) {
			String orderId = req.getParameter("orderId");
			OrderService.delOrder(orderId);
			req.getRequestDispatcher("/order/getMyOrders.action").forward(req, resp);
		}
		// 获取全部订单
		else if (url.equals("/order/getMyOrders.action")) {
			String status = req.getParameter("status");
			System.out.println("getMyOrders.action:status=" + status);
			List<Order> orderlist = OrderService.getAllOrders(logu.getUserId(), status);
			req.setAttribute("orders", orderlist);
			System.out.println("getMyOrders.action:orderlist=" + orderlist.size());
			req.getRequestDispatcher("/order/orderList.jsp").forward(req, resp);
		}
		// 获取订单详情
		else if (url.equals("/order/getOrderDetailById.action")) {
			String orderId = req.getParameter("orderId");
			Order orderDetail = OrderService.getOrderDetailById(orderId);
			req.setAttribute("orderDetail", orderDetail);
			req.getRequestDispatcher("/order/orderDetail.jsp").forward(req, resp);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}