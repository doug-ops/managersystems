/*
 * Date create 28/06/2023.
 */
package com.manager.systems.common.dao.cash.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;

import com.manager.systems.common.dao.cash.CashStatementDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.cash.CashStatemenReportDTO;
import com.manager.systems.common.dto.cash.CashStatementItemDTO;

public class CashStatementDaoImpl implements CashStatementDao {
	
	private Connection connection;
	
	public CashStatementDaoImpl(final Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public void getCashStatement(final CashStatemenReportDTO report) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;
		
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_cash_statement_report");
		query.append("(");
		query.append("?, "); //01 - IN _companys_ids VARCHAR(500)
		query.append("?, "); //02 - IN _date_from DATETIME
		query.append("?, "); //03 - IN _date_to DATETIME
		query.append("?, "); //04 - IN _document_type_ids VARCHAR(500)
		query.append("?  "); //05 - IN _user_childrens_parent VARCHAR(5000)
		query.append(")");
		query.append("}");
		try
		{
			statement = connection.prepareCall(query.toString());
			statement.setString(1, report.getCompanysIds()); //01 - IN _companys_ids VARCHAR(500)
			statement.setTimestamp(2, Timestamp.valueOf(report.getDateFrom())); //02 - IN _date_from DATETIME,
			statement.setTimestamp(3, Timestamp.valueOf(report.getDateTo())); //03 - IN _date_to DATETIME
			statement.setString(4, report.getDocumentsTypeIds()); //04 - IN _document_type_ids VARCHAR(500)
			statement.setString(5, report.getUserChildrensParent()); //06 - IN _user_childrens_parent VARCHAR(5000)
			
	        boolean hasResults = statement.execute();  
	        do
	        {
	        	if(hasResults)
	        	{
		        	resultSet = statement.getResultSet();
		        	while (resultSet.next()) 
			        {
	        			final CashStatementItemDTO item = CashStatementItemDTO.builder()
	        					.movementType(resultSet.getInt(DatabaseConstants.COLUMN_MOVEMENT_TYPE))
	        					.weekYear(resultSet.getInt(DatabaseConstants.COLUMN_WEEK_YEAR))
	        					.paymentData(resultSet.getString(DatabaseConstants.COLUMN_PAYMENT_DATA))
	        					.cashierClosingDate(resultSet.getString(DatabaseConstants.COLUMN_CASHIER_CLOSING_DATE))
	        					.creationDate(resultSet.getString(DatabaseConstants.COLUMN_CREATION_DATE))
	        					.changeDate(resultSet.getString(DatabaseConstants.COLUMN_CHANGE_DATE))
	        					.documentParentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_PARENT_ID))
	        					.documentId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_ID))
	        					.documentType(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_TYPE))
	        					.credit(resultSet.getInt(DatabaseConstants.COLUMN_CREDIT) == 1 ? true : false)
	        					.companyId(resultSet.getInt(DatabaseConstants.COLUMN_COMPANY_ID))
	        					.providerId(resultSet.getLong(DatabaseConstants.COLUMN_PROVIDER_ID))
	        					.bankAccountId(resultSet.getLong(DatabaseConstants.COLUMN_BANK_ACCOUNT_ID))
	        					.documentTransferId(resultSet.getLong(DatabaseConstants.COLUMN_DOCUMENT_TRANSFER_ID))
	        					.isDocumentTransfer(resultSet.getInt(DatabaseConstants.COLUMN_FINANCIAL_IS_DOCUMENT_TRANSFER) == 1 ? true : false)
	        					.documentValue(resultSet.getDouble(DatabaseConstants.COLUMN_DOCUMENT_VALUE))
	        					.isPaymentResidue(resultSet.getInt(DatabaseConstants.COLUMN_PAYMENT_RESIDUE) == 1 ? true : false)
	        					.documentStatus(resultSet.getInt(DatabaseConstants.COLUMN_DOCUMENT_STATUS))
	        					.build();	        			        		
	        			report.addItem(item);
			        }
	        	}	        	
	        	hasResults = statement.getMoreResults();
	        } while (hasResults);
		}
		catch(final Exception ex)
		{
			throw ex;
		}
		finally
		{
			if(statement!=null)
			{
				statement.close();
			}
		}
	}
}
