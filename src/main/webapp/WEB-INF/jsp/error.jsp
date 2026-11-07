<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Error"/>
<%@ include file="header.jspf" %>
<div class="alert alert-danger"><h5>Something went wrong</h5>
  <p class="mb-0"><c:out value="${pageContext.exception.message}"/></p></div>
<a href="${ctx}/dashboard">Back to dashboard</a>
<%@ include file="footer.jspf" %>
