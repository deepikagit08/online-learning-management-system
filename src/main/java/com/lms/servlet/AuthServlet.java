package com.lms.servlet;

import com.lms.dao.UserDao;
import com.lms.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet({"/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        switch (req.getServletPath()) {
            case "/logout" -> {
                HttpSession s = req.getSession(false);
                if (s != null) s.invalidate();
                res.sendRedirect(req.getContextPath() + "/login");
            }
            case "/register" -> req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, res);
            default -> req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if ("/register".equals(req.getServletPath())) register(req, res); else login(req, res);
    }

    private void login(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email = req.getParameter("email").trim().toLowerCase();
        User user = userDao.authenticate(email, req.getParameter("password"));
        if (user == null) {
            req.setAttribute("error", "Invalid email or password.");
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, res);
            return;
        }
        req.getSession().invalidate();                 // avoid session fixation
        req.getSession(true).setAttribute("user", user);
        res.sendRedirect(req.getContextPath() + "/dashboard");
    }

    private void register(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String name = req.getParameter("name").trim();
        String email = req.getParameter("email").trim().toLowerCase();
        String password = req.getParameter("password");
        String role = req.getParameter("role");

        String error = null;
        if (name.isEmpty() || email.isEmpty()) error = "Name and email are required.";
        else if (!email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) error = "Enter a valid email address.";
        else if (password == null || password.length() < 6) error = "Password must be at least 6 characters.";
        else if (!"STUDENT".equals(role) && !"INSTRUCTOR".equals(role)) error = "Choose a valid role.";
        else if (userDao.emailExists(email)) error = "That email is already registered.";

        if (error != null) {
            req.setAttribute("error", error);
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, res);
            return;
        }
        userDao.register(name, email, password, role);
        req.setAttribute("success", "Account created. Please log in.");
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, res);
    }
}
