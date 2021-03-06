package org.jeecg.modules.demo.maximumbalance.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.demo.bankstatement.entity.BankStatement;
import org.jeecg.modules.demo.maximumbalance.entity.MaximumBalance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 最大余额表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface MaximumBalanceMapper extends BaseMapper<MaximumBalance> {
	List<MaximumBalance> list1(HashMap hashMap);
	List<MaximumBalance> list2(HashMap hashMap);
}
