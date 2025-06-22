<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard.jsp">RedSocial UTP</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/dashboard.jsp"><i class="bi bi-house-door-fill me-1"></i>Inicio</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/GrupoServlet?accion=listar"><i class="bi bi-people-fill me-1"></i>Grupos</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/RecursoServlet?accion=listar"><i class="bi bi-book-fill me-1"></i>Recursos</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/ConexionServlet?accion=listar"><i class="bi bi-link-45deg me-1"></i>Conexiones</a>
                </li>
            </ul>

            <ul class="navbar-nav">
                <!-- Icono de Notificaciones -->
                <li class="nav-item">
                    <a class="nav-link position-relative" href="${pageContext.request.contextPath}/NotificacionServlet?accion=listar">
                        <i class="bi bi-bell-fill fs-5"></i>
                        <span class="position-absolute top-25 start-75 translate-middle badge rounded-pill bg-danger">
                            3<span class="visually-hidden">notificaciones sin leer</span>
                        </span>
                    </a>
                </li>

                <!-- Menú Desplegable del Usuario -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-person-circle me-1"></i><c:out value="${sessionScope.usuarioLogueado.nombre}"/>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/PerfilServlet"><i class="bi bi-person-fill me-2"></i>Mi Perfil</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/CerrarSesionServlet"><i class="bi bi-box-arrow-right me-2"></i>Cerrar Sesión</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
