package com.yc.uploadServer.web.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

import com.yc.utils.UploadUtil;

@WebServlet("/productServlet.action")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProductServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		UploadUtil uu = new UploadUtil();

		// ???在jsp中如何取得 PageContext
		PageContext pageContext = JspFactory.getDefaultFactory()
				.getPageContext(this, request, response, null, true, 8 * 1024,
						true);
		Map<String, String> map = null;
		try {
			map = uu.uploadFile(pageContext);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(map);
		// TODO:响应..

	}

}
