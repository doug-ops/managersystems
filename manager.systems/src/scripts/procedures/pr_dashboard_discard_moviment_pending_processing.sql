USE `manager`;
DROP procedure IF EXISTS `pr_dashboard_discard_moviment_pending_processing`;

DELIMITER $$
USE `manager`$$
CREATE PROCEDURE `pr_dashboard_discard_moviment_pending_processing`(IN _operation INT,
																	 IN _user_id BIGINT,
                                                                     IN _id BIGINT)
BEGIN
	 DECLARE _client_id TINYINT DEFAULT 0;
    
	/**Drop temporary tables*/
    DROP TEMPORARY TABLE IF EXISTS tmp_company_filter; 
    /**End Drop temporary tables*/
    
	/**Temporary table to insert tmp_company_filter*/
	CREATE TEMPORARY TABLE IF NOT EXISTS tmp_company_filter
	(
		company_id BIGINT NOT NULL, 
        company_description VARCHAR(200),
        inactive BIT
	);
	/**End temporary table to insert tmp_company_filter*/
    
    SELECT IFNULL((SELECT u.client_id FROM user u WHERE u.id = _user_id LIMIT 1),0) INTO _client_id;
    
	INSERT INTO tmp_company_filter (company_id, company_description, inactive)
	SELECT c.id, c.social_name, p.inactive FROM company c
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
             
    IF(1=_operation) THEN		
		BEGIN 
			update product_movement set processing = 1 where id = _id;
        END;
	END IF;  /** End operation 1*/
	IF(4 = _operation) THEN
		BEGIN  
			SELECT 
				x.company_id, f.company_description, x.id, x.product_id, x.product_description,
				x.input_movement, x.credit_in_final, x.output_movement, x.credit_out_final,
				x.clock_movement, x.credit_clock_final, x.change_date, x.is_offline
            FROM
            (
				SELECT 
					pm.company_id,
					pm.id, 
					pm.product_id,
					po.description product_description,
					po.input_movement,
					pm.credit_in_final,
					po.output_movement,
					pm.credit_out_final,
					po.clock_movement,
					pm.credit_clock_final,
					pm.reading_date change_date, 
                    pm.is_offline
				FROM product_movement pm
				INNER JOIN product po on pm.product_id = po.id
				WHERE pm.processing = 0
            ) x
            INNER JOIN tmp_company_filter f ON x.company_id = f.company_id
            ORDER BY x.change_date;
		END;
	END IF;  /** End operation 4*/
END$$

CALL pr_dashboard_discard_moviment_pending_processing(4,8, null);
