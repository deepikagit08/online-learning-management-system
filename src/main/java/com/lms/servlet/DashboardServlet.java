package com.lms.servlet;

import com.lms.dao.*;
import com.lms.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private final CourseDao courseDao = new CourseDao();
    private final EnrollmentDao enrollmentDao = new EnrollmentDao();
    private final SubmissionDao submissionDao = new SubmissionDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user.isAdmin()) { res.sendRedirect(req.getContextPath() + "/admin/users"); return; }

        if (user.isInstructor()) {
            req.setAttribute("myCourses", courseDao.findByInstructor(user.getId()));
        } else {
            req.setAttribute("progress", enrollmentDao.coursesWithProgress(user.getId(), courseDao));
            req.setAttribute("grades", submissionDao.findByStudent(user.getId()));
        }
        req.getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(req, res);
    }
}
