package cn.edu.neu.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.edu.neu.model.Address;
import cn.edu.neu.model.Category;
import cn.edu.neu.model.Goods;
import cn.edu.neu.model.User;
import cn.edu.neu.service.AddressService;
import cn.edu.neu.service.GoodsService;
import cn.edu.neu.service.HomeService;

@WebServlet("*.action")

public class GoodsController extends HttpServlet {

	public GoodsController() {
		super();
		// TODO Auto-generated constructor stub
	}

	// @SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getServletPath();
		// 导航菜单商品分类下拉列表
		List<Category> catelist = HomeService.getCateList();
		req.setAttribute("catelist", catelist);

		if (url.equals("/goods/goodsCate.action")) {

			List<Goods> goodslist = new ArrayList<Goods>();
			int childid = Integer.parseInt(req.getParameter("childid"));
			goodslist = GoodsService.getCateGoods(childid);
			req.setAttribute("childid", childid);
//			System.out.println("========"+req.getParameter("childid"));
			req.setAttribute("goods", goodslist);
			req.getRequestDispatcher("/goods/goods_list.jsp").forward(req, resp);
		} else if (url.equals("/goods/newGoods.action")) {
			List<Goods> goodslist = new ArrayList<Goods>();
			goodslist = GoodsService.getNewgoods();

			req.setAttribute("newGoods", goodslist);

			req.getRequestDispatcher("/goods/new_goods.jsp").forward(req, resp);

			// 热销商品
		} else if (url.equals("/goods/saleGoods.action")) {
			List<Goods> goodslist = new ArrayList<Goods>();
			goodslist = GoodsService.getSalegoods();
			req.setAttribute("salesGoods", goodslist);
			req.getRequestDispatcher("/goods/sale_goods.jsp").forward(req, resp);
		}

		if (url.equals("/goods/goodsDetail.action")) {
			Goods goodsDetail = new Goods();
			int goods_id = Integer.parseInt(req.getParameter("goods_id"));
			goodsDetail = GoodsService.getGoodsDetail(goods_id);

			req.setAttribute("goodsDetail", goodsDetail);
			Category cate = new Category();

			cate = GoodsService.getCateName(goods_id);
			req.setAttribute("cate", cate);
			
			String activePage=req.getParameter("activePage");
			req.setAttribute("activePage", activePage);
			
			req.getRequestDispatcher("/goods/goods_detail.jsp").forward(req, resp);
			// 新到商品
		}
		// 加入购物车
		else if (url.equals("/goods/addCart.action"))

		{
			List<Goods> goodslist = new ArrayList<Goods>();
			Object ob = req.getSession().getAttribute("goodslist");
			resp.setContentType("text/json; charset=utf-8");
			PrintWriter out = resp.getWriter();
			if (ob != null) {
				goodslist = (List<Goods>) ob;

			}
			try {
				int goodsId = Integer.parseInt(req.getParameter("goodsId"));
				String goodsName = req.getParameter("goodsName");
				String goodsPic = req.getParameter("goodsPic");
				Float goodsPrice = Float.parseFloat(req.getParameter("goodsPrice"));
				Float goodsDiscount = Float.parseFloat(req.getParameter("goodsDiscount"));
				int goodsPostalfee = Integer.parseInt(req.getParameter("goodsPostalfee"));
				int goodsSales = Integer.parseInt(req.getParameter("goodsSales"));

				Goods good = new Goods();
				good.setGoodsPic(goodsPic);
				good.setGoodsId(goodsId);
				good.setGoodsName(goodsName);
				good.setGoodsPrice(goodsPrice);
				good.setGoodsDiscount(goodsDiscount);
				good.setGoodsPostalfee(goodsPostalfee);

				if (ob != null) {
					boolean find = false;
					int i = 0;
					for (; i < goodslist.size(); i++) {
						int goods_id = goodslist.get(i).getGoodsId();
						if (goods_id == goodsId) {
							System.out.println("--相等---" + i);
							find = true;
							break;
						}
					}
					if (find) {
						goodslist.get(i).setGoodsSales(goodslist.get(i).getGoodsSales() + goodsSales);
					} else {
						good.setGoodsSales(goodsSales);
						goodslist.add(good);
					}
				} else {
					System.out.println("goodsSales setGoodsSales");
					good.setGoodsSales(goodsSales);
					goodslist.add(good);
				}
				req.getSession().setAttribute("goodslist", goodslist);
				out.print("{\"success\":true,\"cartnum\":" + goodslist.size() + "}");
			} catch (Exception e) {
				e.printStackTrace();
				out.print("{\"success\":false}");
			}
			out.flush();
		} else if (url.equals("/goods/deleteCart.action")) {
			resp.setContentType("text/json; charset=utf-8");
			PrintWriter out = resp.getWriter();
			String index = req.getParameter("index");
			List<Goods> goodslist = new ArrayList<Goods>();
			Object ob = req.getSession().getAttribute("goodslist");
			if (ob != null && index != null) {
				goodslist = (List<Goods>) ob;
				goodslist.remove(Integer.parseInt(index));
			}
			req.getSession().setAttribute("goodslist", goodslist);
			out.print("{\"success\":true,\"cartnum\":" + goodslist.size() + "}");

			// 清空购物车
		} else if (url.equals("/goods/clearCart.action")) {
			resp.setContentType("text/json; charset=utf-8");
			PrintWriter out = resp.getWriter();
			req.getSession().removeAttribute("goodslist");
			out.print("{\"success\":true}");

			// 修改购物车中商品数量
		} else if (url.equals("/goods/changeCart.action")) {
			List<Goods> goodslist = new ArrayList<Goods>();
			Object ob = req.getSession().getAttribute("goodslist");
			if (ob != null) {
				goodslist = (List<Goods>) ob;
			}
			resp.setContentType("text/json; charset=utf-8");
			PrintWriter out = resp.getWriter();
			String index = req.getParameter("index");
			String goodsSales = req.getParameter("goodsSales");
			float totalAmount = 0;
			float totalPrice = 0;
			if (ob != null) {
				for (int i = 0; i < goodslist.size(); i++) {
					if (i == Integer.parseInt(index)) {
						goodslist.get(i).setGoodsSales(Integer.parseInt(goodsSales));
						totalPrice = goodslist.get(i).getGoodsSales() * goodslist.get(i).getGoodsDiscount();
					}
					totalAmount = totalAmount + goodslist.get(i).getGoodsSales() * goodslist.get(i).getGoodsDiscount();
				}
			}

			out.print("{\"success\":true,\"totalAmount\":" + totalAmount + ",\"totalPrice\":" + totalPrice + "}");
		} else if (url.equals("/goods/buyGoods.action")) {
			req.setAttribute("catelist", catelist);
			User logu = (User) req.getSession().getAttribute("_LOGIN_USER_");
			if (logu != null) {
				List<Address> addresses = AddressService.getAllAddress(logu.getUserId());
				req.setAttribute("addrs", addresses);
			}
			req.getRequestDispatcher("/order/buyGoods.jsp").forward(req, resp);
		}
		// 新到商品移动端接口json
		else if (url.equals("/goods/newGoodsForMobile.action")) {
			resp.setContentType("text/json; charset=utf-8");
			PrintWriter out = resp.getWriter();

			List<Goods> goodslist = new ArrayList<Goods>();
			goodslist = GoodsService.getNewgoods();

			ObjectMapper mapper = new ObjectMapper();
			String jsonlist = mapper.writeValueAsString(goodslist);
			System.out.println(jsonlist);
			out.print(jsonlist);
			out.flush();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}