package org.jeecg.modules.demo.bqrq.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.jeecg.modules.demo.bankstatement.mapper.BankStatementMapper;
import org.jeecg.modules.demo.bqrq.entity.Dqrq;
import org.jeecg.modules.demo.bqrq.mapper.DqrqMapper;
import org.jeecg.modules.demo.bqrq.service.IDqrqService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 补全流水表
 * @Author: jeecg-boot
 * @Date:   2020-03-11
 * @Version: V1.0
 */
@Service
public class DqrqServiceImpl extends ServiceImpl<DqrqMapper, Dqrq> implements IDqrqService {

	 @Resource
	 DqrqMapper Dqrqmapper;
	
	@Override
	public List<Dqrq> selectbqrq(HashMap hashMap) {
		// TODO Auto-generated method stub
		return Dqrqmapper.selectbqrq(hashMap);
	}

}
