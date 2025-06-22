<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro - Red Social Académica</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f0f2f5; }
        .register-container {
            max-width: 500px;
            margin-top: 50px;
            background-color: #fff;
            padding: 2rem;
            border-radius: 0.5rem;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="register-container">
                <h2 class="text-center mb-4">Crear Nueva Cuenta</h2>

                <form action="RegistroServlet" method="POST">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="nombre" class="form-label">Nombre</label>
                            <input type="text" class="form-control" id="nombre" name="nombre" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="apellido" class="form-label">Apellido</label>
                            <input type="text" class="form-control" id="apellido" name="apellido" required>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="correo" class="form-label">Correo Electrónico</label>
                        <input type="email" class="form-control" id="correo" name="correo" required>
                    </div>
                    <div class="mb-3">
                        <label for="contrasena" class="form-label">Contraseña</label>
                        <input type="password" class="form-control" id="contrasena" name="contrasena" required minlength="6">
                    </div>
                    <div class="row">
                        <div class="col-md-8 mb-3">
                            <label for="carrera" class="form-label">Carrera</label>
                            <input type="text" class="form-control" id="carrera" name="carrera" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="ciclo" class="form-label">Ciclo</label>
                            <input type="number" class="form-control" id="ciclo" name="ciclo" required min="1" max="12">
                        </div>
                    </div>

                    <c:if test="${not empty mensajeError}">
                        <div class="alert alert-danger" role="alert">
                                ${mensajeError}
                        </div>
                    </c:if>

                    <div class="d-grid mt-2">
                        <button type="submit" class="btn btn-success">Registrarme</button>
                    </div>
                </form>
                <hr>
                <div class="text-center">
                    <p>¿Ya tienes una cuenta?</p>
                    <a href="login.jsp">Iniciar Sesión</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
