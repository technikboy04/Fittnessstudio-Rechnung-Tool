package application;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import javafx.collections.ObservableList;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class PDFErstellen {


    public PDFErstellen() throws URISyntaxException {
    }

    /**
     * Erstellt eine PDF mit den übergebenen Rechnungsdaten in die PDF-Vorlage
     *
     * @param filename            PDF-Vorlage, welche befüllt werden soll
     * @param rechnungsDaten      Benötigeten Rechnungsdate, welche in die PDF geschriben werden sollen
     * @param rechnungspositionen Rechnungspositionen
     * @param rechnungsPfad       Dateipfad, an dem die PDF gespeichert werden soll
     * @throws DocumentException
     * @throws IOException
     * @throws URISyntaxException
     */
    public void createPdf(String filename, List rechnungsDaten, ObservableList rechnungspositionen, String rechnungsPfad) throws DocumentException,
            IOException, URISyntaxException {
        PdfReader pdfTemplate = new PdfReader(filename);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(pdfTemplate, out);
        AcroFields form = stamper.getAcroFields();

        // Befüllt die Felder der PDF
        form.setField("tpKdNum", (String) rechnungsDaten.get(0));
        form.setField("tpDatum", (String) rechnungsDaten.get(1));
        form.setField("tpReNum", (String) rechnungsDaten.get(2));
        form.setField("tpZahlung", (String) rechnungsDaten.get(3));
        form.setField("tpReSum", (String) rechnungsDaten.get(4));
        form.setField("tpStatus", (String) rechnungsDaten.get(5));

        //Rechnungspositionen setzen
        StringBuilder sb = new StringBuilder();
        sb.append("Produktname" + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "Anzahl" + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + "Preis" + "\n");
        for (Object o : rechnungspositionen) {
            Rechnungsposition position = (Rechnungsposition) o;
            sb.append(position.getProduktname() + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + position.getAnzahl() + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + position.getPreis() + "\n");

        }
        form.setField("tbRePos", sb.toString());

        stamper.close();

        FileOutputStream fos = new FileOutputStream(rechnungsPfad + ".pdf");

        out.writeTo(fos);
        fos.close();
        pdfTemplate.close();

    }

}