package org.jeecg.modules.demo.bankstatement.service;

import org.jeecg.modules.demo.bankstatement.entity.BankStatement;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;

/**
 * @Description: 流水表
 * @Author: jeecg-boot
 * @Date:   2020-02-28
 * @Version: V1.0
 */
public interface IBankStatementService extends IService<BankStatement> {
    int insertMaximumBalance(HashMap hashMap);
}
