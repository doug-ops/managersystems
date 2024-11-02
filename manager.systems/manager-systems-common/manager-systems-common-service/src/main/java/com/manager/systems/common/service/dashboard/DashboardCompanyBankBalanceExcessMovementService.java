package com.manager.systems.common.service.dashboard;

import java.util.List;

import com.manager.systems.common.dto.dashboard.DashboardCompanyBankBalanceExcessMovementDTO;

public interface DashboardCompanyBankBalanceExcessMovementService {
	List<DashboardCompanyBankBalanceExcessMovementDTO> getData(long userId) throws Exception;
}
