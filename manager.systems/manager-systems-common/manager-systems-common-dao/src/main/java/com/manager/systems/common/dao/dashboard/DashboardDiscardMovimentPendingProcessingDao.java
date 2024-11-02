package com.manager.systems.common.dao.dashboard;

import java.util.List;

import com.manager.systems.common.dto.dashboard.DashboardDiscardMovimentPendingProcessingDTO;

public interface DashboardDiscardMovimentPendingProcessingDao {
	List<DashboardDiscardMovimentPendingProcessingDTO> getData(long userId) throws Exception;
	boolean discardMovement(long id) throws Exception;
}