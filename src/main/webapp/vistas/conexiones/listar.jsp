<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Conexiones</title>

    <!-- Enlace a Bootstrap para un diseño limpio y responsivo -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

    <style>
        /* Estilos personalizados opcionales */
        .tarjeta-conexion {
            display: flex;
            align-items: center;
            margin-bottom: 1rem;
            padding: 1rem;
            border: 1px solid #ddd;
            border-radius: 0.5rem;
        }
        .tarjeta-conexion img {
            border-radius: 50%;
            margin-right: 1rem;
        }
        .tarjeta-conexion .info {
            flex-grow: 1;
        }
        .debug-info {
            background-color: #fff3cd;
            border: 1px solid #ffeeba;
            padding: 1rem;
            margin-bottom: 1rem;
            border-radius: 0.25rem;
        }
    </style>
</head>
<body>

<%-- Incluye tu barra de navegación si tienes una en un archivo separado --%>
<%-- <jsp:include page="/vistas/common/navbar.jsp" /> --%>

<div class="container mt-4">

    <!-- ======================= SECCIÓN DE DEPURACIÓN ======================= -->
    <!-- Este bloque nos ayuda a ver qué datos llegan a la página.      -->
    <!-- Puedes eliminarlo una vez que el problema esté resuelto.         -->
    <div class="debug-info">
        <h4>Información de Depuración</h4>
        <p>Usuario en sesión (ID): <strong>${sessionScope.usuario.id}</strong></p>
        <p>Usuario en sesión (Nombre): <strong>${sessionScope.usuario.nombre}</strong></p>
        <p>¿La lista de conexiones está vacía? <strong>${empty listaDeConexiones}</strong></p>
        <p>Tamaño de la lista de conexiones: <strong>${listaDeConexiones.size()}</strong></p>
        <p>Contenido de la lista: <strong>${listaDeConexiones}</strong></p>
    </div>
    <!-- ===================== FIN DE SECCIÓN DE DEPURACIÓN ===================== -->

    <h2>Mis Conexiones</h2>
    <hr>

    <%-- Condición 1: Si la lista de conexiones está vacía, muestra un mensaje --%>
    <c:if test="${empty listaDeConexiones}">
        <div class="alert alert-info">
            Aún no tienes conexiones. ¡Busca a otros estudiantes y empieza a conectar!
        </div>
    </c:if>

    <%-- Condición 2: Si la lista NO está vacía, la recorre y muestra cada amigo --%>
    <c:if test="${not empty listaDeConexiones}">
        <div class="lista-conexiones">
            <c:forEach var="amigo" items="${listaDeConexiones}">
                <div class="tarjeta-conexion">
                        <%-- Puedes añadir una imagen de perfil si tienes esa funcionalidad --%>
                    <img src="https://placehold.co/60x60/EFEFEF/AAAAAA?text=Perfil" alt="Foto de ${amigo.nombre}">

                    <div class="info">
                        <h5 class="mb-1">
                                <%-- Enlace al perfil del amigo --%>
                            <a href="${pageContext.request.contextPath}/PerfilServlet?id=${amigo.id}">
                                    ${amigo.nombre} ${amigo.apellido}
                            </a>
                        </h5>
                        <p class="mb-1 text-muted">${amigo.carrera}</p>
                    </div>

                    <div class="acciones">
                            <%-- Botones para interactuar con el amigo --%>
                        <a href="${pageContext.request.contextPath}/MensajeServlet?accion=iniciar&destinatario=${amigo.id}" class="btn btn-primary btn-sm">Enviar Mensaje</a>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>
</div>

</body>
</html>
