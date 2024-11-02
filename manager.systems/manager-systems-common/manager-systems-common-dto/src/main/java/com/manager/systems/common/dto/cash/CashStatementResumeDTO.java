/*
 * Date create 04/07/2023.
 */
package com.manager.systems.common.dto.cash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashStatementResumeDTO implements Serializable{
	
	private static final long serialVersionUID = 5026656839011045820L;

	private int weekYear;
	private String weekDescription;
	private String dateFrom;
	private String dateTo;
	
	public String getWeekTitleLabel() {
		final StringBuilder result = new StringBuilder();
		result.append(this.weekDescription.toUpperCase());
		result.append(ConstantDataManager.SPACE);
		result.append(dateFrom);
		result.append(ConstantDataManager.SPACE);
		result.append(ConstantDataManager.TRACO);
		result.append(ConstantDataManager.SPACE);
		result.append(dateTo);
		return result.toString();
	}
	
	private Map<Integer, CashStatemenResumeItemDTO> resumeMap;
	
	public void addItem(final CashStatementItemDTO item) {
		if(this.resumeMap == null) {
			this.resumeMap = new TreeMap<>();
		}
		
		if(this.weekYear == 0) {
			this.weekYear = item.getWeekYear();
			this.populateWeekData();
		}
		
		CashStatemenResumeItemDTO resumeItem = this.resumeMap.get(item.getMovementType());
		if(resumeItem == null) {
			resumeItem = CashStatemenResumeItemDTO.builder()
					.movementType(item.getMovementType())
					.weekYear(item.getWeekYear())				
			.build();
		}
		resumeItem.addItem(item);
		this.resumeMap.put(item.getMovementType(), resumeItem);
	}
	
	public void populateWeekData() {
		
		final int year = Integer.valueOf(String.valueOf(this.weekYear).substring(0, 4));
		final int week = Integer.valueOf(String.valueOf(this.weekYear).substring(4, 6));
		final WeekFields weekFields = WeekFields.of (new Locale(com.manager.systems.common.utils.ConstantDataManager.PT, com.manager.systems.common.utils.ConstantDataManager.BR));
		LocalDateTime date = LocalDateTime.now()
		        .withYear(year)
		        .with(weekFields.weekOfYear(), week)
		        .with(weekFields.dayOfWeek(), 2)
				.withHour(12)
				.withMinute(0)
				.withSecond(0);
		
		this.dateFrom = StringUtils.formatDate(date, StringUtils.DATE_PATTERN_DD_MM_YYYY);
		
		date = date.plusDays(6);
		this.dateTo = StringUtils.formatDate(date, StringUtils.DATE_PATTERN_DD_MM_YYYY);
		
		this.weekDescription = ConstantDataManager.WEEK_LABEL + ConstantDataManager.SPACE + week;
	}
}