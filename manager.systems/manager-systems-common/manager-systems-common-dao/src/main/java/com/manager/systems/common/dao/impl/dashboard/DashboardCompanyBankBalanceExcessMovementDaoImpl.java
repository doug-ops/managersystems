package com.manager.systems.common.dao.impl.dashboard;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.vo.OperationType;

import com.manager.systems.common.dao.dashboard.DashboardCompanyBankBalanceExcessMovementDao;
import com.manager.systems.common.dto.dashboard.DashboardCompanyBankBalanceExcessMovementDTO;

public class DashboardCompanyBankBalanceExcessMovementDaoImpl implements DashboardCompanyBankBalanceExcessMovementDao {

	private Connection connection;
	
	public DashboardCompanyBankBalanceExcessMovementDaoImpl(Connection connection) {
		super();
		this.connection = connection;
	}
	
	@Override
	public List<DashboardCompanyBankBalanceExcessMovementDTO> getData(final long userId) throws Exception {

		CallableStatement statement = null;
		ResultSet resultSet = null;
		
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_dashboard_company_bank_balance_excess_movement");
		query.append("(");
		query.append("?, "); //01 - _operation INT,
		query.append("? "); //02 - IN _user_id BIGINT
		query.append(")");
		query.append("}");
		
		final List<DashboardCompanyBankBalanceExcessMovementDTO> items = new ArrayList<>();
			
		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - _operation INT,
			statement.setLong(2, userId);//02 - IN _user_id BIGINT
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				items.add(DashboardCompanyBankBalanceExcessMovementDTO
						.builder()
						.companyId(resultSet.getLong(DatabaseConstants.COLUMN_COMPANY_ID))
						.companyDescription(resultSet.getString(DatabaseConstants.COLUMN_COMPANY_DESCRIPTION))
						.balance(resultSet.getDouble(DatabaseConstants.COLUMN_COMPANY_BALANCE))
						.movementType(resultSet.getString(DatabaseConstants.COLUMN_MOVEMENT_TYPE))
						.inactive(resultSet.getBoolean(DatabaseConstants.COLUMN_INACTIVE))
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
}
