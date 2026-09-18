package com.proyecto.backend;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

// La ruta a la que JavaScript enviará el archivo
@WebServlet(name = "AnalisisServlet", urlPatterns = {"/AnalisisServlet"})
// Etiqueta OBLIGATORIA para que Java acepte recibir archivos (PDFs)
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB máximo por archivo
    maxRequestSize = 1024 * 1024 * 50     // 50MB máximo en total
)
public class AnalisisServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Configuramos la respuesta para devolver texto o HTML al frontend
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            // 1. Atrapamos el archivo PDF que envió JavaScript
            Part filePart = request.getPart("expedientePdf");
            
            if (filePart != null) {
                String fileName = filePart.getSubmittedFileName();
                long fileSize = filePart.getSize();
                
                // 2. Enviamos una respuesta HTML de vuelta al navegador
                out.println("<h3 style='color: #27ae60;'>¡Conexión Exitosa con el Backend de Java!</h3>");
                out.println("<p>El servidor recibió correctamente el archivo: <strong>" + fileName + "</strong></p>");
                out.println("<p>Peso confirmado en el servidor: " + (fileSize / 1024) + " KB</p>");
                out.println("<p style='color: #7f8c8d;'><em>El puente está abierto. El siguiente paso será usar PDFBox para extraer su texto.</em></p>");
            } else {
                out.println("<p style='color: red;'>Error: No se recibió ningún expediente.</p>");
            }
        } catch (Exception e) {
            response.getWriter().println("<p style='color: red;'>Error interno en el servidor: " + e.getMessage() + "</p>");
        }
    }
}