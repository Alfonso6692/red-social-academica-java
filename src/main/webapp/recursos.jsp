<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty sessionScope.usuarioLogueado}">
    <c:redirect url="login.jsp"/>
</c:if>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recursos Educativos - Red Social Académica</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<jsp:include page="componentes/header.jsp" />

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Recursos Educativos</h1>
        <a href="subir_recurso.jsp" class="btn btn-primary">Subir Nuevo Recurso</a>
    </div>

    <!-- Formulario de búsqueda -->
    <form action="RecursoServlet" method="GET" class="mb-4">
        <input type="hidden" name="accion" value="buscar">
        <div class="input-group">
            <input type="text" class="form-control" placeholder="Buscar por etiqueta (ej. java, sql, grafos)" name="etiqueta">
            <button class="btn btn-outline-secondary" type="submit">Buscar</button>
        </div>
    </form>

    <!-- Tabla para mostrar los recursos -->
    <table class="table table-hover">
        <thead>
        <tr>
            <th scope="col">Título</th>
            <th scope="col">Tipo</th>
            <th scope="col">Fecha de Publicación</th>
            <th scope="col">Etiquetas</th>
        </tr>
        </thead>
        <tbody>
        <%-- Aquí se iteraría sobre la lista de recursos con JSTL --%>
        <%-- <c:forEach var="recurso" items="${listaRecursos}">
            <tr>
                <td><a href="${recurso.url}"><c:out value="${recurso.titulo}"/></a></td>
                <td><span class="badge bg-secondary"><c:out value="${recurso.tipo}"/></span></td>
                <td><c:out value="${recurso.fechaPublicacion}"/></td>
                <td><c:out value="${recurso.etiquetas}"/></td>
            </tr>
        </c:forEach> --%>

        <%-- Datos de ejemplo --%>
        <tr>
            <td><a href="#">Guía Completa de Estructuras de Datos</a></td>
            <td><span class="badge bg-danger">PDF</span></td>
            <td>2024-05-05</td>
            <td>estructuras, algoritmos</td>
        </tr>
        <tr>
            <td><a href="#">Ejercicios de Algoritmos</a></td>
            <td><span class="badge bg-success">XLS</span></td>
            <td>2024-04-12</td>
            <td>java, python</td>
        </tr>
        </tbody>
    </table>
</div>

</body>
</html>
s