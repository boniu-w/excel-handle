package org.jeecg.modules.demo.maximumbalance.service;

import java.util.HashMap;
import java.util.List;

import org.jeecg.modules.demo.maximumbalance.entity.MaximumBalance;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 最大余额表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface IMaximumBalanceService extends IService<MaximumBalance> {
	List<MaximumBalance> list1(HashMap hashMap);
	List<MaximumBalance> list2(HashMap hashMap);

}
