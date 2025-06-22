<%--
  Created by IntelliJ IDEA.
  User: ASUS-VASQUEZ
  Date: 21/06/2025
  Time: 09:26 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty sessionScope.usuarioLogueado}">
  <c:redirect url="login.jsp"/>
</c:if>

<%-- Variable para el menú --%>
<c:set var="paginaActual" value="notificaciones" scope="request" />

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Notificaciones - Red Social Académica</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body>

<jsp:include page="componentes/header.jsp" />

<div class="container mt-4">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h1>Tus Notificaciones</h1>
  </div>

  <div class="list-group">
    <c:choose>
      <c:when test="${not empty listaNotificaciones}">
        <c:forEach var="notif" items="${listaNotificaciones}">
          <div class="list-group-item list-group-item-action ${notif.leida ? 'text-muted' : 'fw-bold'}">
            <div class="d-flex w-100 justify-content-between">
              <p class="mb-1"><i class="bi bi-info-circle-fill me-2"></i><c:out value="${notif.mensaje}"/></p>
              <small><c:out value="${notif.fecha}"/></small>
            </div>
            <c:if test="${not notif.leida}">
              <a href="${pageContext.request.contextPath}/NotificacionServlet?accion=marcarLeida&idNotificacion=${notif.id}" class="btn btn-sm btn-outline-secondary mt-2">Marcar como leída</a>
            </c:if>
          </div>
        </c:forEach>
      </c:when>
      <c:otherwise>
        <div class="alert alert-info" role="alert">
          No tienes notificaciones nuevas.
        </div>
      </c:otherwise>
    </c:choose>
  </div>

</div>

</body>
</html>
