package umg.programacion2.Reportes;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import umg.programacion2.DataBase.Model.ProductoModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class PdfReport {
    private static final Font TITLE_FONT = new Font(Font.FontFamily.COURIER, 14, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

    public void generateProductReport(List<ProductoModel> productos, String outputPath) throws DocumentException, IOException {
        Document document = new Document(PageSize.LETTER, 50, 50, 50, 50);
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();

        addTitle(document);
        addProductTable(document, productos);

        document.close();
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph("Reporte de Productos", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);
    }

    private void addProductTable(Document document, List<ProductoModel> productos) throws DocumentException {
        PdfPTable table = new PdfPTable(6); // 6 columnas: ID, Descripción, Origen, Precio, Existencia, Precio Total
        table.setWidthPercentage(100);
        addTableHeader(table);
        addRows(table, productos);
        document.add(table);
    }

    private void addTableHeader(PdfPTable table) {
        String[] columnTitles = {"ID", "Descripción", "Origen", "Precio", "Existencia", "Precio Total"};
        for (String columnTitle : columnTitles) {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.CYAN);
            header.setBorderWidth(1);
            header.setPhrase(new Phrase(columnTitle, HEADER_FONT));
            table.addCell(header);
        }
    }

    private void addRows(PdfPTable table, List<ProductoModel> productos) {
        for (ProductoModel producto : productos) {
            table.addCell(new Phrase(String.valueOf(producto.getIdProducto()), NORMAL_FONT));
            table.addCell(new Phrase(producto.getDescripcion(), NORMAL_FONT));
            table.addCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(producto.getPrecio()), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(producto.getExistencia()), NORMAL_FONT));
            DecimalFormat df = new DecimalFormat("#.00");
            String cantidadFormateada = df.format(producto.getExistencia() * producto.getPrecio());
            table.addCell(new Phrase(cantidadFormateada, NORMAL_FONT));
        }
    }
}
