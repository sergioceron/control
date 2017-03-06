package mx.ipn.cidetec.virtual.entities;

/**
 * Created by root on 5/05/14.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FooterPdf extends  PdfPageEventHelper {
    public final String RESOURCE_CIDETEC
            = "cidetec.png";
    public final String RESOURCE_SEP
            = "SEP.jpg";
    public final String RESOURCE_ESCUDONACIONAL
            = "EscudoMexicano.jpg";
    public final String RESOURCE_IPN
            = "ipn.png";

    private final String pieUnidad = "\"Unidad Profesional Adolfo López Mateos\", Av. Juan de Dios Batiz s/n Esq. Miguel Othon de Mendizabal,";
    private final String pieColonia = "Col.Nueva Industrial Vallejo, Deleg. Gustavo A. Madero, México,D.F.. C.P. 07700";
    private final String pieTelefono = "Tel.: 57296000 ext. 52516, Fax en la ext. 52541, email: cidetec@ipn.mx";
    private final String pieWeb = "www.cidetec.ipn.mx";
    private final String pieNota = "Nota: Escala de evaluación de 0 al 5, es la máxima puntuación";
    private final String pieExpediente = "c.i.p expediente";
    private final String pieOCN = "OCN/JCGR/sig*";
    private int pageTotal = 0;
    private static final String INSTITUTO = "Instituto Politécnico Nacional";
    private static final String CIDETEC = "Centro de Innovación y Desarrollo Tecnológico en Cómputo";
    PdfTemplate total;
    private PdfWriter writer;

    public int getPageTotal() {
        return pageTotal+1;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }


    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        Path file = Paths.get(".");

        pageTotal++;
        this.writer = writer;
        /*
        ColumnText.showTextAligned(writer.getDirectContent(), Element.PARAGRAPH, new Phrase(INSTITUTO,FontFactory.getFont("arial",4,Font.BOLD,BaseColor.BLACK)), 192,390,0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_UNDEFINED, new Phrase(CIDETEC,FontFactory.getFont("arial",4,Font.BOLD,BaseColor.BLACK)), 112,365,0);
        try {
            setWatherMark(RESOURCE_ESCUDONACIONAL, 200, 200, 40, 40, 0.2f);
            setImage(RESOURCE_CIDETEC, 25,25, 230, 360);
            setImage(RESOURCE_SEP, 100, 70,10, 350);
            setImage(RESOURCE_IPN, 22,29, 250, 380);
        } catch (IOException  ex) {
            Logger.getLogger(FooterPdf.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        */
        //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("-"+writer.getPageNumber()+"-HOJA-CID 824/2015",FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)), 140,350,0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(pieNota,FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)), 27,60,0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(pieExpediente,FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)),27,45,0);
        //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(pieOCN,FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)), 27,35,0);
        //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(pieUnidad,FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)), 150,25,0);
        //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(pieColonia,FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)), 150,20,0);
        //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(pieTelefono,FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)), 150,15,0);
        //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(pieWeb,FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)), 150,10,0);

    }

    public void onPages(PdfWriter writer, Document document) {
        //int numeroPaginas = writer.getCurrentPageNumber();
        //if(numeroPaginas>numeroPaginas-1)
        //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Pagina "+writer.getPageNumber()+" de "+numeroPaginas,FontFactory.getFont("arial",4,Font.NORMAL,BaseColor.BLACK)), 150,300,0);

    }
    public void setImage(String RESOURCE,int width, int height,int x,int y) throws BadElementException, IOException, DocumentException{
        Image img = Image.getInstance(RESOURCE);
        img.scaleAbsoluteWidth(width);
        img.scaleAbsoluteHeight(height);
        img.setAbsolutePosition(x, y);
         writer.getDirectContent().addImage(img);
    }
    public void setWatherMark(String RESOURCE,int width,int height,int x,int y,float transparent) throws IOException, BadElementException, DocumentException{
        Image img = Image.getInstance(RESOURCE);
        PdfContentByte canvas = writer.getDirectContentUnder();
        PdfGState gs1 = new PdfGState();
        gs1.setFillOpacity(transparent);
        canvas.setGState(gs1);
        img.scaleAbsoluteWidth(width);
        img.scaleAbsoluteHeight(height);
        //img.scaleAbsolute(PageSize.A4.rotate());
        img.setAbsolutePosition(x, y);
        canvas.addImage(img);
    }
}
