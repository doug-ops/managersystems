package com.manager.systems.common.service.dashboard;

import java.util.List;

import com.manager.systems.common.dto.dashboard.DashboardDiscardMovimentPendingProcessingDTO;
public interface DashboardDiscardMovimentPendingProcessingService {
	List<DashboardDiscardMovimentPendingProcessingDTO> getData(long userId) throws Exception;
	boolean discardMovement(long id) throws Exception;
}