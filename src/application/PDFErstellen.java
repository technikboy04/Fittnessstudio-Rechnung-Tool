package application;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PDFErstellen {






    public PDFErstellen() throws URISyntaxException {
    }

    public void createPdf(String filename) throws DocumentException,
            IOException, URISyntaxException {
        PdfReader pdfTemplate = new PdfReader(filename);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(pdfTemplate, out);
        AcroFields form = stamper.getAcroFields();
        //stamper.getAcroFields().setField("testFeld   ", "21.09.1999");
        stamper.getAcroFields().setField("tpReNum", "test");
        form.setField("tpDatum", "2156565");
        form.setField("tpZahlung", "2 Wochen");
        form.setField("" +
                "", "192,63€");
        form.setField("tpStatus", "Austehend");
        form.setField("tbRePos", "Artikel 1");
        form.setField("tbRePos", form.getField("tbRePos") + "\n test 2");

        stamper.close();


        Path dateipfadPath = Paths.get(Main.class.getResource("testausgefüllt.pdf").toURI());
        String dateipfad = dateipfadPath.toString();


        FileOutputStream fos = new FileOutputStream(dateipfad);

        out.writeTo(fos);
        fos.close();
        pdfTemplate.close();


    }

    public void setImage(PdfContentByte cb, String imgPath, float scalePercent)
            throws MalformedURLException, IOException, DocumentException {
        Image img = Image.getInstance(imgPath);
        img.scalePercent(scalePercent);
        img.setAbsolutePosition(cb.getXTLM(), cb.getYTLM());
        cb.addImage(img);
    }



}