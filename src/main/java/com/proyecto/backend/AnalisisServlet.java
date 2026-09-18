package com.proyecto.backend;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

// Nuevas importaciones de la librería PDFBox
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

@WebServlet(name = "AnalisisServlet", urlPatterns = {"/AnalisisServlet"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)
public class AnalisisServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            Part filePart = request.getPart("expedientePdf");
            
            if (filePart != null) {
                String fileName = filePart.getSubmittedFileName();
                
                out.println("<h3 style='color: #27ae60;'>¡Lectura de PDF Exitosa!</h3>");
                out.println("<p>Archivo analizado: <strong>" + fileName + "</strong></p>");
                
                // 1. Extraer los bytes del archivo que envió JavaScript
                InputStream fileContent = filePart.getInputStream();
                byte[] pdfBytes = fileContent.readAllBytes();
                
                // 2. Cargar el documento en la memoria de Java usando PDFBox
                try (PDDocument document = Loader.loadPDF(pdfBytes)) {
                    
                    // 3. Extraer todo el texto del documento
                    PDFTextStripper stripper = new PDFTextStripper();
                    String textoExtraido = stripper.getText(document);
                    
                    // 4. Cortar el texto para no colapsar la pantalla (muestra los primeros 800 caracteres)
                    String previsualizacion = textoExtraido.length() > 800 
                            ? textoExtraido.substring(0, 800) + "\n\n... [TEXTO TRUNCADO PARA VISTA PREVIA] ..." 
                            : textoExtraido;
                            
                    out.println("<h4>Vista previa del texto extraído:</h4>");
                    out.println("<div style='background: #f4f7f6; padding: 15px; border-left: 4px solid #2980b9; font-family: monospace; white-space: pre-wrap;'>");
                    out.println(previsualizacion);
                    out.println("</div>");
                    
                } catch (Exception e) {
                    out.println("<p style='color: red;'>Error específico al leer el PDF: " + e.getMessage() + "</p>");
                }
                
            } else {
                out.println("<p style='color: red;'>Error: No se recibió ningún expediente.</p>");
            }
        } catch (Exception e) {
            response.getWriter().println("<p style='color: red;'>Error interno: " + e.getMessage() + "</p>");
        }
    }
}