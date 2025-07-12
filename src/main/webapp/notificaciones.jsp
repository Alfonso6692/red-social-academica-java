<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty sessionScope.usuarioLogueado}"><c:redirect url="login.jsp"/></c:if>
<!DOCTYPE html>
<html lang="es">
<head>
  <title>Notificaciones</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="componentes/header.jsp" />
<div class="container mt-4">
  <h2>Tus Notificaciones</h2>
  <div class="list-group mt-3">
    <c:choose>
      <c:when test="${not empty listaNotificaciones}">
        <c:forEach var="notif" items="${listaNotificaciones}">
          <div class="list-group-item">
            <p class="mb-1"><c:out value="${notif.mensaje}"/></p>
            <small class="text-muted"><c:out value="${notif.fecha}"/></small>
          </div>
        </c:forEach>
      </c:when>
      <c:otherwise>
        <p>No tienes notificaciones.</p>
      </c:otherwise>
    </c:choose>
  </div>
</div>
</body>
</html>
