package org.jeecg.modules.demo.bankstatement.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.demo.bankstatement.entity.BankStatement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 流水表
 * @Author: jeecg-boot
 * @Date:   2020-02-28
 * @Version: V1.0
 */
public interface BankStatementMapper extends BaseMapper<BankStatement> {
    int insertMaximumBalance(HashMap hashMap);
    List<BankStatement> selectCard(HashMap hashMap);
    List<BankStatement> selectMaxDate();
    List<BankStatement> selectMaxBalance(HashMap hashMap);
}
