<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Courses"/>
<%@ include file="header.jspf" %>
<h3 class="mb-4">All courses</h3>
<div class="row">
  <c:forEach var="c" items="${courses}">
    <div class="col-md-4 mb-3"><div class="card h-100 p-3">
      <h6><c:out value="${c.title}"/></h6>
      <p class="text-muted small mb-1">by <c:out value="${c.instructorName}"/></p>
      <p class="small"><c:out value="${c.description}"/></p>
      <div class="mt-auto">
        <a class="btn btn-sm btn-outline-primary" href="${ctx}/course?id=${c.id}">View</a>
        <c:if test="${sessionScope.user.student}">
          <c:choose>
            <c:when test="${enrolledIds.contains(c.id)}"><span class="badge bg-success">Enrolled</span></c:when>
            <c:otherwise>
              <form method="post" action="${ctx}/enroll" class="d-inline"><input type="hidden" name="courseId" value="${c.id}"><button class="btn btn-sm btn-primary">Enroll</button></form>
            </c:otherwise>
          </c:choose>
        </c:if>
      </div>
    </div></div>
  </c:forEach>
  <c:if test="${empty courses}"><p class="text-muted">No courses available yet.</p></c:if>
</div>
<%@ include file="footer.jspf" %>
