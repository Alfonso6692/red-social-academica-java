<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Red Social Académica</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid #ddd;
            padding-bottom: 20px;
            margin-bottom: 20px;
        }
        .nav-menu {
            display: flex;
            gap: 20px;
        }
        .nav-menu a {
            text-decoration: none;
            color: #007bff;
            padding: 10px 15px;
            border-radius: 4px;
            border: 1px solid #007bff;
            transition: all 0.3s;
        }
        .nav-menu a:hover {
            background-color: #007bff;
            color: white;
        }
        .welcome-message {
            background-color: #e8f5e8;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .quick-actions {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }
        .action-card {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            text-align: center;
            border: 1px solid #dee2e6;
        }
        .action-card h3 {
            margin-top: 0;
            color: #333;
        }
        .btn {
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 4px;
            display: inline-block;
            margin-top: 10px;
        }
        .btn:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="container">
    <!-- Header -->
    <div class="header">
        <h1>Red Social Académica</h1>
        <div class="nav-menu">
            <a href="dashboard.jsp">Inicio</a>
            <a href="RecursoServlet?accion=listar">Recursos</a>
            <a href="GrupoServlet?accion=listar">Grupos</a>
            <a href="perfil.jsp">Perfil</a>
            <a href="LogoutServlet">Cerrar Sesión</a>
        </div>
    </div>

    <!-- Mensaje de bienvenida -->
    <div class="welcome-message">
        <h2>¡Bienvenido/a, ${sessionScope.usuario.nombreCompleto}!</h2>
        <p>Explora recursos educativos, conecta con otros estudiantes y forma parte de grupos de estudio.</p>
        <c:if test="${not empty sessionScope.usuario.infoAcademica}">
            <p><strong>📚 ${sessionScope.usuario.infoAcademica}</strong></p>
        </c:if>
    </div>

    <!-- Acciones rápidas -->
    <div class="quick-actions">
        <!-- Recursos -->
        <div class="action-card">
            <h3>📚 Recursos Educativos</h3>
            <p>Explora y comparte recursos de aprendizaje</p>
            <a href="RecursoServlet?accion=listar" class="btn">Ver Recursos</a>
            <a href="compartir-recurso.jsp" class="btn">Compartir Recurso</a>
        </div>

        <!-- Grupos -->
        <div class="action-card">
            <h3>👥 Grupos de Estudio</h3>
            <p>Únete a grupos y colabora con otros estudiantes</p>
            <a href="GrupoServlet?accion=listar" class="btn">Ver Grupos</a>
            <a href="crear-grupo.jsp" class="btn">Crear Grupo</a>
        </div>

        <!-- Mensajes -->
        <div class="action-card">
            <h3>💬 Mensajes</h3>
            <p>Comunícate con otros miembros</p>
            <a href="MensajeServlet?accion=listar" class="btn">Ver Mensajes</a>
            <a href="enviar-mensaje.jsp" class="btn">Enviar Mensaje</a>
        </div>

        <!-- Notificaciones -->
        <div class="action-card">
            <h3>🔔 Notificaciones</h3>
            <p>Mantente al día con las últimas actualizaciones</p>
            <a href="NotificacionServlet?accion=listar" class="btn">Ver Notificaciones</a>
        </div>
    </div>

    <!-- Estadísticas rápidas (opcional) -->
    <div style="margin-top: 30px; padding-top: 20px; border-top: 1px solid #ddd;">
        <h3>Resumen de Actividad</h3>
        <div style="display: flex; gap: 20px; justify-content: space-around; text-align: center;">
            <div>
                <strong>Recursos Compartidos</strong>
                <div style="font-size: 24px; color: #007bff;">-</div>
            </div>
            <div>
                <strong>Grupos Unidos</strong>
                <div style="font-size: 24px; color: #28a745;">-</div>
            </div>
            <div>
                <strong>Mensajes Enviados</strong>
                <div style="font-size: 24px; color: #ffc107;">-</div>
            </div>
        </div>
    </div>
</div>

<!-- Verificar si hay parámetros de éxito/error -->
<c:if test="${param.exito == 'true'}">
    <script>
        alert('Operación realizada con éxito');
    </script>
</c:if>

<c:if test="${param.error == 'true'}">
    <script>
        alert('Error al realizar la operación');
    </script>
</c:if>
</body>
</html>