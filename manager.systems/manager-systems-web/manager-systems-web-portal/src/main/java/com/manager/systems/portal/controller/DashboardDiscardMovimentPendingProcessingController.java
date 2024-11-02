package com.manager.systems.portal.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.manager.systems.common.dto.adm.MachineTypeDTO;
import com.manager.systems.common.dto.dashboard.DashboardDiscardMovimentPendingProcessingDTO;
import com.manager.systems.common.service.dashboard.DashboardDiscardMovimentPendingProcessingService;
import com.manager.systems.common.service.impl.dashboard.DashboardDiscardMovimentPendingProcessingServiceImpl;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.model.admin.User;
import com.manager.systems.portal.utils.ConstantDataManager;
import com.manager.systems.web.adm.exception.AdminException;
import com.manager.systems.web.common.controller.BaseController;
import com.manager.systems.web.movements.exception.MovementException;

@Controller
@RequestMapping(value = ConstantDataManager.DASHBOARD_DISCARD_MOVEMENT_PENDING_PROCESSING_CONTROLLER)
public class DashboardDiscardMovimentPendingProcessingController extends BaseController {
	
	@PostMapping(value = ConstantDataManager.DASHBOARD_FILTER)
	public ResponseEntity<String> filter(final HttpServletRequest request, final Model model) throws Exception {
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new Gson();

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;

		Connection connection = null;

		try {
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final DashboardDiscardMovimentPendingProcessingService service = new DashboardDiscardMovimentPendingProcessingServiceImpl(connection);
			final List<DashboardDiscardMovimentPendingProcessingDTO> itens =  service.getData(user.getId());

			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_ITENS, itens);
			status = true;
		} catch (final MovementException ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} catch (final Exception ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}
	
	@RequestMapping(value=ConstantDataManager.COMMON_CONTROLLER_DISCARD_MOVEMENT_METHOD, method=RequestMethod.POST)
	public ResponseEntity<String> discardMovement(final HttpServletRequest request) throws Exception
	{
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
		
		Connection connection = null;
		MachineTypeDTO machineType = null;
				
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final String idParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);
			
			final DashboardDiscardMovimentPendingProcessingService service = new DashboardDiscardMovimentPendingProcessingServiceImpl(connection);
			final boolean isSaved = service.discardMovement(Integer.valueOf(idParameter));
			
			if(!isSaved)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_DASHBOARD_DISCARD_MOVEMENT_ERROR, null));
			}
			
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
		}
		catch (final AdminException ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
		}
		finally 
		{
			if(connection!=null)
			{
				connection.close();
			}
		}	
		
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_DATA, machineType);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}
}