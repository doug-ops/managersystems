package com.manager.systems.common.dto.dashboard;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DashboardDiscardMovimentPendingProcessingDTO {
	private long companyId;
	private String companyDescription;
	private long id;
	private long productId;
	private String productDescription;
	private double inputMovement;
	private double creditInFinal;
	private double outputMovement;
	private double creditOutFinal;
	private double clockMovement;
	private double creditClockFinal;
	private Date changeDate;
	private int processing;
	private String changeDateString;
	private boolean isOffline;
}