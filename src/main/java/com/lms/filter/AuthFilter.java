package com.lms.filter;

import com.lms.model.User;
import java.io.IOException;
import java.util.Set;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

/** Servlet filter: login check + role based access control. */
@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final Set<String> PUBLIC = Set.of("/login", "/register");
    private static final Set<String> TEACHER_ONLY = Set.of("/course/new", "/lesson/add", "/assignment/add", "/grade", "/submissions");
    private static final Set<String> STUDENT_ONLY = Set.of("/enroll", "/submit", "/lesson/complete");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (PUBLIC.contains(path) || path.startsWith("/css/")) { chain.doFilter(request, response); return; }

        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null) { res.sendRedirect(req.getContextPath() + "/login"); return; }

        boolean allowed = true;
        if (path.startsWith("/admin/")) allowed = user.isAdmin();
        else if (TEACHER_ONLY.contains(path)) allowed = user.isInstructor() || user.isAdmin();
        else if (STUDENT_ONLY.contains(path)) allowed = user.isStudent();

        if (!allowed) { res.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have access to this page."); return; }
        chain.doFilter(request, response);
    }
}
