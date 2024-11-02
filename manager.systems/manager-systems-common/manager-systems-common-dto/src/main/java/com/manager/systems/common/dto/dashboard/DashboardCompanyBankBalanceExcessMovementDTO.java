package com.manager.systems.common.dto.dashboard;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DashboardCompanyBankBalanceExcessMovementDTO {
	private long companyId;
	private String companyDescription;
	private double balance;
	private String movementType;
	private boolean inactive;
}
