package com.manager.systems.common.dao.impl.dashboard;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.dashboard.DashboardDiscardMovimentPendingProcessingDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.dashboard.DashboardDiscardMovimentPendingProcessingDTO;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.OperationType;

public class DashboardDiscardMovimentPendingProcessingDaoImpl implements DashboardDiscardMovimentPendingProcessingDao {

	private Connection connection;
	
	public DashboardDiscardMovimentPendingProcessingDaoImpl(Connection connection) {
		super();
		this.connection = connection;
	}
	
	@Override
	public List<DashboardDiscardMovimentPendingProcessingDTO> getData(final long userId) throws Exception {

		CallableStatement statement = null;
		ResultSet resultSet = null;
		
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_dashboard_discard_moviment_pending_processing");
		query.append("(");
		query.append("?, "); //01 - _operation INT,
		query.append("?, "); //02 - IN _user_id BIGINT,
		query.append("? "); //03 - IN _id BIGINT
		query.append(")");
		query.append("}");
		
		final List<DashboardDiscardMovimentPendingProcessingDTO> items = new ArrayList<>();
			
		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - _operation INT,
			statement.setLong(2, userId);//02 - IN _user_id BIGINT
			statement.setNull(3, Types.BIGINT);//03 - IN _id BIGINT
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				items.add(DashboardDiscardMovimentPendingProcessingDTO
						.builder()
						.id(resultSet.getInt(DatabaseConstants.COLUMN_ID))
						.companyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID))
						.companyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION))
						.productId(resultSet.getLong(DatabaseConstants.COLUMN_PRODUCT_ID))
						.productDescription(resultSet.getString(DatabaseConstants.COLUMN_PRODUCT_DESCRIPTION))
						.inputMovement(resultSet.getDouble(DatabaseConstants.COLUMN_INPUT_MOVEMENT))
						.creditInFinal(resultSet.getDouble(DatabaseConstants.COLUMN_CREDIT_IN_FINAL))
						.outputMovement(resultSet.getDouble(DatabaseConstants.COLUMN_OUTPUT_MOVEMENT))
						.creditOutFinal(resultSet.getDouble(DatabaseConstants.COLUMN_CREDIT_OUT_FINAL))
						.clockMovement(resultSet.getDouble(DatabaseConstants.COLUMN_CLOCK_MOVEMENT))
						.creditClockFinal(resultSet.getDouble(DatabaseConstants.COLUMN_CREDIT_CLOCK_FINAL))
						.changeDate(resultSet.getDate(DatabaseConstants.COLUMN_CHANGE_DATE))
						.changeDateString(StringUtils.formatDate(resultSet.getDate(DatabaseConstants.COLUMN_CHANGE_DATE), StringUtils.DATE_PATTERN_DD_MM_YYYY))
						.isOffline(resultSet.getBoolean(DatabaseConstants.COLUMN_IS_OFFLINE))
						.build());
				
			}
		} catch (final Exception ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}

		return items;
	}
	
	@Override
	public boolean discardMovement(final long id) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_dashboard_discard_moviment_pending_processing");
		query.append("(");
		query.append("?, "); //01 - _operation INT,
		query.append("?, "); //02 - IN _user_id BIGINT,
		query.append("? "); //03 - IN _id BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - _operation INT,
			statement.setNull(2, Types.BIGINT);//02 - IN _user_id BIGINT
			statement.setLong(3, id);//03 - IN _id BIGINT
			result = (statement.executeUpdate()>0); 
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
		return result;
	}
}