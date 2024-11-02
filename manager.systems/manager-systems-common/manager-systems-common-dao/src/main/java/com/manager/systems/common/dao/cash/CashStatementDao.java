/*
 * Create Date 04/07/2023
 */
package com.manager.systems.common.dao.cash;

import com.manager.systems.common.dto.cash.CashStatemenReportDTO;

public interface CashStatementDao {
	void getCashStatement(CashStatemenReportDTO report) throws Exception;
}