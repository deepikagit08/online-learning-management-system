<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Admin"/>
<%@ include file="header.jspf" %>
<h3 class="mb-4">Admin Panel</h3>
<h5>Users (${users.size()})</h5>
<table class="table table-sm bg-white mb-4">
  <thead><tr><th>Name</th><th>Email</th><th>Role</th><th></th></tr></thead>
  <c:forEach var="u" items="${users}">
    <tr><td><c:out value="${u.name}"/></td><td><c:out value="${u.email}"/></td><td>${u.role}</td>
      <td><c:if test="${!u.admin}"><form method="post" action="${ctx}/admin/delete" onsubmit="return confirm('Delete this user?')">
        <input type="hidden" name="type" value="user"><input type="hidden" name="id" value="${u.id}"><button class="btn btn-sm btn-outline-danger">Delete</button></form></c:if></td></tr>
  </c:forEach>
</table>
<h5>Courses (${courses.size()})</h5>
<table class="table table-sm bg-white">
  <thead><tr><th>Title</th><th>Instructor</th><th></th></tr></thead>
  <c:forEach var="c" items="${courses}">
    <tr><td><a href="${ctx}/course?id=${c.id}"><c:out value="${c.title}"/></a></td><td><c:out value="${c.instructorName}"/></td>
      <td><form method="post" action="${ctx}/admin/delete" onsubmit="return confirm('Delete this course?')">
        <input type="hidden" name="type" value="course"><input type="hidden" name="id" value="${c.id}"><button class="btn btn-sm btn-outline-danger">Delete</button></form></td></tr>
  </c:forEach>
</table>
<%@ include file="footer.jspf" %>
