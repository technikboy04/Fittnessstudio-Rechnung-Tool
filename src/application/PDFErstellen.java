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
        form.setField("tpDatum", "21.09.1999");
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
//        PdfDocument pdfDoc = new PdfDocument();
//        Document document = new Document();
//        PdfWriter writer = PdfWriter.getInstance(document,
//                new FileOutputStream(filename));

//        document.open();
//
//        PdfContentByte cb = writer.getDirectContent();
//        BaseFont bf = BaseFont.createFont();
//        setImage(cb, "C:\\Users\\janmo\\Desktop\\Mockup der GUI.PNG", 80);
//        cb.beginText();
//        cb.setFontAndSize(bf, 12);
//        cb.moveText(300, 300);
//        cb.showText("Falsches Üben von Xylophonmusik quält jeden größeren Zwerg.");
//        cb.moveText(120, -16);
//        cb.setCharacterSpacing(2);
//        cb.setWordSpacing(12);
//        cb.newlineShowText("Erst recht auch jeden kleineren.");
//        document.add(new Paragraph("Rechnung"));
//        Paragraph p = new Paragraph("Das ist ein Text");
//        document.add(new Paragraph("Bla Bla Bla"));

//       cb.endText();

//        document.close();

    }

    public void setImage(PdfContentByte cb, String imgPath, float scalePercent)
            throws MalformedURLException, IOException, DocumentException {
        Image img = Image.getInstance(imgPath);
        img.scalePercent(scalePercent);
        img.setAbsolutePosition(cb.getXTLM(), cb.getYTLM());
        cb.addImage(img);
    }

    public void printMeasures(){
        System.out.println("A4-Ma\u00DFe: " + PageSize.A4.getWidth() + "pt x "
                + PageSize.A4.getHeight() + "pt - "
                + (PageSize.A4.getWidth() * 0.3527) + "mm x "
                + (PageSize.A4.getHeight() * 0.3527) + "mm");
    }


}