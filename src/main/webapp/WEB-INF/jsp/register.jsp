<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Register"/>
<%@ include file="header.jspf" %>
<div class="row justify-content-center"><div class="col-md-5">
  <div class="card p-4">
    <h4 class="mb-3">Create account</h4>
    <form method="post" action="${ctx}/register">
      <div class="mb-3"><label class="form-label">Full name</label><input name="name" class="form-control" value="<c:out value='${param.name}'/>" required></div>
      <div class="mb-3"><label class="form-label">Email</label><input type="email" name="email" class="form-control" value="<c:out value='${param.email}'/>" required></div>
      <div class="mb-3"><label class="form-label">Password (min 6 chars)</label><input type="password" name="password" class="form-control" required></div>
      <div class="mb-3"><label class="form-label">I am a</label>
        <select name="role" class="form-select"><option value="STUDENT">Student</option><option value="INSTRUCTOR">Instructor</option></select></div>
      <button class="btn btn-primary w-100">Register</button>
    </form>
    <p class="mt-3 mb-0 text-center">Already registered? <a href="${ctx}/login">Login</a></p>
  </div>
</div></div>
<%@ include file="footer.jspf" %>
