<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Verificar si el usuario ha iniciado sesión --%>
<c:if test="${empty sessionScope.usuarioLogueado}">
    <c:redirect url="login.jsp"/>
</c:if>

<%-- Variable para el menú --%>
<c:set var="paginaActual" value="grupos" scope="request" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Nuevo Grupo - Red Social Académica</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body { background-color: #f0f2f5; }
        .form-container {
            max-width: 700px;
            margin: 50px auto;
            background-color: #fff;
            padding: 2rem;
            border-radius: 0.75rem;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>

<jsp:include page="componentes/header.jsp" />

<div class="container">
    <div class="form-container">
        <h2 class="text-center mb-4">Crear un Nuevo Grupo de Estudio</h2>

        <form action="${pageContext.request.contextPath}/GrupoServlet" method="POST">

            <%-- Campo oculto para indicar la acción al servlet --%>
            <input type="hidden" name="accion" value="crear">

            <div class="mb-3">
                <label for="nombre" class="form-label fw-bold">Nombre del Grupo</label>
                <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Ej: Programación Competitiva Avanzada" required>
            </div>

            <div class="mb-3">
                <label for="descripcion" class="form-label fw-bold">Descripción</label>
                <textarea class="form-control" id="descripcion" name="descripcion" rows="3" placeholder="Una breve descripción sobre el propósito y las metas del grupo." required></textarea>
            </div>

            <div class="mb-3">
                <label for="categoriaId" class="form-label fw-bold">Categoría</label>
                <%-- En una versión más avanzada, esto sería un menú desplegable cargado desde la BD --%>
                <input type="text" class="form-control" id="categoriaId" name="categoriaId" placeholder="Ej: Ingeniería de Software" required>
                <div class="form-text">
                    Ingresa la categoría principal a la que pertenece el grupo.
                </div>
            </div>

            <div class="mb-4">
                <label for="temas" class="form-label fw-bold">Temas Principales (separados por comas)</label>
                <input type="text" class="form-control" id="temas" name="temas" placeholder="Ej: grafos, programación dinámica, estructuras de datos" required>
            </div>

            <%-- Contenedor para mostrar mensajes de error que puedan venir del servlet --%>
            <c:if test="${not empty param.error}">
                <div class="alert alert-danger" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    <c:out value="${param.error}"/>
                </div>
            </c:if>

            <div class="d-grid">
                <button type="submit" class="btn btn-primary btn-lg">Crear Grupo</button>
            </div>
        </form>
        <div class="text-center mt-3">
            <a href="${pageContext.request.contextPath}/GrupoServlet?accion=listar">Volver a la lista de grupos</a>
        </div>
    </div>
</div>

</body>
</html>
