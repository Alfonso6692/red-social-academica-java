<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty sessionScope.usuarioLogueado}"><c:redirect url="login.jsp"/></c:if>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Chat con <c:out value="${otroUsuario.nombre}"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .chat-body { height: 400px; overflow-y: scroll; border: 1px solid #ccc; padding: 10px; }
        .sent { text-align: right; color: blue; }
        .received { text-align: left; color: green; }
    </style>
</head>
<body>
<jsp:include page="componentes/header.jsp" />
<div class="container mt-4">
    <h3>Chat con <c:out value="${otroUsuario.nombre} ${otroUsuario.apellido}"/></h3>
    <div class="chat-body">
        <c:forEach var="msg" items="${conversacion}">
            <p class="${msg.idEmisor eq sessionScope.usuarioLogueado.id ? 'sent' : 'received'}">
                <strong><c:out value="${msg.idEmisor eq sessionScope.usuarioLogueado.id ? 'Tú' : otroUsuario.nombre}"/>:</strong>
                <c:out value="${msg.texto}"/>
            </p>
        </c:forEach>
    </div>
    <form action="MensajeServlet" method="POST" class="mt-3">
        <input type="hidden" name="idReceptor" value="${otroUsuario.id}">
        <div class="input-group">
            <input type="text" class="form-control" name="textoMensaje" placeholder="Escribe un mensaje..." required>
            <button type="submit" class="btn btn-primary">Enviar</button>
        </div>
    </form>
</div>
</body>
</html>
