package org.jeecg.modules.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.entity.BankFlow;
import org.jeecg.modules.app.entity.ConditionData;
import org.jeecg.modules.app.mapper.BankFlowMapper;
import org.jeecg.modules.app.service.BankFlowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wg
 * @Package org.jeecg.modules.app.service
 * @date 2020/3/26 11:27
 * @Copyright
 */
@Service
public class BankFlowServiceImpl extends ServiceImpl<BankFlowMapper,BankFlow> implements BankFlowService{


    @Resource
    BankFlowMapper bankFlowMapper;

    @Override
    public List<BankFlow> examimeBankFlowByCondition(ConditionData conditionData) {
        List<BankFlow> bankFlows = bankFlowMapper.examimeBankFlowByCondition(conditionData);
        return bankFlows;
    }

    @Override
    public void clearBankFlow() {
        bankFlowMapper.clearBankFlow();
    }
}
