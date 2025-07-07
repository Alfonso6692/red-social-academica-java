<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty sessionScope.usuarioLogueado}">
    <c:redirect url="login.jsp"/>
</c:if>

<a href="${pageContext.request.contextPath}/MensajeServlet?idOtroUsuario=${usuarioPerfil.id}" class="btn btn-secondary">
    <i class="bi bi-chat-dots-fill me-1"></i> Enviar Mensaje
</a>

<%-- Variable para el menú --%>
<c:set var="paginaActual" value="perfil" scope="request" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil de <c:out value="${usuarioPerfil.nombre}"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body { background-color: #f0f2f5; }
        .profile-card {
            background-color: #fff;
            border-radius: 0.75rem;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            overflow: hidden; /* Para que la imagen de cabecera no se salga de los bordes redondeados */
        }
        .profile-header {
            background-color: #343a40; /* Un fondo oscuro para la cabecera */
            height: 150px;
            position: relative;
        }
        .profile-picture {
            width: 130px;
            height: 130px;
            border-radius: 50%;
            position: absolute;
            bottom: -65px; /* Mitad de la altura para que sobresalga */
            left: 50%;
            transform: translateX(-50%);
            border: 4px solid white;
            background-color: #eee;
        }
        .profile-body {
            padding-top: 75px; /* Espacio para la foto de perfil que sobresale */
            padding-bottom: 2rem;
        }
    </style>
</head>
<body>

<jsp:include page="componentes/header.jsp" />

<div class="container my-5">
    <c:choose>
        <c:when test="${not empty usuarioPerfil}">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="profile-card">
                        <div class="profile-header">
                            <img src="https://via.placeholder.com/130" class="profile-picture" alt="Foto de Perfil">
                        </div>
                        <div class="profile-body text-center px-4">
                            <h2 class="mt-3"><c:out value="${usuarioPerfil.nombre} ${usuarioPerfil.apellido}"/></h2>
                            <p class="text-muted"><c:out value="${usuarioPerfil.carrera}"/> - Ciclo <c:out value="${usuarioPerfil.ciclo}"/></p>
                            <p><i class="bi bi-envelope-fill me-2"></i><c:out value="${usuarioPerfil.correo}"/></p>

                            <hr>

                            <h5 class="mt-4">Intereses</h5>
                            <div>
                                <c:forEach var="interes" items="${usuarioPerfil.intereses}">
                                    <span class="badge bg-info text-dark me-1"><c:out value="${interes}"/></span>
                                </c:forEach>
                                <c:if test="${empty usuarioPerfil.intereses}">
                                    <p class="text-muted">Aún no se han añadido intereses.</p>
                                </c:if>
                            </div>

                            <!-- Botones de acción -->
                            <div class="mt-4">
                                <c:if test="${esMiPerfil}">
                                    <a href="editar_perfil.jsp" class="btn btn-primary"><i class="bi bi-pencil-square me-1"></i> Editar Perfil</a>
                                </c:if>
                                <c:if test="${not esMiPerfil}">
                                    <form action="ConexionServlet" method="POST" class="d-inline">
                                        <input type="hidden" name="accion" value="solicitar">
                                        <input type="hidden" name="idDestinatario" value="${usuarioPerfil.id}">
                                        <button type="submit" class="btn btn-success"><i class="bi bi-person-plus-fill me-1"></i> Enviar Solicitud de Conexión</button>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-danger" role="alert">
                El perfil solicitado no existe.
            </div>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>
