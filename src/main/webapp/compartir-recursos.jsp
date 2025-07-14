<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Compartir Recurso - Red Social Académica</title>
  <!-- Bootstrap 5 -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body {
      background-color: #f0f2f5;
    }
    .main-container {
      max-width: 800px;
      margin-top: 50px;
      background-color: #fff;
      padding: 2rem;
      border-radius: 0.5rem;
      box-shadow: 0 4px 8px rgba(0,0,0,0.1);
    }
  </style>
</head>
<body>
<!-- Verificar sesión -->
<c:if test="${empty sessionScope.usuario}">
  <c:redirect url="login.jsp"/>
</c:if>

<div class="container">
  <div class="row justify-content-center">
    <div class="col-md-10">
      <div class="main-container">
        <!-- Header -->
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h2>📚 Compartir Recurso Educativo</h2>
          <div>
            <a href="dashboard.jsp" class="btn btn-outline-secondary">← Volver al Dashboard</a>
          </div>
        </div>

        <!-- Información del usuario -->
        <div class="alert alert-info">
          <strong>Compartiendo como:</strong> ${sessionScope.usuario.nombreCompleto}
          <br><small>${sessionScope.usuario.infoAcademica}</small>
        </div>

        <!-- Mensajes de error/éxito -->
        <c:if test="${not empty param.error}">
          <div class="alert alert-danger" role="alert">
            <i class="fas fa-exclamation-triangle"></i> ${param.error}
          </div>
        </c:if>
        <c:if test="${not empty param.exito}">
          <div class="alert alert-success" role="alert">
            <i class="fas fa-check-circle"></i> ¡Recurso compartido exitosamente!
          </div>
        </c:if>

        <!-- Formulario -->
        <form action="RecursoServlet" method="post" class="needs-validation" novalidate>
          <input type="hidden" name="accion" value="compartir">

          <!-- Título -->
          <div class="mb-3">
            <label for="titulo" class="form-label">Título del Recurso *</label>
            <input type="text" class="form-control" id="titulo" name="titulo"
                   value="${param.titulo}" placeholder="Ej: Tutorial de Python para principiantes"
                   required maxlength="200">
            <div class="invalid-feedback">
              Por favor, ingresa un título para el recurso.
            </div>
          </div>

          <!-- Descripción -->
          <div class="mb-3">
            <label for="descripcion" class="form-label">Descripción</label>
            <textarea class="form-control" id="descripcion" name="descripcion"
                      rows="4" placeholder="Describe el contenido del recurso y por qué es útil..."
                      maxlength="1000">${param.descripcion}</textarea>
            <div class="form-text">Opcional. Ayuda a otros usuarios a entender el contenido.</div>
          </div>

          <!-- URL -->
          <div class="mb-3">
            <label for="url" class="form-label">URL del Recurso *</label>
            <input type="url" class="form-control" id="url" name="url"
                   value="${param.url}" placeholder="https://ejemplo.com/recurso"
                   required>
            <div class="invalid-feedback">
              Por favor, ingresa una URL válida del recurso.
            </div>
            <div class="form-text">Debe ser una URL completa (incluir http:// o https://)</div>
          </div>

          <!-- Tipo -->
          <div class="mb-3">
            <label for="tipo" class="form-label">Tipo de Recurso *</label>
            <select class="form-control" id="tipo" name="tipo" required>
              <option value="">Selecciona el tipo</option>
              <option value="PDF" ${param.tipo == 'PDF' ? 'selected' : ''}>📄 Documento PDF</option>
              <option value="Video" ${param.tipo == 'Video' ? 'selected' : ''}>🎥 Video</option>
              <option value="Enlace" ${param.tipo == 'Enlace' ? 'selected' : ''}>🔗 Página Web/Enlace</option>
              <option value="Presentacion" ${param.tipo == 'Presentacion' ? 'selected' : ''}>📊 Presentación</option>
              <option value="Codigo" ${param.tipo == 'Codigo' ? 'selected' : ''}>💻 Código/Repositorio</option>
              <option value="Libro" ${param.tipo == 'Libro' ? 'selected' : ''}>📚 Libro/E-book</option>
              <option value="Tutorial" ${param.tipo == 'Tutorial' ? 'selected' : ''}>🎯 Tutorial</option>
              <option value="Otro" ${param.tipo == 'Otro' ? 'selected' : ''}>📝 Otro</option>
            </select>
            <div class="invalid-feedback">
              Por favor, selecciona el tipo de recurso.
            </div>
          </div>

          <!-- Etiquetas -->
          <div class="mb-3">
            <label for="etiquetas" class="form-label">Etiquetas</label>
            <input type="text" class="form-control" id="etiquetas" name="etiquetas"
                   value="${param.etiquetas}"
                   placeholder="programación, java, tutorial, principiantes">
            <div class="form-text">
              Separa las etiquetas con comas. Ayudan a otros a encontrar tu recurso.
              <br><strong>Ejemplos:</strong> programación, matemáticas, física, química, inglés
            </div>
          </div>

          <!-- Botones -->
          <div class="d-grid gap-2 d-md-flex justify-content-md-end">
            <a href="RecursoServlet?accion=listar" class="btn btn-outline-secondary me-md-2">
              Cancelar
            </a>
            <button type="submit" class="btn btn-primary">
              📤 Compartir Recurso
            </button>
          </div>
        </form>

        <!-- Información adicional -->
        <div class="mt-4 p-3 bg-light rounded">
          <h6>💡 Consejos para compartir recursos:</h6>
          <ul class="mb-0 small">
            <li>Asegúrate de que la URL funcione correctamente</li>
            <li>Usa títulos descriptivos que expliquen claramente el contenido</li>
            <li>Agrega etiquetas relevantes para facilitar la búsqueda</li>
            <li>Verifica que el recurso sea apropiado para el ámbito académico</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- Validación del formulario -->
<script>
  // Validación de Bootstrap
  (function() {
    'use strict';
    window.addEventListener('load', function() {
      var forms = document.getElementsByClassName('needs-validation');
      var validation = Array.prototype.filter.call(forms, function(form) {
        form.addEventListener('submit', function(event) {
          if (form.checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
          }
          form.classList.add('was-validated');
        }, false);
      });
    }, false);
  })();

  // Contador de caracteres para descripción
  document.getElementById('descripcion').addEventListener('input', function() {
    const maxLength = 1000;
    const currentLength = this.value.length;
    const remaining = maxLength - currentLength;

    let helpText = this.parentNode.querySelector('.form-text');
    if (remaining < 100) {
      helpText.innerHTML = `Opcional. Ayuda a otros usuarios a entender el contenido. <span class="text-warning">(${remaining} caracteres restantes)</span>`;
    }
  });
</script>
</body>
</html>