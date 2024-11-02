USE `manager`;
DROP procedure IF EXISTS `pr_dashboard_company_bank_balance_excess_movement`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_dashboard_company_bank_balance_excess_movement`(IN _operation INT,
																	 IN _user_id BIGINT)
BEGIN
	
    DECLARE _client_id TINYINT DEFAULT 0;
    
	/**Drop temporary tables*/
    DROP TEMPORARY TABLE IF EXISTS tmp_company_filter; 
    /**End Drop temporary tables*/
    
	/**Temporary table to insert tmp_company_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_company_filter
	(
		company_id BIGINT NOT NULL, 
        inactive BIT
	);
	/**End temporary table to insert tmp_company_filter*/
    
    SELECT IFNULL((SELECT u.client_id FROM user u WHERE u.id = _user_id LIMIT 1),0) INTO _client_id;
    
	INSERT INTO tmp_company_filter (company_id, inactive)
	SELECT c.id, p.inactive FROM company c
	INNER JOIN (
				SELECT company_id FROM user_company WHERE user_id IN 
                (
					SELECT x.id FROM
					(
						SELECT u.id FROM user_parent up 
						INNER JOIN user u ON up.user_id = u.id and up.user_parent_id = _user_id
						and u.client_id = _client_id 
						WHERE up.inactive = 0 GROUP BY u.id
                        UNION ALL 
                        SELECT _user_id id
					) x
					GROUP BY id                       
				) GROUP BY company_id
			) uc ON c.id = uc.company_id
             INNER JOIN person p ON c.person_id = p.id
             WHERE c.client_id = _client_id AND p.person_type_id = 1
             GROUP BY c.id;

	IF(4 = _operation) THEN
		BEGIN  
			SELECT 
				z.company_id, z.company_description, z.company_balance, z.movement_type, z.code_type, f.inactive 
            FROM
            (
				SELECT 
					x.company_id, c.social_name company_description, (b.bank_balance_available - x.balance_moviment) company_balance, 'FC' movement_type, 1 code_type
				FROM 
				(
					SELECT 
						ccc.company_id, sum((ccc.movement_value + ccc.pending_movement_value)) balance_moviment
					FROM 
						cashier_closing_company ccc
					WHERE 
						ccc.cashier_closing_status <> 2    
					GROUP BY ccc.company_id
				) x
				INNER JOIN company c ON x.company_id = c.id
				INNER JOIN person p ON c.person_id = p.id
				INNER JOIN bank_account b ON p.bank_account_id = b.id
				WHERE (b.bank_balance_available - x.balance_moviment) <> 0
				UNION ALL              
				SELECT c.id company_id, c.social_name company_description, b.bank_balance_available balance, 'TA' movement_type, 2 code_type  
				FROM company c
				INNER JOIN person p ON c.person_id = p.id
				INNER JOIN (                
					SELECT 
						ftgs.bank_account_origin_id 
					FROM 
						financial_transfer_group_setting  ftgs
					WHERE 
						ftgs.inactive = 0 
						AND ftgs.bank_account_automatic_transfer_id > 0
						AND ftgs.bank_account_destiny_id = 0
					GROUP BY 
						ftgs.bank_account_origin_id
				) x ON p.bank_account_id = x.bank_account_origin_id
				INNER JOIN bank_account b ON p.bank_account_id = b.id
				WHERE b.bank_balance_available <> 0
            ) z            
            INNER JOIN tmp_company_filter f ON z.company_id = f.company_id;
		END;
	END IF;  /** End operation 1 */
END$$

CALL pr_dashboard_company_bank_balance_excess_movement(4,7);