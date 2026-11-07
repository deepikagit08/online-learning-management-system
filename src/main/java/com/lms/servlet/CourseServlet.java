package com.lms.servlet;

import com.lms.dao.*;
import com.lms.model.*;
import java.io.IOException;
import java.util.HashSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet({"/courses", "/course", "/course/new", "/lesson/add", "/enroll", "/lesson/complete"})
public class CourseServlet extends HttpServlet {
    private final CourseDao courseDao = new CourseDao();
    private final LessonDao lessonDao = new LessonDao();
    private final AssignmentDao assignmentDao = new AssignmentDao();
    private final EnrollmentDao enrollmentDao = new EnrollmentDao();

    private User current(HttpServletRequest req) { return (User) req.getSession().getAttribute("user"); }

    private boolean canManage(User u, Course c) { return u.isAdmin() || c.getInstructorId() == u.getId(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User user = current(req);
        switch (req.getServletPath()) {
            case "/courses" -> {
                req.setAttribute("courses", courseDao.findAll());
                req.setAttribute("enrolledIds", user.isStudent() ? enrollmentDao.enrolledCourseIds(user.getId()) : new HashSet<Integer>());
                req.getRequestDispatcher("/WEB-INF/jsp/courses.jsp").forward(req, res);
            }
            case "/course/new" -> req.getRequestDispatcher("/WEB-INF/jsp/course-form.jsp").forward(req, res);
            default -> showCourse(req, res, user);
        }
    }

    private void showCourse(HttpServletRequest req, HttpServletResponse res, User user) throws ServletException, IOException {
        Course course = courseDao.findById(parseInt(req.getParameter("id")));
        if (course == null) { res.sendError(404, "Course not found"); return; }
        int studentId = user.isStudent() ? user.getId() : 0;
        boolean enrolled = user.isStudent() && enrollmentDao.isEnrolled(user.getId(), course.getId());

        req.setAttribute("course", course);
        req.setAttribute("canManage", canManage(user, course));
        req.setAttribute("enrolled", enrolled);
        // Only enrolled students and the owner/admin can see lesson content
        boolean canView = enrolled || canManage(user, course);
        req.setAttribute("canView", canView);
        if (canView) {
            req.setAttribute("lessons", lessonDao.findByCourse(course.getId(), studentId));
            req.setAttribute("assignments", assignmentDao.findByCourse(course.getId(), studentId));
        }
        req.getRequestDispatcher("/WEB-INF/jsp/course.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User user = current(req);
        String ctx = req.getContextPath();
        switch (req.getServletPath()) {
            case "/course/new" -> {
                String title = req.getParameter("title").trim();
                if (title.isEmpty()) {
                    req.setAttribute("error", "Title is required.");
                    req.getRequestDispatcher("/WEB-INF/jsp/course-form.jsp").forward(req, res);
                    return;
                }
                int id = courseDao.create(title, req.getParameter("description"), user.getId());
                res.sendRedirect(ctx + "/course?id=" + id);
            }
            case "/lesson/add" -> {
                Course c = courseDao.findById(parseInt(req.getParameter("courseId")));
                if (c == null || !canManage(user, c)) { res.sendError(403); return; }
                lessonDao.add(c.getId(), req.getParameter("title").trim(), req.getParameter("content"));
                res.sendRedirect(ctx + "/course?id=" + c.getId() + "&msg=Lesson+added");
            }
            case "/enroll" -> {
                int courseId = parseInt(req.getParameter("courseId"));
                enrollmentDao.enroll(user.getId(), courseId);
                res.sendRedirect(ctx + "/course?id=" + courseId + "&msg=Enrolled+successfully");
            }
            case "/lesson/complete" -> {
                int courseId = parseInt(req.getParameter("courseId"));
                if (!enrollmentDao.isEnrolled(user.getId(), courseId)) { res.sendError(403); return; }
                lessonDao.markComplete(user.getId(), parseInt(req.getParameter("lessonId")));
                res.sendRedirect(ctx + "/course?id=" + courseId);
            }
            default -> res.sendError(404);
        }
    }

    static int parseInt(String s) {
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return -1; }
    }
}
