<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Grupos de Estudio - Red Social Académica</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<!-- Verificar sesión -->
<c:if test="${empty sessionScope.usuario}">
  <c:redirect url="login.jsp"/>
</c:if>

<div class="container mt-4">
  <!-- Header -->
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h2>👥 Grupos de Estudio</h2>
    <div>
      <a href="dashboard.jsp" class="btn btn-outline-secondary">← Dashboard</a>
      <a href="crear-grupo.jsp" class="btn btn-primary">+ Crear Grupo</a>
    </div>
  </div>

  <!-- Información del usuario -->
  <div class="alert alert-info">
    <strong>Bienvenido/a:</strong> ${sessionScope.usuario.nombreCompleto}
    <br><small>${sessionScope.usuario.infoAcademica}</small>
  </div>

  <!-- Mensajes -->
  <c:if test="${not empty param.exito}">
    <div class="alert alert-success">✅ ${param.exito}</div>
  </c:if>
  <c:if test="${not empty param.error}">
    <div class="alert alert-danger">❌ ${param.error}</div>
  </c:if>

  <!-- Lista de grupos -->
  <div class="row">
    <c:choose>
      <c:when test="${not empty listaDeGrupos}">
        <c:forEach var="grupo" items="${listaDeGrupos}">
          <div class="col-md-6 col-lg-4 mb-3">
            <div class="card h-100">
              <div class="card-body">
                <h5 class="card-title">${grupo.nombre}</h5>
                <p class="card-text">${grupo.resumenDescripcion}</p>
                <p class="text-muted small">
                  <strong>Temas:</strong> ${grupo.temas}
                </p>
              </div>
              <div class="card-footer">
                <button class="btn btn-primary btn-sm">Ver Detalles</button>
                <button class="btn btn-outline-success btn-sm">Unirse</button>
              </div>
            </div>
          </div>
        </c:forEach>
      </c:when>
      <c:otherwise>
        <div class="col-12">
          <div class="text-center py-5">
            <h4>📭 No hay grupos disponibles</h4>
            <p class="text-muted">Sé el primero en crear un grupo de estudio</p>
            <a href="crear-grupo.jsp" class="btn btn-primary">+ Crear Primer Grupo</a>
          </div>
        </div>
      </c:otherwise>
    </c:choose>
  </div>

  <!-- Estadísticas básicas -->
  <div class="mt-4 p-3 bg-light rounded">
    <h6>📊 Estadísticas:</h6>
    <div class="row text-center">
      <div class="col-md-4">
        <strong>Total de Grupos</strong>
        <div class="fs-4">${listaDeGrupos != null ? listaDeGrupos.size() : 0}</div>
      </div>
      <div class="col-md-4">
        <strong>Mis Grupos</strong>
        <div class="fs-4">-</div>
      </div>
      <div class="col-md-4">
        <strong>Grupos Populares</strong>
        <div class="fs-4">-</div>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>