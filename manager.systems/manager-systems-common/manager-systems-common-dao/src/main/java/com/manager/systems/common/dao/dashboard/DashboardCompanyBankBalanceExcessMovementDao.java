package com.manager.systems.common.dao.dashboard;

import java.util.List;

import com.manager.systems.common.dto.dashboard.DashboardCompanyBankBalanceExcessMovementDTO;

public interface DashboardCompanyBankBalanceExcessMovementDao {
	List<DashboardCompanyBankBalanceExcessMovementDTO> getData(long userId) throws Exception;
}