package com.lms.servlet;

import com.lms.dao.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet({"/admin/users", "/admin/delete"})
public class AdminServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();
    private final CourseDao courseDao = new CourseDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("users", userDao.findAll());
        req.setAttribute("courses", courseDao.findAll());
        req.getRequestDispatcher("/WEB-INF/jsp/admin.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = CourseServlet.parseInt(req.getParameter("id"));
        boolean ok = "course".equals(req.getParameter("type")) ? courseDao.delete(id) : userDao.delete(id);
        res.sendRedirect(req.getContextPath() + "/admin/users?msg=" + (ok ? "Deleted" : "Nothing+deleted"));
    }
}
