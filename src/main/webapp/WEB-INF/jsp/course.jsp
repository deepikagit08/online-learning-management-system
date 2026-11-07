<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="${course.title}"/>
<%@ include file="header.jspf" %>
<div class="card p-4 mb-4">
  <h3><c:out value="${course.title}"/></h3>
  <p class="text-muted">Instructor: <c:out value="${course.instructorName}"/></p>
  <p><c:out value="${course.description}"/></p>
  <c:if test="${sessionScope.user.student && !enrolled}">
    <form method="post" action="${ctx}/enroll"><input type="hidden" name="courseId" value="${course.id}"><button class="btn btn-primary">Enroll in this course</button></form>
  </c:if>
</div>

<c:if test="${canView}">
  <h5>Lessons</h5>
  <div class="accordion mb-4" id="lessons">
    <c:forEach var="l" items="${lessons}" varStatus="st">
      <div class="accordion-item">
        <h2 class="accordion-header"><button class="accordion-button collapsed" data-bs-toggle="collapse" data-bs-target="#l${l.id}">
          ${st.count}. <c:out value="${l.title}"/> <c:if test="${l.completed}"><span class="badge bg-success ms-2">Done</span></c:if></button></h2>
        <div id="l${l.id}" class="accordion-collapse collapse"><div class="accordion-body">
          <pre class="content"><c:out value="${l.content}"/></pre>
          <c:if test="${sessionScope.user.student && !l.completed}">
            <form method="post" action="${ctx}/lesson/complete" class="mt-3">
              <input type="hidden" name="courseId" value="${course.id}"><input type="hidden" name="lessonId" value="${l.id}">
              <button class="btn btn-sm btn-success">Mark as complete</button></form>
          </c:if>
        </div></div>
      </div>
    </c:forEach>
  </div>
  <c:if test="${empty lessons}"><p class="text-muted">No lessons yet.</p></c:if>

  <c:if test="${canManage}">
    <div class="card p-3 mb-4"><h6>Add lesson</h6>
      <form method="post" action="${ctx}/lesson/add">
        <input type="hidden" name="courseId" value="${course.id}">
        <input name="title" class="form-control mb-2" placeholder="Lesson title" required>
        <textarea name="content" rows="4" class="form-control mb-2" placeholder="Lesson content"></textarea>
        <button class="btn btn-primary btn-sm">Add lesson</button>
      </form></div>
  </c:if>

  <h5>Assignments</h5>
  <c:forEach var="a" items="${assignments}">
    <div class="card p-3 mb-3">
      <div class="d-flex justify-content-between">
        <h6 class="mb-1"><c:out value="${a.title}"/> <span class="badge bg-secondary">${a.maxMarks} marks</span></h6>
        <c:if test="${canManage}"><a class="btn btn-sm btn-outline-primary" href="${ctx}/submissions?assignmentId=${a.id}">View submissions</a></c:if>
      </div>
      <p class="small"><c:out value="${a.description}"/></p>
      <c:if test="${sessionScope.user.student}">
        <c:if test="${a.mySubmission.graded}"><div class="alert alert-success py-1">Graded: ${a.mySubmission.marks} / ${a.maxMarks}</div></c:if>
        <c:if test="${not empty a.mySubmission && !a.mySubmission.graded}"><div class="alert alert-warning py-1">Submitted &mdash; awaiting grade</div></c:if>
        <form method="post" action="${ctx}/submit">
          <input type="hidden" name="assignmentId" value="${a.id}">
          <textarea name="answer" rows="3" class="form-control mb-2" placeholder="Your answer" required><c:out value="${a.mySubmission.answer}"/></textarea>
          <button class="btn btn-sm btn-primary">${empty a.mySubmission ? 'Submit' : 'Re-submit'}</button>
        </form>
      </c:if>
    </div>
  </c:forEach>
  <c:if test="${empty assignments}"><p class="text-muted">No assignments yet.</p></c:if>

  <c:if test="${canManage}">
    <div class="card p-3"><h6>Create assignment</h6>
      <form method="post" action="${ctx}/assignment/add">
        <input type="hidden" name="courseId" value="${course.id}">
        <input name="title" class="form-control mb-2" placeholder="Assignment title" required>
        <textarea name="description" rows="3" class="form-control mb-2" placeholder="Instructions"></textarea>
        <input type="number" name="maxMarks" value="100" min="1" class="form-control mb-2" style="max-width:160px">
        <button class="btn btn-primary btn-sm">Create</button>
      </form></div>
  </c:if>
</c:if>
<c:if test="${!canView}"><div class="alert alert-secondary">Enroll in this course to see its lessons and assignments.</div></c:if>
<%@ include file="footer.jspf" %>
