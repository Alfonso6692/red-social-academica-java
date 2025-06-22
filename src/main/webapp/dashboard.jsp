<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Verificar si el usuario ha iniciado sesión --%>
<c:if test="${empty sessionScope.usuarioLogueado}">
    <c:redirect url="login.jsp"/>
</c:if>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Red Social Académica</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<%-- Incluir el encabezado/navegación --%>
<jsp:include page="componentes/header.jsp" />

<div class="container mt-4">
    <div class="p-5 mb-4 bg-light rounded-3">
        <div class="container-fluid py-5">
            <h1 class="display-5 fw-bold">Bienvenido, <c:out value="${sessionScope.usuarioLogueado.nombre}"/></h1>
            <p class="col-md-8 fs-4">Aquí puedes ver las últimas actualizaciones de tus grupos, recursos recientes y notificaciones. ¡Explora y conecta con otros estudiantes!</p>
        </div>
    </div>

    <div class="row align-items-md-stretch">
        <div class="col-md-6">
            <div class="h-100 p-5 text-white bg-dark rounded-3">
                <h2>Tus Grupos</h2>
                <p>Accede a tus grupos de estudio, comparte archivos y organiza tus proyectos.</p>
                <a href="GrupoServlet?accion=listar" class="btn btn-outline-light" type="button">Ver mis grupos</a>
            </div>
        </div>
        <div class="col-md-6">
            <div class="h-100 p-5 bg-light border rounded-3">
                <h2>Recursos Educativos</h2>
                <p>Busca y comparte recursos educativos con otros estudiantes de la comunidad.</p>
                <a href="RecursoServlet?accion=listar" class="btn btn-outline-secondary" type="button">Explorar recursos</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>
