package com.softeams.poSystem.tickets.generators;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.softeams.poSystem.tickets.entities.TicketItem;
import com.softeams.poSystem.tickets.entities.TicketRequest;

import java.io.File;
import java.io.FileOutputStream;

public class TicketPDFGenerator {
    public static File generarTicketPDF(TicketRequest data) throws Exception {
        Rectangle tamañoTicket = new Rectangle(226, 600); // ancho 80mm aprox
        Document doc = new Document(tamañoTicket, 10, 10, 10, 10);
        File file = File.createTempFile("ticket_", ".pdf");
        PdfWriter.getInstance(doc, new FileOutputStream(file));
        doc.open();

        Font normal = FontFactory.getFont(FontFactory.COURIER, 8);
        Font bold = FontFactory.getFont(FontFactory.COURIER_BOLD, 9);

        Paragraph header = new Paragraph(data.getNombreNegocio(), bold);
        header.setAlignment(Element.ALIGN_CENTER);
        doc.add(header);
        doc.add(new Paragraph(data.getDireccion(), normal));
        doc.add(new Paragraph(data.getTelefono(), normal));
        doc.add(new Paragraph(data.getRfc(), normal));
        doc.add(new Paragraph("\n"));

        doc.add(new Paragraph(data.getFecha() + "        " + data.getHora(), normal));
        doc.add(new Paragraph("========================================", normal));

        for (TicketItem item : data.getItems()) {
            String line = String.format("%-4s %-20s %6.2f", item.getCantidad(), item.getDescription(), item.getImporte());
            doc.add(new Paragraph(line, normal));
        }

        doc.add(new Paragraph("========================================", normal));
        doc.add(new Paragraph("No. de Artículos: " + data.getItems().size(), normal));
        doc.add(new Paragraph("Total: $" + data.getTotal(), bold));
        doc.add(new Paragraph("\n" + data.getMensajeFinal(), normal));
        doc.add(new Paragraph(data.getUrl(), normal));

        doc.close();
        return file;
    }
}
