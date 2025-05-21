package com.myapp;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class ColorServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String color = request.getParameter("color");
        HttpSession session = request.getSession();
        session.setAttribute("color", color);
        response.sendRedirect("color.jsp");
    }
}
