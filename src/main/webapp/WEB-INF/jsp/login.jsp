<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Login"/>
<%@ include file="header.jspf" %>
<div class="row justify-content-center"><div class="col-md-5">
  <div class="card p-4">
    <h4 class="mb-3">Login</h4>
    <form method="post" action="${ctx}/login">
      <div class="mb-3"><label class="form-label">Email</label><input type="email" name="email" class="form-control" required></div>
      <div class="mb-3"><label class="form-label">Password</label><input type="password" name="password" class="form-control" required></div>
      <button class="btn btn-primary w-100">Login</button>
    </form>
    <p class="mt-3 mb-0 text-center">New here? <a href="${ctx}/register">Create an account</a></p>
  </div>
</div></div>
<%@ include file="footer.jspf" %>
