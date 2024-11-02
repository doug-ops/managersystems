package com.manager.systems.common.service.impl.dashboard;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.dashboard.DashboardCompanyBankBalanceExcessMovementDao;
import com.manager.systems.common.dao.impl.dashboard.DashboardCompanyBankBalanceExcessMovementDaoImpl;
import com.manager.systems.common.dto.dashboard.DashboardCompanyBankBalanceExcessMovementDTO;
import com.manager.systems.common.service.dashboard.DashboardCompanyBankBalanceExcessMovementService;

public class DashboardCompanyBankBalanceExcessMovementServiceImpl implements DashboardCompanyBankBalanceExcessMovementService {

	private DashboardCompanyBankBalanceExcessMovementDao dao;

	public DashboardCompanyBankBalanceExcessMovementServiceImpl(final Connection connection) {
		super();
		this.dao = new DashboardCompanyBankBalanceExcessMovementDaoImpl(connection);
	}
	
	@Override
	public List<DashboardCompanyBankBalanceExcessMovementDTO> getData(final long userId) throws Exception {
		return this.dao.getData(userId);
	}
}