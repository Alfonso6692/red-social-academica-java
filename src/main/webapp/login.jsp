<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - Red Social Académica</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            margin: 0;
            padding: 0;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .login-container {
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
        }
        .login-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .login-header h1 {
            color: #333;
            margin-bottom: 5px;
        }
        .login-header p {
            color: #666;
            margin: 0;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
        }
        .form-group input {
            width: 100%;
            padding: 12px;
            border: 2px solid #e1e5e9;
            border-radius: 6px;
            font-size: 16px;
            transition: border-color 0.3s;
            box-sizing: border-box;
        }
        .form-group input:focus {
            outline: none;
            border-color: #667eea;
        }
        .btn-login {
            width: 100%;
            padding: 12px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            font-weight: 500;
            cursor: pointer;
            transition: transform 0.2s;
        }
        .btn-login:hover {
            transform: translateY(-2px);
        }
        .error-message {
            background-color: #f8d7da;
            color: #721c24;
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 20px;
            border: 1px solid #f5c6cb;
        }
        .register-link {
            text-align: center;
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #e1e5e9;
        }
        .register-link a {
            color: #667eea;
            text-decoration: none;
        }
        .register-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="login-container">
    <div class="login-header">
        <h1>🎓 Red Social Académica</h1>
        <p>Conecta, aprende y comparte conocimiento</p>
    </div>

    <!-- Mostrar mensajes de error -->
    <c:if test="${not empty mensajeError}">
        <div class="error-message">
                ${mensajeError}
        </div>
    </c:if>

    <!-- Formulario de login -->
    <form action="LoginServlet" method="post">
        <div class="form-group">
            <label for="correo">Correo Electrónico:</label>
            <input type="email"
                   id="correo"
                   name="correo"
                   value="${param.correo}"
                   placeholder="ejemplo@universidad.edu"
                   required>
        </div>

        <div class="form-group">
            <label for="contrasena">Contraseña:</label>
            <input type="password"
                   id="contrasena"
                   name="contrasena"
                   placeholder="Ingresa tu contraseña"
                   required>
        </div>

        <button type="submit" class="btn-login">
            Iniciar Sesión
        </button>
    </form>

    <div class="register-link">
        <p>¿No tienes cuenta? <a href="registro.jsp">Regístrate aquí</a></p>
    </div>
</div>

<!-- JavaScript para mejorar la experiencia -->
<script>
    // Enfocar el primer campo al cargar la página
    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('correo').focus();
    });

    // Validación básica del lado del cliente
    document.querySelector('form').addEventListener('submit', function(e) {
        const correo = document.getElementById('correo').value.trim();
        const contrasena = document.getElementById('contrasena').value.trim();

        if (!correo || !contrasena) {
            alert('Por favor, completa todos los campos');
            e.preventDefault();
            return false;
        }

        if (!isValidEmail(correo)) {
            alert('Por favor, ingresa un correo válido');
            e.preventDefault();
            return false;
        }
    });

    function isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }
</script>
</body>
</html>