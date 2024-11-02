/*
 * Date create 28/06/2023.
 */
package com.manager.systems.common.service.cash;

import com.manager.systems.common.dto.cash.CashStatemenReportDTO;

public interface CashStatementService {
	void getCashStatement(CashStatemenReportDTO report) throws Exception;
	byte[] processPdfReport(CashStatemenReportDTO report) throws Exception;
}