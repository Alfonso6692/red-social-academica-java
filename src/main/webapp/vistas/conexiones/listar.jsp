<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recursos Educativos - Red Social Académica</title>
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
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 1px solid #ddd;
        }
        .search-box {
            margin-bottom: 20px;
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 6px;
        }
        .search-box input, .search-box button {
            padding: 8px 12px;
            margin-right: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .search-box button {
            background-color: #007bff;
            color: white;
            cursor: pointer;
        }
        .alert {
            padding: 12px;
            margin-bottom: 20px;
            border-radius: 4px;
        }
        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .recurso-card {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 15px;
            background-color: white;
            transition: box-shadow 0.3s;
        }
        .recurso-card:hover {
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .recurso-titulo {
            font-size: 18px;
            font-weight: bold;
            color: #333;
            margin-bottom: 10px;
        }
        .recurso-tipo {
            display: inline-block;
            background-color: #007bff;
            color: white;
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 12px;
            margin-bottom: 10px;
        }
        .recurso-descripcion {
            color: #666;
            margin-bottom: 10px;
        }
        .recurso-etiquetas {
            margin-bottom: 10px;
        }
        .etiqueta {
            display: inline-block;
            background-color: #e9ecef;
            color: #495057;
            padding: 2px 8px;
            border-radius: 12px;
            font-size: 12px;
            margin-right: 5px;
        }
        .recurso-url {
            margin-top: 10px;
        }
        .recurso-url a {
            color: #007bff;
            text-decoration: none;
        }
        .recurso-url a:hover {
            text-decoration: underline;
        }
        .btn {
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 4px;
            display: inline-block;
            border: none;
            cursor: pointer;
        }
        .btn:hover {
            background-color: #0056b3;
        }
        .empty-state {
            text-align: center;
            padding: 40px;
            color: #666;
        }
        .nav-links {
            margin-bottom: 20px;
        }
        .nav-links a {
            color: #007bff;
            text-decoration: none;
            margin-right: 15px;
        }
    </style>
</head>
<body>
<div class="container">
    <!-- Navegación -->
    <div class="nav-links">
        <a href="dashboard.jsp">← Volver al Dashboard</a>
        <a href="compartir-recurso.jsp">+ Compartir Recurso</a>
    </div>

    <!-- Header -->
    <div class="header">
        <h1>📚 Recursos Educativos</h1>
        <div>
            <span>Bienvenido/a, ${sessionScope.usuario.nombreCompleto}</span>
        </div>
    </div>

    <!-- Búsqueda -->
    <div class="search-box">
        <form method="get" action="RecursoServlet" style="margin: 0;">
            <input type="hidden" name="accion" value="buscar">
            <input type="text" name="etiqueta" placeholder="Buscar por etiqueta..." value="${etiquetaBuscada}">
            <button type="submit">🔍 Buscar</button>
            <a href="RecursoServlet?accion=listar" class="btn" style="margin-left: 10px;">Ver Todos</a>
        </form>
    </div>

    <!-- Mensajes de éxito/error -->
    <c:if test="${param.exito == 'true'}">
        <div class="alert alert-success">
            ✅ Recurso compartido exitosamente
        </div>
    </c:if>

    <c:if test="${not empty param.error}">
        <div class="alert alert-error">
            ❌ Error: ${param.error}
        </div>
    </c:if>

    <!-- Lista de recursos -->
    <c:choose>
        <c:when test="${not empty listaDeRecursos}">
            <div style="margin-bottom: 15px;">
                <strong>Total de recursos encontrados: ${listaDeRecursos.size()}</strong>
                <c:if test="${not empty etiquetaBuscada}">
                    <span style="color: #666;"> - Filtrado por: "${etiquetaBuscada}"</span>
                </c:if>
            </div>

            <c:forEach var="recurso" items="${listaDeRecursos}">
                <div class="recurso-card">
                    <div class="recurso-titulo">${recurso.titulo}</div>

                    <span class="recurso-tipo">${recurso.tipo}</span>

                    <c:if test="${not empty recurso.descripcion}">
                        <div class="recurso-descripcion">${recurso.descripcion}</div>
                    </c:if>

                    <c:if test="${not empty recurso.etiquetas}">
                        <div class="recurso-etiquetas">
                            <strong>Etiquetas:</strong>
                            <c:forEach var="etiqueta" items="${recurso.etiquetas}">
                                <span class="etiqueta">${etiqueta}</span>
                            </c:forEach>
                        </div>
                    </c:if>

                    <div class="recurso-url">
                        <strong>Enlace:</strong>
                        <a href="${recurso.url}" target="_blank">${recurso.url}</a>
                    </div>

                    <c:if test="${not empty recurso.fechaCreacion}">
                        <div style="margin-top: 10px; font-size: 12px; color: #666;">
                            Compartido el: ${recurso.fechaCreacion}
                        </div>
                    </c:if>
                </div>
            </c:forEach>
        </c:when>

        <c:otherwise>
            <div class="empty-state">
                <h3>📭 No hay recursos disponibles</h3>
                <p>Sé el primero en compartir un recurso educativo</p>
                <a href="compartir-recurso.jsp" class="btn">+ Compartir Primer Recurso</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>