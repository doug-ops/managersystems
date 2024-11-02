/*
 * Date create 04/07/2023.
 */
package com.manager.systems.common.dto.cash;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashStatemenReportDTO {

	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	private String companysIds;
	private String documentsTypeIds;
	private String userChildrensParent;
	private String reportTitle;
	private String reportSubTitle;
	private int groupBy;
	private Map<Integer, CashStatementResumeDTO> resumeMap;
	
	public CashStatemenReportDTO() {
		super();
	}
	
	public final String getDateFromString() {
		return StringUtils.formatDate(this.dateFrom, StringUtils.DATE_PATTERN_DD_MM_YYYY);
	}
	
	public final String getDateToString() {
		return StringUtils.formatDate(this.dateTo, StringUtils.DATE_PATTERN_DD_MM_YYYY);
	}
	
	public final void addItem(final CashStatementItemDTO item)
	{
		if(this.resumeMap == null) {
			this.resumeMap = new TreeMap<>();
		}
		
		CashStatementResumeDTO resume = this.resumeMap.get(item.getWeekYear());
		if(resume == null) {
			resume = CashStatementResumeDTO.builder().build();
		}
		
		resume.addItem(item);
		this.resumeMap.put(item.getWeekYear(), resume);
	}
}