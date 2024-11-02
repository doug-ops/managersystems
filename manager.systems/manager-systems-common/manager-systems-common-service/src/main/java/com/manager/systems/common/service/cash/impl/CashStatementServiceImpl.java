/*
 * Date create 28/06/2023.
 */
package com.manager.systems.common.service.cash.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.manager.systems.common.dao.cash.CashStatementDao;
import com.manager.systems.common.dao.cash.impl.CashStatementDaoImpl;
import com.manager.systems.common.dto.cash.CashStatemenReportDTO;
import com.manager.systems.common.dto.cash.CashStatementResumeDTO;
import com.manager.systems.common.service.cash.CashStatementService;
import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.CreatePDF;
import com.manager.systems.common.utils.HeaderFooterLandscapePageEvent;
import com.manager.systems.common.utils.HeaderFooterPortablePageEvent;
import com.manager.systems.common.utils.StringUtils;

public class CashStatementServiceImpl implements CashStatementService {

	private CashStatementDao cashStatementDao;
	
	public CashStatementServiceImpl(final Connection connection) {
		super();
		this.cashStatementDao = new CashStatementDaoImpl(connection);
	}
	
	@Override
	public void getCashStatement(final CashStatemenReportDTO report) throws Exception {
		this.cashStatementDao.getCashStatement(report);
	}
	
	@Override
	public byte[] processPdfReport(final CashStatemenReportDTO report) throws Exception 
	{
		final Document document = CreatePDF.createPDFDocument(PageSize.LETTER.rotate(), 5, 5, 60, 30);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PdfWriter writer = PdfWriter.getInstance(document, baos);
        
        final PdfPTable tableHeader = new PdfPTable(1);
    	tableHeader.setTotalWidth(680);
    	tableHeader.setLockedWidth(true);
 
    	final StringBuilder pdfTitle = new StringBuilder();
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_CASH_STATEMENT);
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.TRACO);
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_PERIOD.toUpperCase());
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfTitle.append(ConstantDataManager.LABEL_FROM);   
    	pdfTitle.append(ConstantDataManager.SPACE);
    	pdfTitle.append(report.getDateFromString());
    	pdfTitle.append(ConstantDataManager.SPACE);
    	pdfTitle.append(ConstantDataManager.LABEL_TO);    	
    	pdfTitle.append(ConstantDataManager.SPACE);
    	pdfTitle.append(report.getDateToString());
    	    	   	
    	PdfPCell cell = new PdfPCell(new Phrase(pdfTitle.toString(), CreatePDF.HELVETICA_BOLD_ELEMENTO));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(1);
		cell.setBorderWidth(0);				
    	tableHeader.addCell(cell);
    	    	
    	final StringBuilder pdfSubtitle2 = new StringBuilder();
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.PIPE);    	
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_CREDIT);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.REAL); 
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
   // 	pdfSubtitle2.append(StringUtils.formatDecimalValue(report.getTotalCredit()));
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.PIPE); 
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);    	
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_DEBIT);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.REAL); 
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    //	pdfSubtitle2.append(StringUtils.formatDecimalValue(report.getTotalDebit()));    	
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.PIPE); 
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);    	
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.REAL); 
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    //	pdfSubtitle2.append(StringUtils.formatDecimalValue(report.getTotalBalance()));   
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.PIPE); 
        cell = new PdfPCell(new Phrase(pdfSubtitle2.toString(), CreatePDF.HELVETICA_BOLD_ELEMENTO));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(1);
		cell.setBorderWidth(0);				
    	tableHeader.addCell(cell);
        
    	final HeaderFooterLandscapePageEvent event = new HeaderFooterLandscapePageEvent(tableHeader);
        writer.setPageEvent(event);
        
        document.open();
        PdfPTable table = null;
        
  
        final float[] widths = new float[report.getResumeMap().size()+2];
        for(int i = 0; i < widths.length; i++) {
        	widths[i] = 15;
        }
       	double balance = 0; 
        table = CreatePDF.createTable(widths.length, widths);	 
        
        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 1, 1);
        for(final Map.Entry<Integer, CashStatementResumeDTO> entryResume : report.getResumeMap().entrySet()) {
        	CreatePDF.addCell(table, entryResume.getValue().getWeekTitleLabel(), Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
        }
        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
        
        
//        widths = new float[] {15,15,15,15,15,15,15,15};
//        
//       	//double balance = 0; 
//        table = CreatePDF.createTable(widths.length, widths);	      
//        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_MOVEMENT_DATE, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 1, 1);
//        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DOCUMENTS, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
//        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_MOVEMENT_TYPE, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
//        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DESCRIPTION, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
//       	CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_VALUE), Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
//       	CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL), Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
//       	
//       	CreatePDF.addCell(table, report.getDateFromString(), Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1, 1, 0, 1);
//        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.BLANK, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
//        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.BLANK, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
//        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.BLANK, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
//       	CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.BLANK, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
//       	CreatePDF.addCell(table, StringUtils.formatDecimalValue(balance), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
       	
//       	for(final ProviderStatementItemDTO item : report.getItens())
//		{ 
//    		CreatePDF.addCell(table, item.getDocumentDate(), Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1, 1, 0, 1);
//        	CreatePDF.addCell(table, String.valueOf(item.getDocumentParentId()), Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
//        	CreatePDF.addCell(table, item.getTypeMovementDescription(), Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
//        	CreatePDF.addCell(table, item.getMovementDescription(), Element.ALIGN_LEFT, 3, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
//           	CreatePDF.addCell(table, StringUtils.formatDecimalValue(item.getDocumentValue()), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
//           	balance = (balance + item.getDocumentValue());
//        	CreatePDF.addCell(table, StringUtils.formatDecimalValue(balance), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
//		}
////       	
//       	CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 0);
//       	CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
//       	CreatePDF.addCell(table, StringUtils.formatDecimalValue(balance), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
//       	
       	
           	
        document.add(table);
        document.close();        
        return baos.toByteArray();
	}
}