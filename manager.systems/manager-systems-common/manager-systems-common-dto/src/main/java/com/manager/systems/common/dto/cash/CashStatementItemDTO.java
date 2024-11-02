/*
 * Date create 04/07/2023.
 */
package com.manager.systems.common.dto.cash;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashStatementItemDTO implements Serializable {

	private static final long serialVersionUID = -3626071478700389902L;
	
	private int movementType;
	private int weekYear;
	private String paymentData;
	private String cashierClosingDate;
	private String creationDate;
	private String changeDate;
	private long documentParentId;
	private long documentId;
	private int documentType;
	private boolean credit;
	private long companyId;
	private String companyDescription;
	private long providerId;
	private String providerDescription;
	private long bankAccountId;
	private String bankAccountDescription;
	private long documentTransferId;
	private boolean isDocumentTransfer;
	private double documentValue;
	private boolean isPaymentResidue;
	private int documentStatus;
}
