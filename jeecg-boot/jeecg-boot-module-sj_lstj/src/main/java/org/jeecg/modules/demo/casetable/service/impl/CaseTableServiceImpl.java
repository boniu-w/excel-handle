package org.jeecg.modules.demo.casetable.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.jeecg.modules.demo.bankstatement.mapper.BankStatementMapper;
import org.jeecg.modules.demo.casetable.entity.CaseTable;
import org.jeecg.modules.demo.casetable.mapper.CaseTableMapper;
import org.jeecg.modules.demo.casetable.service.ICaseTableService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 案件表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Service
public class CaseTableServiceImpl extends ServiceImpl<CaseTableMapper, CaseTable> implements ICaseTableService {

	 @Resource
	 CaseTableMapper casetableMapper;
	
	@Override
	public void upCase(HashMap hashMap) {
		// TODO Auto-generated method stub
		casetableMapper.upCase(hashMap);
	}

}
