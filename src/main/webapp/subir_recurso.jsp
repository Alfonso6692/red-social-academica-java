<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty sessionScope.usuarioLogueado}">
    <c:redirect url="login.jsp"/>
</c:if>

<c:set var="paginaActual" value="recursos" scope="request" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Subir Nuevo Recurso - Red Social Académica</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<jsp:include page="componentes/header.jsp" />

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <h2 class="mb-4">Compartir un Nuevo Recurso</h2>
            <div class="card">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/RecursoServlet" method="POST">
                        <input type="hidden" name="accion" value="subir">

                        <div class="mb-3">
                            <label for="titulo" class="form-label">Título del Recurso</label>
                            <input type="text" class="form-control" id="titulo" name="titulo" required>
                        </div>

                        <div class="mb-3">
                            <label for="descripcion" class="form-label">Descripción</label>
                            <textarea class="form-control" id="descripcion" name="descripcion" rows="3" required></textarea>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="url" class="form-label">URL del Recurso</label>
                                <input type="url" class="form-control" id="url" name="url" placeholder="https://ejemplo.com/recurso.pdf">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="tipo" class="form-label">Tipo</label>
                                <select class="form-select" id="tipo" name="tipo">
                                    <option selected>PDF</option>
                                    <option>Video</option>
                                    <option>Enlace</option>
                                    <option>Imagen</option>
                                    <option>Otro</option>
                                </select>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="etiquetas" class="form-label">Etiquetas (separadas por comas)</label>
                            <input type="text" class="form-control" id="etiquetas" name="etiquetas" placeholder="java, algoritmos, sql">
                        </div>

                        <button type="submit" class="btn btn-primary">Subir Recurso</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

