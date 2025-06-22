<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - Red Social Académica</title>
    <!-- Integración de Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f0f2f5;
        }
        .login-container {
            max-width: 400px;
            margin-top: 100px;
            background-color: #fff;
            padding: 2rem;
            border-radius: 0.5rem;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .login-container h2 {
            margin-bottom: 1.5rem;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="login-container">
                <h2 class="text-center">Red Social Académica</h2>

                <!-- Formulario de Inicio de Sesión -->
                <form action="LoginServlet" method="POST">
                    <div class="mb-3">
                        <label for="correo" class="form-label">Correo Electrónico</label>
                        <input type="email" class="form-control" id="correo" name="correo" required>
                    </div>
                    <div class="mb-3">
                        <label for="contrasena" class="form-label">Contraseña</label>
                        <input type="password" class="form-control" id="contrasena" name="contrasena" required>
                    </div>

                    <!-- Contenedor para mostrar mensajes de error o éxito -->
                    <c:if test="${not empty mensajeError}">
                        <div class="alert alert-danger" role="alert">
                                ${mensajeError}
                        </div>
                    </c:if>
                    <c:if test="${not empty mensajeExito}">
                        <div class="alert alert-success" role="alert">
                                ${mensajeExito}
                        </div>
                    </c:if>

                    <div class="d-grid">
                        <button type="submit" class="btn btn-primary">Ingresar</button>
                    </div>
                </form>
                <hr>
                <div class="text-center">
                    <p>¿No tienes una cuenta?</p>
                    <a href="registro.jsp" class="btn btn-success">Registrarse</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
