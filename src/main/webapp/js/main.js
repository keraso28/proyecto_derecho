document.addEventListener('DOMContentLoaded', function() {
    // Referencias a los elementos visuales de tu HTML
    const formulario = document.getElementById('formulario-analisis');
    const panelResultados = document.getElementById('panel-resultados');
    const contenidoResultado = document.getElementById('contenido-resultado');

    // Escucha el evento 'submit' cuando se hace clic en el botón
    formulario.addEventListener('submit', function(event) {
        // 1. Evita que la página se recargue y borre todo
        event.preventDefault();

        // 2. Captura el archivo PDF seleccionado
        const inputArchivo = document.getElementById('archivoPdf');
        const archivo = inputArchivo.files[0];

        if (!archivo) {
            alert('Por favor, selecciona un expediente en formato PDF.');
            return;
        }

        // Mensajes de diagnóstico para la consola del navegador
        console.log("Paso 1: Archivo capturado exitosamente ->", archivo.name);
        console.log("Paso 2: Tamaño del documento ->", (archivo.size / 1024).toFixed(2), "KB");

        // 3. Empaqueta el archivo usando FormData (el formato estándar para enviar archivos)
        const formData = new FormData();
        formData.append('expedientePdf', archivo);

        // 4. Muestra un estado de "Cargando" en la interfaz
        panelResultados.style.display = 'block';
        contenidoResultado.innerHTML = '<p style="color: #2980b9;"><strong>Procesando expediente...</strong> Extrayendo texto y consultando reglas penales.</p>';

        /*
         * 5. EL PUENTE (AJAX/Fetch)
         * Este bloque está comentado temporalmente porque aún no hemos creado el 
         * 'AnalisisServlet' en Java. Una vez lo creemos, descomentaremos esto.
         */
        
        
        fetch('AnalisisServlet', {
            method: 'POST',
            body: formData
        })
        .then(response => response.text())
        .then(data => {
            // Imprime la respuesta de Java en la pantalla del usuario
            contenidoResultado.innerHTML = data;
        })
        .catch(error => {
            console.error('Error de conexión con Java:', error);
            contenidoResultado.innerHTML = '<p style="color:red;">Error al comunicar con el motor de análisis.</p>';
        });
        
    });
});