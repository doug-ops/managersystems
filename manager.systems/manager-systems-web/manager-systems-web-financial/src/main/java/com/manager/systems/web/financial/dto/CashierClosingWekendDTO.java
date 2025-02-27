/**
 *Create Date 12/02/2022
 */
package com.manager.systems.web.financial.dto;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manager.systems.common.utils.StringUtils;

public class CashierClosingWekendDTO implements Serializable 
{
	private static final long serialVersionUID = -5234403116121957942L;
	
	private Map<Long, Map<Integer, CashierClosingWekendItemDTO>> itens = new TreeMap<Long, Map<Integer, CashierClosingWekendItemDTO>>();
	private Map<Integer, CashierClosingWekendItemDTO> mapDate = new TreeMap<Integer, CashierClosingWekendItemDTO>();	
	private Gson gson;
	
	public CashierClosingWekendDTO() 
	{
		super();
		this.gson = new Gson();
	}
	
	public final Map<Long, Map<Integer, CashierClosingWekendItemDTO>> getItens() {
		return this.itens;
	}
	
	public final Map<Integer, CashierClosingWekendItemDTO> getMapDate() {
		return this.mapDate;
	}
	
	public final void addItem (final CashierClosingItemDTO item) throws Exception
	{
		final Long companyId = item.getCompanyId();
		final String companyDescription = item.getCompany();
		final double documentValue = item.getDocumentValue(); 
		final String paymentDate = item.getPaymentData();
		
		final Calendar convertPaymentDate = StringUtils.convertStringDateToCalendar(paymentDate, StringUtils.DATE_FORMAT_DD_MM_YYYY);
		int week = convertPaymentDate.get(Calendar.WEEK_OF_YEAR);
		
		Map<Integer, CashierClosingWekendItemDTO> mapDateCompany = this.itens.get(companyId);
		if(mapDateCompany == null) {			
			final String jsonString = gson.toJson(this.mapDate);			 
			final Type type = new TypeToken<TreeMap<Integer, CashierClosingWekendItemDTO>>(){}.getType();
			final Map<Integer, CashierClosingWekendItemDTO> clonedMap = gson.fromJson(jsonString, type);
			for (final Map.Entry<Integer, CashierClosingWekendItemDTO> entryWeek : clonedMap.entrySet()) {
				entryWeek.getValue().setCompanyId(companyId);
				entryWeek.getValue().setCompanyDescription(companyDescription);
			}
			
			mapDateCompany = clonedMap;
			
			this.itens.put(companyId, mapDateCompany);
		}
		
		CashierClosingWekendItemDTO movementWekendItem = mapDateCompany.get(week);
		if(movementWekendItem == null) {
			week = ((CashierClosingWekendItemDTO)mapDateCompany.values().toArray() [mapDateCompany.size()-1]).getWeek();
			movementWekendItem = mapDateCompany.get(week);
		}
		if(movementWekendItem!=null) {
			movementWekendItem.setCompanyId(companyId);
			movementWekendItem.setCompanyDescription(companyDescription);
			movementWekendItem.sumDocumentValue(item.isCredit() ? documentValue : (documentValue < 0 ? documentValue : (documentValue * -1)));
			mapDateCompany.put(week, movementWekendItem);
			this.itens.put(companyId, mapDateCompany);
		}
	}
	
	
	public final void addItemCashierClosing (final CashierClosingItemDTO item) throws Exception
	{
		final Long companyId = item.getCompanyId();
		final String companyDescription = item.getCompany();
		final double documentValue = item.getDocumentValue(); 
		final String paymentDate = item.getPaymentData();
		
		final Calendar convertPaymentDate = StringUtils.convertStringDateToCalendar(paymentDate, StringUtils.DATE_FORMAT_DD_MM_YYYY);
		int week = convertPaymentDate.get(Calendar.WEEK_OF_YEAR);
		
		Map<Integer, CashierClosingWekendItemDTO> mapDateCompany = this.itens.get(companyId);
		if(mapDateCompany == null) {			
			final String jsonString = gson.toJson(this.mapDate);			 
			final Type type = new TypeToken<TreeMap<Integer, CashierClosingWekendItemDTO>>(){}.getType();
			final Map<Integer, CashierClosingWekendItemDTO> clonedMap = gson.fromJson(jsonString, type);
			for (final Map.Entry<Integer, CashierClosingWekendItemDTO> entryWeek : clonedMap.entrySet()) {
				entryWeek.getValue().setCompanyId(companyId);
				entryWeek.getValue().setCompanyDescription(companyDescription);
			}
			
			mapDateCompany = clonedMap;
			
			this.itens.put(companyId, mapDateCompany);
		}
		
		CashierClosingWekendItemDTO movementWekendItem = mapDateCompany.get(week);
		if(movementWekendItem == null) {
			week = ((CashierClosingWekendItemDTO)mapDateCompany.values().toArray() [mapDateCompany.size()-1]).getWeek();
			movementWekendItem = mapDateCompany.get(week);
		}
		if(movementWekendItem!=null) {
			movementWekendItem.setCompanyId(companyId);
			movementWekendItem.setCompanyDescription(companyDescription);
			movementWekendItem.sumDocumentValue(item.isCredit() ? documentValue : (documentValue < 0 ? documentValue : (documentValue * -1)));
			mapDateCompany.put(week, movementWekendItem);
			this.itens.put(companyId, mapDateCompany);
		}
	}
	
