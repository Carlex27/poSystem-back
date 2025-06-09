package com.softeams.poSystem.tickets.services;

import javax.print.*;
import java.io.File;
import java.io.FileInputStream;

public class PrinterService {

    public static void imprimirPDF(File file, String printerName) throws Exception {
        DocFlavor flavor = DocFlavor.INPUT_STREAM.PDF;
        PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, null);

        for (PrintService service : services) {
            if (service.getName().toLowerCase().contains(printerName.toLowerCase())) {
                DocPrintJob job = service.createPrintJob();
                FileInputStream fis = new FileInputStream(file);
                Doc doc = new SimpleDoc(fis, flavor, null);
                job.print(doc, null);
                fis.close();
                return;
            }
        }

        throw new RuntimeException("Impresora no encontrada: " + printerName);
    }
}
