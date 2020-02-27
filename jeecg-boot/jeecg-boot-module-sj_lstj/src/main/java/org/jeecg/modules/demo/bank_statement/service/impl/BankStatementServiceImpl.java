package org.jeecg.modules.demo.bank_statement.service.impl;

import org.jeecg.modules.demo.bank_statement.entity.BankStatement;
import org.jeecg.modules.demo.bank_statement.mapper.BankStatementMapper;
import org.jeecg.modules.demo.bank_statement.service.IBankStatementService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 银行流水表
 * @Author: jeecg-boot
 * @Date:   2020-02-27
 * @Version: V1.0
 */
@Service
public class BankStatementServiceImpl extends ServiceImpl<BankStatementMapper, BankStatement> implements IBankStatementService {

}
