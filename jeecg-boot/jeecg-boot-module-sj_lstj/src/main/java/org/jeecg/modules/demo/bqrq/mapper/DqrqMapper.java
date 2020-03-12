package org.jeecg.modules.demo.bqrq.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.demo.bankstatement.entity.BankStatement;
import org.jeecg.modules.demo.bqrq.entity.Dqrq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 补全流水表
 * @Author: jeecg-boot
 * @Date:   2020-03-11
 * @Version: V1.0
 */
public interface DqrqMapper extends BaseMapper<Dqrq> {
	List<Dqrq> selectbqrq(HashMap hashMap);
}
