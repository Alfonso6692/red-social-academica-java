<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Mensajes - Red Social Académica</title>
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
    <h2>💬 Mensajes</h2>
    <div>
      <a href="dashboard.jsp" class="btn btn-outline-secondary">← Dashboard</a>
      <a href="enviar-mensaje.jsp" class="btn btn-primary">+ Enviar Mensaje</a>
    </div>
  </div>

  <!-- Información del usuario -->
  <div class="alert alert-info">
    <strong>Bienvenido/a:</strong> ${sessionScope.usuario.nombreCompleto}
    <br><small>Estado: En línea 🟢</small>
  </div>

  <!-- Mensajes -->
  <c:if test="${not empty param.exito}">
    <div class="alert alert-success">✅ ${param.exito}</div>
  </c:if>
  <c:if test="${not empty param.error}">
    <div class="alert alert-danger">❌ ${param.error}</div>
  </c:if>

  <!-- Pestañas -->
  <ul class="nav nav-tabs" id="mensajeTabs" role="tablist">
    <li class="nav-item" role="presentation">
      <button class="nav-link active" id="recibidos-tab" data-bs-toggle="tab" data-bs-target="#recibidos"
              type="button" role="tab" aria-controls="recibidos" aria-selected="true">
        📥 Recibidos
        <c:if test="${mensajesNoLeidos > 0}">
          <span class="badge bg-danger">${mensajesNoLeidos}</span>
        </c:if>
      </button>
    </li>
    <li class="nav-item" role="presentation">
      <button class="nav-link" id="enviados-tab" data-bs-toggle="tab" data-bs-target="#enviados"
              type="button" role="tab" aria-controls="enviados" aria-selected="false">
        📤 Enviados
      </button>
    </li>
  </ul>

  <!-- Contenido de las pestañas -->
  <div class="tab-content" id="mensajeTabContent">
    <!-- Mensajes Recibidos -->
    <div class="tab-pane fade show active" id="recibidos" role="tabpanel" aria-labelledby="recibidos-tab">
      <div class="mt-3">
        <c:choose>
          <c:when test="${not empty mensajesRecibidos}">
            <c:forEach var="mensaje" items="${mensajesRecibidos}">
              <div class="card mb-2 ${mensaje.leido ? '' : 'border-primary'}">
                <div class="card-body">
                  <div class="d-flex justify-content-between">
                    <h6 class="card-title mb-1">
                      De: Usuario ${mensaje.idEmisor}
                      <c:if test="${!mensaje.leido}">
                        <span class="badge bg-primary">Nuevo</span>
                      </c:if>
                    </h6>
                    <small class="text-muted">${mensaje.fechaFormateada}</small>
                  </div>
                  <p class="card-text">${mensaje.texto}</p>
                  <div class="btn-group btn-group-sm">
                    <button class="btn btn-outline-primary">Responder</button>
                    <c:if test="${!mensaje.leido}">
                      <button class="btn btn-outline-success">Marcar como leído</button>
                    </c:if>
                  </div>
                </div>
              </div>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <div class="text-center py-4">
              <h5>📭 No tienes mensajes recibidos</h5>
              <p class="text-muted">Los mensajes que recibas aparecerán aquí</p>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </div>

    <!-- Mensajes Enviados -->
    <div class="tab-pane fade" id="enviados" role="tabpanel" aria-labelledby="enviados-tab">
      <div class="mt-3">
        <c:choose>
          <c:when test="${not empty mensajesEnviados}">
            <c:forEach var="mensaje" items="${mensajesEnviados}">
              <div class="card mb-2">
                <div class="card-body">
                  <div class="d-flex justify-content-between">
                    <h6 class="card-title mb-1">Para: Usuario ${mensaje.idReceptor}</h6>
                    <small class="text-muted">${mensaje.fechaFormateada}</small>
                  </div>
                  <p class="card-text">${mensaje.texto}</p>
                  <small class="text-muted">
                    Estado: ${mensaje.leido ? '✅ Leído' : '📩 Enviado'}
                  </small>
                </div>
              </div>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <div class="text-center py-4">
              <h5>📤 No has enviado mensajes</h5>
              <p class="text-muted">Los mensajes que envíes aparecerán aquí</p>
              <a href="enviar-mensaje.jsp" class="btn btn-primary">Enviar Primer Mensaje</a>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </div>

  <!-- Estadísticas -->
  <div class="mt-4 p-3 bg-light rounded">
    <h6>📊 Estadísticas de Mensajería:</h6>
    <div class="row text-center">
      <div class="col-md-3">
        <strong>Total Recibidos</strong>
        <div class="fs-5">${mensajesRecibidos != null ? mensajesRecibidos.size() : 0}</div>
      </div>
      <div class="col-md-3">
        <strong>Total Enviados</strong>
        <div class="fs-5">${mensajesEnviados != null ? mensajesEnviados.size() : 0}</div>
      </div>
      <div class="col-md-3">
        <strong>No Leídos</strong>
        <div class="fs-5 text-danger">${mensajesNoLeidos != null ? mensajesNoLeidos : 0}</div>
      </div>
      <div class="col-md-3">
        <strong>Estado</strong>
        <div class="fs-5 text-success">En línea 🟢</div>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>