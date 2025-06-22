<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty sessionScope.usuarioLogueado}">
    <c:redirect url="login.jsp"/>
</c:if>

<c:set var="paginaActual" value="grupos" scope="request" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Grupos de Estudio - Red Social Académica</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<jsp:include page="componentes/header.jsp" />

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Grupos de Estudio</h1>
        <a href="crear_grupo.jsp" class="btn btn-primary">Crear Nuevo Grupo</a>
    </div>

    <div class="list-group">
        <%-- Usamos c:choose para mostrar un mensaje si no hay grupos --%>
        <c:choose>
            <c:when test="${not empty listaGrupos}">
                <%-- El bucle c:forEach itera sobre la lista de grupos enviada por el servlet --%>
                <c:forEach var="grupo" items="${listaGrupos}">
                    <a href="#" class="list-group-item list-group-item-action">
                        <div class="d-flex w-100 justify-content-between">
                            <h5 class="mb-1"><c:out value="${grupo.nombre}"/></h5>
                                <%-- En el futuro aquí se podría mostrar el número de miembros --%>
                        </div>
                        <p class="mb-1"><c:out value="${grupo.descripcion}"/></p>
                        <small class="text-muted">Temas: <c:out value="${grupo.temas}"/></small>
                    </a>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">
                    Aún no hay grupos creados. ¡Sé el primero en crear uno!
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</div>

</body>
</html>
