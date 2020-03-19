package org.jeecg.modules.demo.maximumbalance.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.jeecg.modules.demo.bankstatement.mapper.BankStatementMapper;
import org.jeecg.modules.demo.maximumbalance.entity.MaximumBalance;
import org.jeecg.modules.demo.maximumbalance.mapper.MaximumBalanceMapper;
import org.jeecg.modules.demo.maximumbalance.service.IMaximumBalanceService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 最大余额表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
@Service
public class MaximumBalanceServiceImpl extends ServiceImpl<MaximumBalanceMapper, MaximumBalance> implements IMaximumBalanceService {
	@Resource
    MaximumBalanceMapper m;
	
	@Override
	public List<MaximumBalance> list1(HashMap hashMap) {
		// TODO Auto-generated method stub
		return m.list1(hashMap);
	}
	
	@Override
	public List<MaximumBalance> list2(HashMap hashMap) {
		// TODO Auto-generated method stub
		return m.list2(hashMap);
	}

}
