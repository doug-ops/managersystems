package com.manager.systems.common.service.adm;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.common.dao.utils.ConnectionUtils;
import com.manager.systems.common.dto.adm.FinancialGroupDTO;
import com.manager.systems.common.dto.adm.ReportFinancialGroupDTO;
import com.manager.systems.common.service.impl.adm.FinancialGroupServiceImpl;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.Combobox;

public class FinancialGroupServiceTest 
{
	private Connection connection = null;
	private FinancialGroupService financialGroupService = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.financialGroupService = new FinancialGroupServiceImpl(this.connection);
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.financialGroupService);
		
		//Save insert
		FinancialGroupDTO financialGroup = new FinancialGroupDTO();
		financialGroup.setId("1");
		financialGroup.setDescription("Teste");
		financialGroup.setInactive(false);
		financialGroup.setChangeData(new ChangeData(1L));		
		final boolean isSaved = this.financialGroupService.save(financialGroup);
		Assert.assertTrue(isSaved);
		
		//Get
		financialGroup = new FinancialGroupDTO();
		financialGroup.setId("1");
		this.financialGroupService.get(financialGroup);
		Assert.assertEquals("Teste", financialGroup.getDescription());

		//Inactive
		financialGroup = new FinancialGroupDTO();
		financialGroup.setId("1");
		financialGroup.setInactive(true);
		financialGroup.setChangeData(new ChangeData(1L));
		final boolean isInactive = this.financialGroupService.inactive(financialGroup);
		Assert.assertTrue(isInactive);
		
		//Get
		financialGroup = new FinancialGroupDTO();
		financialGroup.setId("1");
		this.financialGroupService.get(financialGroup);
		Assert.assertTrue(financialGroup.isInactive());
		
		//Save update
		financialGroup = new FinancialGroupDTO();
		financialGroup.setId("1");
		financialGroup.setDescription("Teste2");
		financialGroup.setInactive(false);
		financialGroup.setChangeData(new ChangeData(1L));		
		final boolean isUpdated = this.financialGroupService.save(financialGroup);
		Assert.assertTrue(isUpdated);		
		
		//Get
		financialGroup = new FinancialGroupDTO();
		financialGroup.setId("1");
		this.financialGroupService.get(financialGroup);
		Assert.assertEquals("Teste2", financialGroup.getDescription());
		Assert.assertFalse(financialGroup.isInactive());		
		
		final ReportFinancialGroupDTO reportFinancialGroup = new ReportFinancialGroupDTO();
		reportFinancialGroup.setFinancialGroupIdFrom("1");
		reportFinancialGroup.setFinancialGroupIdTo("1");
		reportFinancialGroup.setInactive("0");
		reportFinancialGroup.setDescription("Teste2");
		this.financialGroupService.getAll(reportFinancialGroup);
		Assert.assertTrue(reportFinancialGroup.geFinancialGroups().size()>0);	
		
		final List<Combobox> items = this.financialGroupService.getAllCombobox();
		Assert.assertTrue(items.size()>0);
	}

	@After
	public void finaliza() throws Exception
	{
		if(this.connection!=null)
		{
			this.connection.rollback();
			this.connection.close();
		}
		Assert.assertTrue(this.connection.isClosed());
		this.connection = null;		
	}
}