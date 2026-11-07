<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="New course"/>
<%@ include file="header.jspf" %>
<div class="row justify-content-center"><div class="col-md-7"><div class="card p-4">
  <h4 class="mb-3">Create course</h4>
  <form method="post" action="${ctx}/course/new">
    <div class="mb-3"><label class="form-label">Title</label><input name="title" class="form-control" required></div>
    <div class="mb-3"><label class="form-label">Description</label><textarea name="description" rows="4" class="form-control"></textarea></div>
    <button class="btn btn-primary">Create</button> <a class="btn btn-link" href="${ctx}/dashboard">Cancel</a>
  </form>
</div></div></div>
<%@ include file="footer.jspf" %>