	public final void prepareMapDate(final LocalDateTime dateFrom, final LocalDateTime dateTo) {
		final WeekFields weekFields = WeekFields.of(Locale.getDefault()); 
		final int initialWeekNumber = dateFrom.get(weekFields.weekOfWeekBasedYear());
		 int finalWeekNumber = dateTo.get(weekFields.weekOfWeekBasedYear());
		
		LocalDateTime date = dateFrom;
	    Date dateConver = Date.from(dateTo.atZone(ZoneId.systemDefault()).toInstant());
        Calendar calendario = Calendar.getInstance();
        
        calendario.setTime(dateConver);
        
        int day = calendario.get(Calendar.DAY_OF_MONTH);
        int month = calendario.get(Calendar.MONTH) + 1;
       
        if(day == 27 && month == 2) {
        	finalWeekNumber = finalWeekNumber -1;
        }
		for(int i = initialWeekNumber; i <= finalWeekNumber; i++) {
			
			final String initialDayOfWeek = getInitialDayOfWeek(date);
			final String finalDayOfWeek = getFinalDayOfWeek(date);
			
			final CashierClosingWekendItemDTO movementWekendItem = new CashierClosingWekendItemDTO();
			movementWekendItem.setWeek(i);
			movementWekendItem.setWeekDescription(initialDayOfWeek + " à " + finalDayOfWeek);
			movementWekendItem.sumDocumentValue(0d);			
			
			this.mapDate.put(i, movementWekendItem);
			
			date = date.plusDays(7);
		}
	}
	
	private final String getInitialDayOfWeek(final LocalDateTime date) {
		final int dayOfWeek = date.getDayOfWeek().getValue();
		if(dayOfWeek != 1) {
			return StringUtils.formatDate(date.minusDays(dayOfWeek-1), StringUtils.DATE_PATTERN_DD_MM);
		}else {
			return StringUtils.formatDate(date.minusDays(0), StringUtils.DATE_PATTERN_DD_MM);
		}
	}
	
	private final String getFinalDayOfWeek(final LocalDateTime date) {
		final int dayOfWeek = date.getDayOfWeek().getValue();
		if(dayOfWeek != 7) {
			return StringUtils.formatDate(date.plusDays((7-dayOfWeek)), StringUtils.DATE_PATTERN_DD_MM);
		}else {
			return StringUtils.formatDate(date.plusDays(0), StringUtils.DATE_PATTERN_DD_MM);
		}
	}
	
	public final Map<String, Map<Integer, CashierClosingWekendItemDTO>> getOrdernadCompany(){
		
		final Map<String, Map<Integer, CashierClosingWekendItemDTO>> result = new TreeMap<>();
		for(Map.Entry<Long, Map<Integer, CashierClosingWekendItemDTO>> entry : this.itens.entrySet()){
			
			String companyDescription = null;
			for(Map.Entry<Integer, CashierClosingWekendItemDTO> entryItem : entry.getValue().entrySet()) {
				companyDescription = entryItem.getValue().getCompanyDescription();
				if(!StringUtils.isNull(companyDescription)) {
					break;
				}
			}
			result.put(companyDescription, entry.getValue());
		}
		
		
		return result;
	}
}