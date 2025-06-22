<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Verificar si el usuario ha iniciado sesión --%>
<c:if test="${empty sessionScope.usuarioLogueado}">
    <c:redirect url="login.jsp"/>
</c:if>

<%-- Variable para el menú --%>
<c:set var="paginaActual" value="conexiones" scope="request" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Conexiones - Red Social Académica</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body>

<jsp:include page="componentes/header.jsp" />

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Mis Conexiones</h1>
        <%-- En el futuro, aquí podría ir un botón para buscar nuevos usuarios --%>
    </div>

    <div class="row">
        <c:choose>
            <c:when test="${not empty listaConexiones}">
                <%-- El bucle c:forEach itera sobre la lista de conexiones (amigos) --%>
                <c:forEach var="conexion" items="${listaConexiones}">
                    <div class="col-md-6 col-lg-4 mb-4">
                        <div class="card h-100">
                            <div class="card-body text-center">
                                <img src="https://via.placeholder.com/80" class="rounded-circle mb-3" alt="Foto de perfil">
                                <h5 class="card-title"><c:out value="${conexion.nombre} ${conexion.apellido}"/></h5>
                                <p class="card-text text-muted"><c:out value="${conexion.carrera}"/></p>
                                <a href="${pageContext.request.contextPath}/PerfilServlet?id=${conexion.id}" class="btn btn-outline-primary btn-sm">Ver Perfil</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="col-12">
                    <div class="alert alert-info">
                        Aún no tienes conexiones. ¡Busca a otros estudiantes y empieza a conectar!
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</div>

</body>
</html>
