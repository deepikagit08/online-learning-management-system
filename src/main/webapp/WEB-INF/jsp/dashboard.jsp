<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Dashboard"/>
<%@ include file="header.jspf" %>
<h3 class="mb-4">${sessionScope.user.homeTitle}</h3>

<c:if test="${sessionScope.user.instructor}">
  <div class="d-flex justify-content-between mb-3"><h5>My courses</h5><a class="btn btn-primary" href="${ctx}/course/new">+ New course</a></div>
  <div class="row">
    <c:forEach var="c" items="${myCourses}">
      <div class="col-md-4 mb-3"><div class="card h-100 p-3">
        <h6><c:out value="${c.title}"/></h6>
        <p class="text-muted small"><c:out value="${c.description}"/></p>
        <a class="btn btn-sm btn-outline-primary mt-auto" href="${ctx}/course?id=${c.id}">Manage</a>
      </div></div>
    </c:forEach>
    <c:if test="${empty myCourses}"><p class="text-muted">You have not created any courses yet.</p></c:if>
  </div>
</c:if>

<c:if test="${sessionScope.user.student}">
  <h5>My enrolled courses</h5>
  <div class="row mb-4">
    <c:forEach var="e" items="${progress}">
      <div class="col-md-4 mb-3"><div class="card h-100 p-3">
        <h6><c:out value="${e.key.title}"/></h6>
        <div class="progress mb-2"><div class="progress-bar" style="width:${e.value}%">${e.value}%</div></div>
        <a class="btn btn-sm btn-outline-primary" href="${ctx}/course?id=${e.key.id}">Continue</a>
      </div></div>
    </c:forEach>
    <c:if test="${empty progress}"><p class="text-muted">Not enrolled yet. <a href="${ctx}/courses">Browse courses</a></p></c:if>
  </div>
  <h5>My grades</h5>
  <table class="table table-sm bg-white">
    <thead><tr><th>Assignment</th><th>Status</th><th>Marks</th></tr></thead>
    <c:forEach var="g" items="${grades}">
      <tr><td><c:out value="${g.assignmentTitle}"/></td>
          <td>${g.graded ? 'Graded' : 'Awaiting grade'}</td><td>${g.graded ? g.marks : '-'}</td></tr>
    </c:forEach>
  </table>
</c:if>
<%@ include file="footer.jspf" %>
