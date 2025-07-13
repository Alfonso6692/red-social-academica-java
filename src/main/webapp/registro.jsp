<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro - Red Social Académica</title>
    <!-- Integración de Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f0f2f5;
        }
        .registro-container {
            max-width: 500px;
            margin-top: 50px;
            background-color: #fff;
            padding: 2rem;
            border-radius: 0.5rem;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .registro-container h2 {
            margin-bottom: 1.5rem;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="registro-container">
                <h2 class="text-center">Crear Cuenta</h2>
                <p class="text-center text-muted">Únete a la Red Social Académica</p>

                <!-- Formulario de Registro -->
                <form action="RegistroServlet" method="POST">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="nombre" class="form-label">Nombre Completo</label>
                                <input type="text" class="form-control" id="nombre" name="nombre"
                                       value="${param.nombre}" placeholder="Ej: Juan Pérez" required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="correo" class="form-label">Correo Electrónico</label>
                                <input type="email" class="form-control" id="correo" name="correo"
                                       value="${param.correo}" placeholder="ejemplo@universidad.edu" required>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="contrasena" class="form-label">Contraseña</label>
                                <input type="password" class="form-control" id="contrasena" name="contrasena"
                                       placeholder="Mínimo 6 caracteres" required minlength="6">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="confirmarContrasena" class="form-label">Confirmar Contraseña</label>
                                <input type="password" class="form-control" id="confirmarContrasena"
                                       name="confirmarContrasena" placeholder="Repetir contraseña" required>
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="universidad" class="form-label">Universidad/Institución</label>
                        <input type="text" class="form-control" id="universidad" name="universidad"
                               value="${param.universidad}" placeholder="Ej: Universidad Tecnológica del Perú">
                    </div>

                    <div class="mb-3">
                        <label for="carrera" class="form-label">Carrera/Área de Estudio</label>
                        <input type="text" class="form-control" id="carrera" name="carrera"
                               value="${param.carrera}" placeholder="Ej: Ingeniería de Sistemas">
                    </div>

                    <!-- Contenedor para mostrar mensajes de error o éxito -->
                    <c:if test="${not empty mensajeError}">
                        <div class="alert alert-danger" role="alert">
                            <i class="fas fa-exclamation-triangle"></i> ${mensajeError}
                        </div>
                    </c:if>
                    <c:if test="${not empty mensajeExito}">
                        <div class="alert alert-success" role="alert">
                            <i class="fas fa-check-circle"></i> ${mensajeExito}
                        </div>
                    </c:if>

                    <div class="d-grid">
                        <button type="submit" class="btn btn-success btn-lg">Crear Cuenta</button>
                    </div>
                </form>

                <hr>
                <div class="text-center">
                    <p>¿Ya tienes una cuenta?</p>
                    <a href="login.jsp" class="btn btn-outline-primary">Iniciar Sesión</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- Validación de contraseñas -->
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const form = document.querySelector('form');
        const contrasena = document.getElementById('contrasena');
        const confirmarContrasena = document.getElementById('confirmarContrasena');

        form.addEventListener('submit', function(e) {
            if (contrasena.value !== confirmarContrasena.value) {
                e.preventDefault();
                alert('Las contraseñas no coinciden');
                confirmarContrasena.focus();
                return false;
            }

            if (contrasena.value.length < 6) {
                e.preventDefault();
                alert('La contraseña debe tener al menos 6 caracteres');
                contrasena.focus();
                return false;
            }
        });

        // Validación en tiempo real
        confirmarContrasena.addEventListener('blur', function() {
            if (this.value && this.value !== contrasena.value) {
                this.classList.add('is-invalid');
            } else {
                this.classList.remove('is-invalid');
            }
        });
    });
</script>
</body>
</html>