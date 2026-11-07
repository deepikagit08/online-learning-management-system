<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Submissions"/>
<%@ include file="header.jspf" %>
<a href="${ctx}/course?id=${course.id}">&larr; Back to course</a>
<h4 class="my-3">Submissions: <c:out value="${assignment.title}"/> <small class="text-muted">(max ${assignment.maxMarks})</small></h4>
<c:forEach var="s" items="${submissions}">
  <div class="card p-3 mb-3">
    <strong><c:out value="${s.studentName}"/></strong>
    <pre class="content my-2"><c:out value="${s.answer}"/></pre>
    <form method="post" action="${ctx}/grade" class="d-flex gap-2 align-items-center">
      <input type="hidden" name="assignmentId" value="${assignment.id}"><input type="hidden" name="submissionId" value="${s.id}">
      <input type="number" name="marks" min="0" max="${assignment.maxMarks}" value="${s.marks}" class="form-control" style="max-width:120px" required>
      <button class="btn btn-sm btn-primary">Save grade</button>
      <c:if test="${s.graded}"><span class="badge bg-success">Graded</span></c:if>
    </form>
  </div>
</c:forEach>
<c:if test="${empty submissions}"><p class="text-muted">No submissions yet.</p></c:if>
<%@ include file="footer.jspf" %>
