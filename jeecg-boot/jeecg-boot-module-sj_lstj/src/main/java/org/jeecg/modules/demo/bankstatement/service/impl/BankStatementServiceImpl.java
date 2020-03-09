package org.jeecg.modules.demo.bankstatement.service.impl;

import org.jeecg.modules.demo.bankstatement.entity.BankStatement;
import org.jeecg.modules.demo.bankstatement.mapper.BankStatementMapper;
import org.jeecg.modules.demo.bankstatement.service.IBankStatementService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @Description: 流水表
 * @Author: jeecg-boot
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@Service
public class BankStatementServiceImpl extends ServiceImpl<BankStatementMapper, BankStatement> implements IBankStatementService {

    @Resource
    BankStatementMapper bankStatementMapper;

    @Override
    public int insertMaximumBalance(HashMap hashMap) {
        return bankStatementMapper.insertMaximumBalance(hashMap);
    }
}
