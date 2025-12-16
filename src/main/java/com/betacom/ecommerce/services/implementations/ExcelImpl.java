package com.betacom.ecommerce.services.implementations;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.betacom.ecommerce.dto.output.OrderDTO;
import com.betacom.ecommerce.dto.output.OrderItemDTO;
import com.betacom.ecommerce.dto.output.SpedizioneDTO;
import com.betacom.ecommerce.services.interfaces.IExcelServices;

@Service
public class ExcelImpl implements IExcelServices {

	private int rowIdx = 0;
	private CellStyle titleStyle = null;		
	private CellStyle labelStyle = null;
	private CellStyle headerStyle = null;
	private CellStyle moneyStyle = null;
	private CellStyle dateStyle = null;
	private CellStyle wrapStyle = null;
	
	@Override
	public byte[] exportOrder(OrderDTO order) throws Exception {
		try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			rowIdx = 0;
			Sheet sheet = wb.createSheet("Ordine");
			/*
			 * Create Styles
			 */
			DataFormat df = wb.createDataFormat();

			titleStyle = createStyleTitle(wb);		
			labelStyle = createStyleLabel(wb);
			headerStyle = createStyleHeader(wb);

			moneyStyle = wb.createCellStyle();
			moneyStyle.setDataFormat(df.getFormat("#,##0.00 €"));

			dateStyle = wb.createCellStyle();
			dateStyle.setDataFormat(df.getFormat("dd/mm/yyyy"));

			wrapStyle = wb.createCellStyle();
			wrapStyle.setWrapText(true);

			/**
			 * Build Header
			 */
			sheet = buildTitle(sheet, "Ordine numero " + order.getNumeroOrdine());
			rowIdx= rowIdx + 2; // empty row
			sheet = buildHeader(sheet, order);

			/**
			 * Build detaglio ordine
			 */
			rowIdx++; // empty row before table
			
			sheet = buildTitle(sheet, "Dettaglio Ordine");
			rowIdx= rowIdx+2; // 2 empty row before table
			
			sheet = buildDetaglio(sheet, order);
			
			// Column widths  lunghezza * 256  (formato interne di excel)
			
			sheet.setColumnWidth(0, 40 * 256); // Nome
			sheet.setColumnWidth(1, 24 * 256); // Artista
			sheet.setColumnWidth(2, 14 * 256); // Supporto
			sheet.setColumnWidth(3, 16 * 256); // Prezzo unitario
			sheet.setColumnWidth(4, 10 * 256); // Quantita
			sheet.setColumnWidth(5, 16 * 256); // Prezzo totale

			wb.write(out);
			return out.toByteArray();
		}
	}

	/**
	 * create style title
	 */
		
	private CellStyle createStyleTitle (Workbook wb) {
		CellStyle titleStyle = wb.createCellStyle();
		Font titleFont = wb.createFont();
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 14);
		titleStyle.setFont(titleFont);
		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		return titleStyle;
	}
	private CellStyle createStyleLabel (Workbook wb) {
		CellStyle labelStyle = wb.createCellStyle();
		Font labelFont = wb.createFont();
		labelFont.setBold(true);
		labelStyle.setFont(labelFont);
		return labelStyle;
	}
	

	private CellStyle createStyleHeader (Workbook wb) {
		CellStyle headerStyle = wb.createCellStyle();
		Font headerFont = wb.createFont();
		headerFont.setBold(true);
		headerStyle.setFont(headerFont);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);
		return headerStyle;
	}
	
	private Sheet buildTitle(Sheet sheet, String label) {  // merge del title sulle 5 columns
		Row titleRow = sheet.createRow(rowIdx);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue(label);
		titleCell.setCellStyle(titleStyle);
		sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 0, 5));
		return sheet;
	}
	
	/**
	 * create Header
	 */	
	private Sheet buildHeader(Sheet sheet, OrderDTO order) {

		rowIdx = writeKV(sheet, rowIdx, "Data ordine", order.getDataOrdine(), labelStyle, dateStyle);
		rowIdx = writeKV(sheet, rowIdx, "Data invio", order.getDataInvio(), labelStyle, dateStyle);

		rowIdx = writeKV(sheet, rowIdx, "Status", order.getStatus(), labelStyle, null);
		rowIdx = writeKV(sheet, rowIdx, "Modalità pagamento", order.getModalitaPagamento(), labelStyle, null);
		
		Row destRow = sheet.createRow(rowIdx++);  // destinatatio non usa writekv perché la valore é merged 1-5
		Cell destLabel = destRow.createCell(0);
		destLabel.setCellValue("Destinatario");
		destLabel.setCellStyle(labelStyle);
		
		Cell destValue = destRow.createCell(1);
		destValue.setCellValue(buildDestinatario(order.getSpedizione()));
		destValue.setCellStyle(wrapStyle);

		sheet.addMergedRegion(new CellRangeAddress(
			    destRow.getRowNum(), // first row
			    destRow.getRowNum(), // last row
			    1,                   // first column (B)
			    5                    // last column (F)
			));
		
		rowIdx++; // empty row
		
		// Totale ordine
		
		Row totalRow = sheet.createRow(rowIdx++);
		Cell totalLabel = totalRow.createCell(0);
		totalLabel.setCellValue("Totale ordine");
		totalLabel.setCellStyle(labelStyle);

		Cell totalValue = totalRow.createCell(1);
		totalValue.setCellValue(order.getPrezzoTotale());
		totalValue.setCellStyle(moneyStyle);
		return sheet;
	}
	/**
	 * Build detaglio
	 */
	private Sheet buildDetaglio(Sheet sheet, OrderDTO order) {
		String[] headers = { "Nome", "Artista", "Supporto", "Prezzo unitario", "Quantita", "Prezzo totale" };
		Row headerRow = sheet.createRow(rowIdx++);
		for (int i = 0; i < headers.length; i++) {  // create header tabella
			Cell c = headerRow.createCell(i);
			c.setCellValue(headers[i]);
			c.setCellStyle(headerStyle);
		}

		Optional.ofNullable(order.getRiga())  // se per caso non ci sono righe (caso improbabile)
	    .stream()
	    .flatMap(List::stream)
	    .forEach(it -> {
	        Row r = sheet.createRow(rowIdx++);
	        
	        // Nome
	        Cell c0 = r.createCell(0, CellType.STRING);
	        c0.setCellValue(Optional.ofNullable(it.getProductName()).orElse(""));
	        c0.setCellStyle(wrapStyle);
	        
	        // Artista
	        Cell c1 = r.createCell(1, CellType.STRING);
	        c1.setCellValue(Optional.ofNullable(it.getArtist()).orElse(""));
	        
	        // Supporto
	        Cell c2 = r.createCell(2, CellType.STRING);
	        c2.setCellValue(Optional.ofNullable(it.getSupporto()).orElse(""));
	        
	        // Prezzo unitario
	        Cell c3 = r.createCell(3, CellType.NUMERIC);
	        c3.setCellValue(it.getPrezzoUnitario());
	        c3.setCellStyle(moneyStyle);
	        
	        // Quantità
	        Cell c4 = r.createCell(4, CellType.NUMERIC);
	        c4.setCellValue(Optional.ofNullable(it.getQuantita()).orElse(0));
	        
	        // Prezzo totale
	        Cell c5 = r.createCell(5, CellType.NUMERIC);
	        c5.setCellValue(it.getPrezzoDaPagare());
	        c5.setCellStyle(moneyStyle);
	    });

		return sheet;
	}
	
	/**
	 * Scrive una riga key-value: Col A = label, Col B = value
	 */
	private int writeKV(Sheet sheet, int rowIdx, String label, Object value, CellStyle labelStyle,
			CellStyle valueStyle) {
		Row row = sheet.createRow(rowIdx++);
		Cell c0 = row.createCell(0);
		c0.setCellValue(label);
		if (labelStyle != null)
			c0.setCellStyle(labelStyle);

		Cell c1 = row.createCell(1);

		if (value == null) {
			c1.setCellValue("");
		} else if (value instanceof LocalDate ld) {
			// Excel data "vera"
			c1.setCellValue(java.sql.Date.valueOf(ld));  // POI non é compatibile DataUtil
			if (valueStyle != null)
				c1.setCellStyle(valueStyle);
		} else if (value instanceof Number n) {
			c1.setCellValue(n.doubleValue());
			if (valueStyle != null)
				c1.setCellStyle(valueStyle);
		} else {
			c1.setCellValue(String.valueOf(value));
			if (valueStyle != null)
				c1.setCellStyle(valueStyle);
		}
		return rowIdx;
	}

	private static String nullToEmpty(String s) {
		return s == null ? "" : s;
	}

	
	private static String buildDestinatario(SpedizioneDTO s) {
		if (s == null)
			return "";
		return s.getNome() + " " + s.getCognome() + " " + s.getVia() + " " + s.getCommune() + " " + s.getCap();
	}
}
