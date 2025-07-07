<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty sessionScope.usuarioLogueado}">
    <c:redirect url="login.jsp"/>
</c:if>

<c:set var="paginaActual" value="mensajes" scope="request" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat con <c:out value="${otroUsuario.nombre}"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body { background-color: #f0f2f5; }
        .chat-container {
            max-width: 800px;
            margin: 30px auto;
            background-color: #fff;
            border-radius: 0.75rem;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            display: flex;
            flex-direction: column;
            height: calc(100vh - 120px);
        }
        .chat-header {
            padding: 1rem;
            border-bottom: 1px solid #dee2e6;
            background-color: #f8f9fa;
        }
        .chat-body {
            flex-grow: 1;
            padding: 1rem;
            overflow-y: auto;
            display: flex;
            flex-direction: column;
        }
        .chat-footer {
            padding: 1rem;
            border-top: 1px solid #dee2e6;
            background-color: #f8f9fa;
        }
        .message {
            max-width: 70%;
            padding: 0.5rem 1rem;
            border-radius: 1rem;
            margin-bottom: 0.5rem;
        }
        .sent {
            background-color: #0d6efd;
            color: white;
            align-self: flex-end;
            border-bottom-right-radius: 0;
        }
        .received {
            background-color: #e9ecef;
            color: black;
            align-self: flex-start;
            border-bottom-left-radius: 0;
        }
    </style>
</head>
<body>

<jsp:include page="componentes/header.jsp" />

<div class="chat-container">
    <div class="chat-header">
        <h4 class="mb-0">Chat con <c:out value="${otroUsuario.nombre} ${otroUsuario.apellido}"/></h4>
    </div>

    <div class="chat-body" id="chat-body">
        <c:choose>
            <c:when test="${not empty conversacion}">
                <c:forEach var="msg" items="${conversacion}">
                    <c:choose>
                        <%-- Si el emisor del mensaje es el usuario logueado, es un mensaje enviado --%>
                        <c:when test="${msg.idEmisor eq sessionScope.usuarioLogueado.id}">
                            <div class="message sent">
                                <p class="mb-0"><c:out value="${msg.texto}"/></p>
                            </div>
                        </c:when>
                        <%-- Si no, es un mensaje recibido --%>
                        <c:otherwise>
                            <div class="message received">
                                <p class="mb-0"><c:out value="${msg.texto}"/></p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p class="text-center text-muted">Aún no hay mensajes. ¡Envía el primero!</p>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="chat-footer">
        <form action="${pageContext.request.contextPath}/MensajeServlet" method="POST" class="d-flex">
            <input type="hidden" name="idReceptor" value="${otroUsuario.id}">
            <input type="text" class="form-control me-2" name="textoMensaje" placeholder="Escribe un mensaje..." required autocomplete="off">
            <button type="submit" class="btn btn-primary"><i class="bi bi-send-fill"></i></button>
        </form>
    </div>
</div>

<script>
    // Script para hacer scroll hasta el último mensaje
    const chatBody = document.getElementById('chat-body');
    chatBody.scrollTop = chatBody.scrollHeight;
</script>

</body>
</html>
