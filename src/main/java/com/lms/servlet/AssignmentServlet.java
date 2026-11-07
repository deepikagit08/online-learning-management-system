package com.lms.servlet;

import com.lms.dao.*;
import com.lms.model.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet({"/assignment/add", "/submit", "/grade", "/submissions"})
public class AssignmentServlet extends HttpServlet {
    private final CourseDao courseDao = new CourseDao();
    private final AssignmentDao assignmentDao = new AssignmentDao();
    private final SubmissionDao submissionDao = new SubmissionDao();
    private final EnrollmentDao enrollmentDao = new EnrollmentDao();

    private boolean canManage(User u, Course c) { return c != null && (u.isAdmin() || c.getInstructorId() == u.getId()); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        Assignment a = assignmentDao.findById(CourseServlet.parseInt(req.getParameter("assignmentId")));
        Course c = a == null ? null : courseDao.findById(a.getCourseId());
        if (!canManage(user, c)) { res.sendError(403); return; }
        req.setAttribute("assignment", a);
        req.setAttribute("course", c);
        req.setAttribute("submissions", submissionDao.findByAssignment(a.getId()));
        req.getRequestDispatcher("/WEB-INF/jsp/submissions.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String ctx = req.getContextPath();
        switch (req.getServletPath()) {
            case "/assignment/add" -> {
                Course c = courseDao.findById(CourseServlet.parseInt(req.getParameter("courseId")));
                if (!canManage(user, c)) { res.sendError(403); return; }
                int max = CourseServlet.parseInt(req.getParameter("maxMarks"));
                assignmentDao.create(c.getId(), req.getParameter("title").trim(), req.getParameter("description"), max > 0 ? max : 100);
                res.sendRedirect(ctx + "/course?id=" + c.getId() + "&msg=Assignment+created");
            }
            case "/submit" -> {
                Assignment a = assignmentDao.findById(CourseServlet.parseInt(req.getParameter("assignmentId")));
                if (a == null || !enrollmentDao.isEnrolled(user.getId(), a.getCourseId())) { res.sendError(403); return; }
                submissionDao.submit(a.getId(), user.getId(), req.getParameter("answer"));
                res.sendRedirect(ctx + "/course?id=" + a.getCourseId() + "&msg=Submitted");
            }
            case "/grade" -> {
                Assignment a = assignmentDao.findById(CourseServlet.parseInt(req.getParameter("assignmentId")));
                Course c = a == null ? null : courseDao.findById(a.getCourseId());
                if (!canManage(user, c)) { res.sendError(403); return; }
                int marks = CourseServlet.parseInt(req.getParameter("marks"));
                if (marks < 0 || marks > a.getMaxMarks()) {
                    res.sendRedirect(ctx + "/submissions?assignmentId=" + a.getId() + "&msg=Marks+must+be+between+0+and+" + a.getMaxMarks());
                    return;
                }
                submissionDao.grade(CourseServlet.parseInt(req.getParameter("submissionId")), marks);
                res.sendRedirect(ctx + "/submissions?assignmentId=" + a.getId() + "&msg=Grade+saved");
            }
            default -> res.sendError(404);
        }
    }
}
