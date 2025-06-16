package com.softeams.poSystem.tickets.generators;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.softeams.poSystem.core.dtos.abono.AbonoResumenDto;
import com.softeams.poSystem.core.dtos.cortes.CorteDto;
import com.softeams.poSystem.core.dtos.cortes.ResumeDepartamentosDto;
import com.softeams.poSystem.core.entities.Client;
import com.softeams.poSystem.tickets.entities.TicketItem;
import com.softeams.poSystem.tickets.entities.TicketRequest;
import com.softeams.poSystem.tickets.entities.TicketSettings;


import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TicketPDFGenerator {

    public static File generarTicketPDF(TicketRequest data) throws Exception {
        int items = data.getItems().size();
        int alturaBase = 200;
        int alturaPorItem = 20;
        int alturaTotal = alturaBase + (items * alturaPorItem);

        Rectangle tamañoTicket = new Rectangle(160, alturaTotal); // 58 mm de ancho
        Document doc = new Document(tamañoTicket, 10, 10, 10, 10);
        File file = File.createTempFile("ticket_", ".pdf");
        PdfWriter.getInstance(doc, new FileOutputStream(file));
        doc.open();

        Font normal = FontFactory.getFont(FontFactory.COURIER, 8);
        Font bold = FontFactory.getFont(FontFactory.COURIER_BOLD, 9);

        // Encabezado
        Paragraph header = new Paragraph(data.getNombreNegocio(), bold);
        header.setAlignment(Element.ALIGN_CENTER);
        doc.add(header);

        Paragraph direccion = new Paragraph(data.getDireccion(), normal);
        direccion.setAlignment(Element.ALIGN_CENTER);
        doc.add(direccion);

        Paragraph telefono = new Paragraph(data.getTelefono(), normal);
        telefono.setAlignment(Element.ALIGN_CENTER);
        doc.add(telefono);

        Paragraph rfc = new Paragraph(data.getRfc(), normal);
        rfc.setAlignment(Element.ALIGN_CENTER);
        doc.add(rfc);

        doc.add(new Paragraph("\n"));

        // Fecha y cajero
        doc.add(new Paragraph(data.getFecha() + " " + data.getHora(), normal));
        doc.add(new Paragraph("CAJERO: " + data.getCajero(), normal));
        doc.add(new Paragraph("FOLIO: " + data.getFolio(), normal));
        doc.add(new Paragraph("CANT. DESCRIPCION     IMPORTE", normal));
        doc.add(new Paragraph("=============================", normal));

        // Detalle de productos
        int totalArticulos = 0;
        for (TicketItem item : data.getItems()) {
            String line = String.format("%-2s %-18s %6.2f",
                    item.getCantidad(),
                    item.getDescription().length() > 18 ? item.getDescription().substring(0, 18) : item.getDescription(),
                    item.getImporte());
            doc.add(new Paragraph(line, normal));
            totalArticulos += Integer.parseInt(item.getCantidad());
        }

        // Totales y nota
        doc.add(new Paragraph("=============================", normal));
        doc.add(new Paragraph("NO. DE ARTÍCULOS: " + totalArticulos, normal));
        doc.add(new Paragraph("TOTAL: $" + data.getTotal(), bold));

        // Si es venta a crédito
        if (data.isCreditSale()) {
            Paragraph titulo = new Paragraph("* VENTA A CREDITO *", bold);
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);
            Paragraph firma1 = new Paragraph("FIRMA DEL CLIENTE:", normal);
            firma1.setAlignment(Element.ALIGN_CENTER);
            doc.add(firma1);


            Paragraph linea = new Paragraph("______________________", normal);
            linea.setAlignment(Element.ALIGN_CENTER);
            doc.add(linea);

            Paragraph firma = new Paragraph(data.getClientName().toUpperCase(), bold);
            firma.setAlignment(Element.ALIGN_CENTER);
            doc.add(firma);
        }

        doc.add(new Paragraph("\n"));

        Paragraph despedida = new Paragraph("GRACIAS POR SU COMPRA", normal);
        despedida.setAlignment(Element.ALIGN_CENTER);
        doc.add(despedida);

        Paragraph url = new Paragraph(data.getUrl(), normal);
        url.setAlignment(Element.ALIGN_CENTER);
        doc.add(url);

        doc.close();
        return file;
    }

    public static File generarTicketCortePDF(TicketSettings settings, CorteDto corteDto, LocalDateTime fecha, Long countSalesa) throws Exception {
        int items = corteDto.ventasPorDepartamento().size();
        int alturaBase = 300;
        int alturaPorItem = 20;
        int alturaTotal = alturaBase + (items * alturaPorItem);

        Rectangle tamañoTicket = new Rectangle(160, alturaTotal); // 58 mm de ancho
        Document doc = new Document(tamañoTicket, 10, 10, 10, 10);
        File file = File.createTempFile("ticket_corte_", ".pdf");
        PdfWriter.getInstance(doc, new FileOutputStream(file));
        doc.open();

        Font normal = FontFactory.getFont(FontFactory.COURIER, 8);
        Font bold = FontFactory.getFont(FontFactory.COURIER_BOLD, 9);

        // Encabezado
        Paragraph header = new Paragraph(settings.getNombreNegocio(), bold);
        header.setAlignment(Element.ALIGN_CENTER);
        doc.add(header);

        Paragraph direccion = new Paragraph(settings.getDireccion(), normal);
        direccion.setAlignment(Element.ALIGN_CENTER);
        doc.add(direccion);

        Paragraph telefono = new Paragraph(settings.getTelefono(), normal);
        telefono.setAlignment(Element.ALIGN_CENTER);
        doc.add(telefono);

        Paragraph rfc = new Paragraph(settings.getRfc(), normal);
        rfc.setAlignment(Element.ALIGN_CENTER);
        doc.add(rfc);

        doc.add(new Paragraph("CORTE DEL DIA", bold));
        doc.add(new Paragraph("DEL " + fecha.toLocalDate().toString(), normal));
        doc.add(new Paragraph("\n"));

        doc.add(new Paragraph("CAJERO: - TODOS -", normal));
        doc.add(new Paragraph("CAJA: CAJA PRINCIPAL", normal));
        doc.add(new Paragraph("=============================", normal));

        doc.add(new Paragraph("== VENTAS DEL DIA ==", bold));
        doc.add(new Paragraph(countSalesa+" VENTAS EN EL DIA.", normal));
        doc.add(new Paragraph("=============================", normal));


        doc.add(new Paragraph("== VENTAS DE CONTADO ==", bold));
        doc.add(new Paragraph("CON EFECTIVO: $" + corteDto.ventasEfectivo(), normal));
        doc.add(new Paragraph("TOTAL: $" + corteDto.ventasEfectivo(), normal));
        doc.add(new Paragraph("=============================", normal));

        doc.add(new Paragraph("== SALIDAS/PROVEEDORES ==", bold));
        doc.add(new Paragraph("TOTAL: $" + corteDto.pagoProveedores(), normal));
        doc.add(new Paragraph("=============================", normal));

        doc.add(new Paragraph("== DINERO EN CAJA ==", bold));
        doc.add(new Paragraph("PAGOS CON EFECTIVO: +$" + corteDto.ventasEfectivo(), normal));
        doc.add(new Paragraph("PAGOS A PROVEEDORES: -$" + corteDto.pagoProveedores(), normal));
        doc.add(new Paragraph("TOTAL: $" + corteDto.dineroEnCaja(), normal));
        doc.add(new Paragraph("=============================", normal));

        doc.add(new Paragraph("== VENTAS TOTALES ==", bold));
        doc.add(new Paragraph("VENTAS DE CONTADO: $" + corteDto.ventasEfectivo(), normal));
        doc.add(new Paragraph("PAGOS DE CLIENTES: $" + corteDto.pagoClientes(), normal));
        doc.add(new Paragraph("TOTAL: $" + corteDto.ventasTotales(), normal));
        doc.add(new Paragraph("=============================", normal));

        doc.add(new Paragraph("== GANANCIA DEL DIA ==", bold));
        doc.add(new Paragraph("GANANCIA: $" + corteDto.gananciaDelDia(), normal));
        doc.add(new Paragraph("=============================", normal));

        doc.add(new Paragraph("== PAGOS DE CREDITOS ==", bold));
        for(AbonoResumenDto abono: corteDto.abonos()){
            String nombre = abono.getClientName().toUpperCase();
            BigDecimal montoAbono = abono.getMontoAbono();
            String linea = String.format("%-18s %s", nombre, montoAbono);
            doc.add(new Paragraph(linea, normal));
        }

        doc.add(new Paragraph("=============================", normal));

        doc.add(new Paragraph("==== VENTAS POR DEPT ====", bold));
        if(corteDto.abonos() == null){
            doc.add(new Paragraph("NO HUBO PAGOS", normal));
        }
        for (ResumeDepartamentosDto dep : corteDto.ventasPorDepartamento()) {
            String nombre = dep.getNombreDepartamento().toUpperCase();
            String total = String.format("$%.2f", dep.getTotalVentas());
            String linea = String.format("%-18s %s", nombre, total);
            doc.add(new Paragraph(linea, normal));
        }

        doc.add(new Paragraph("\n"));

        Paragraph despedida = new Paragraph("GRACIAS POR SU COMPRA", normal);
        despedida.setAlignment(Element.ALIGN_CENTER);
        doc.add(despedida);

        Paragraph url = new Paragraph(settings.getUrl(), normal);
        url.setAlignment(Element.ALIGN_CENTER);
        doc.add(url);

        doc.close();
        return file;
    }

    public static File generarTicketSaldosPDF(TicketSettings settings, List<Client> clientList) throws Exception {
        int items = clientList.size();
        int alturaBase = 300;
        int alturaPorItem = 25;
        int alturaTotal = alturaBase + (items * alturaPorItem);

        Rectangle tamañoTicket = new Rectangle(160, alturaTotal); // 58 mm de ancho
        Document doc = new Document(tamañoTicket, 10, 10, 10, 10);
        File file = File.createTempFile("ticket_saldos_", ".pdf");
        PdfWriter.getInstance(doc, new FileOutputStream(file));
        doc.open();

        Font normal = FontFactory.getFont(FontFactory.COURIER, 8);
        Font bold = FontFactory.getFont(FontFactory.COURIER_BOLD, 9);

        // Encabezado
        Paragraph header = new Paragraph(settings.getNombreNegocio(), bold);
        header.setAlignment(Element.ALIGN_CENTER);
        doc.add(header);

        Paragraph direccion = new Paragraph(settings.getDireccion(), normal);
        direccion.setAlignment(Element.ALIGN_CENTER);
        doc.add(direccion);

        Paragraph telefono = new Paragraph(settings.getTelefono(), normal);
        telefono.setAlignment(Element.ALIGN_CENTER);
        doc.add(telefono);

        Paragraph rfc = new Paragraph(settings.getRfc(), normal);
        rfc.setAlignment(Element.ALIGN_CENTER);
        doc.add(rfc);

        doc.add(new Paragraph("\nCLIENTES CON SALDO PENDIENTE\n", bold));
        doc.add(new Paragraph("=============================", normal));

        // Clientes con saldo
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Client client : clientList) {
            String nombre = client.getName().toUpperCase();
            String balance = String.format("$%.2f", client.getBalance());
            String ultimoPago = client.getLastAbonoDate() != null
                    ? client.getLastAbonoDate().format(formatter)
                    : "SIN PAGO";

            Paragraph cliente = new Paragraph(nombre, bold);
            Paragraph saldo = new Paragraph("Saldo: " + balance, normal);
            Paragraph fecha = new Paragraph("Último pago: " + ultimoPago, normal);

            doc.add(cliente);
            doc.add(saldo);
            doc.add(fecha);
            doc.add(new Paragraph("-----------------------------", normal));
        }

        // Footer
        doc.add(new Paragraph("\n"));

        Paragraph despedida = new Paragraph("GRACIAS POR SU COMPRA", normal);
        despedida.setAlignment(Element.ALIGN_CENTER);
        doc.add(despedida);

        Paragraph url = new Paragraph(settings.getUrl(), normal);
        url.setAlignment(Element.ALIGN_CENTER);
        doc.add(url);

        doc.close();
        return file;
    }



}
