package com.manager.systems.common.service.impl.dashboard;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.dashboard.DashboardDiscardMovimentPendingProcessingDao;
import com.manager.systems.common.dao.impl.dashboard.DashboardDiscardMovimentPendingProcessingDaoImpl;
import com.manager.systems.common.dto.dashboard.DashboardDiscardMovimentPendingProcessingDTO;
import com.manager.systems.common.service.dashboard.DashboardDiscardMovimentPendingProcessingService;

public class DashboardDiscardMovimentPendingProcessingServiceImpl implements DashboardDiscardMovimentPendingProcessingService {

	private DashboardDiscardMovimentPendingProcessingDao dao;

	public DashboardDiscardMovimentPendingProcessingServiceImpl(final Connection connection) {
		super();
		this.dao = new DashboardDiscardMovimentPendingProcessingDaoImpl(connection);
	}
	
	@Override
	public List<DashboardDiscardMovimentPendingProcessingDTO> getData(final long userId) throws Exception {
		return this.dao.getData(userId);
	}
	
	@Override
	public boolean discardMovement(final long id) throws Exception 
	{
		return this.dao.discardMovement(id);
	}
}