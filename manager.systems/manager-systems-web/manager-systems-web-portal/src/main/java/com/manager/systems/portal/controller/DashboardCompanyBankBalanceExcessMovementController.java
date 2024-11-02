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

import com.google.gson.Gson;
import com.manager.systems.common.dto.dashboard.DashboardCompanyBankBalanceExcessMovementDTO;
import com.manager.systems.common.service.dashboard.DashboardCompanyBankBalanceExcessMovementService;
import com.manager.systems.common.service.impl.dashboard.DashboardCompanyBankBalanceExcessMovementServiceImpl;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.model.admin.User;
import com.manager.systems.portal.utils.ConstantDataManager;
import com.manager.systems.web.common.controller.BaseController;
import com.manager.systems.web.movements.exception.MovementException;

@Controller
@RequestMapping(value = ConstantDataManager.DASHBOARD_COMPANY_BANK_BALANCE_EXCESS_MOVEMENT_CONTROLLER)
public class DashboardCompanyBankBalanceExcessMovementController extends BaseController {
	
	@PostMapping(value = ConstantDataManager.DASHBOARD_COMPANY_BANK_BALANCE_EXCESS_MOVEMENT_FILTER)
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
			
			final DashboardCompanyBankBalanceExcessMovementService service = new DashboardCompanyBankBalanceExcessMovementServiceImpl(connection);
			final List<DashboardCompanyBankBalanceExcessMovementDTO> itens =  service.getData(user.getId());

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
}
