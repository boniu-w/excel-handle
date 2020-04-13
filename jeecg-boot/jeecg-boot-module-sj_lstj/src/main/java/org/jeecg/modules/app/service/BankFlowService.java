package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.entity.BankFlow;
import org.jeecg.modules.app.entity.ConditionData;

import java.util.List;

/**
 * @author wg
 * @Package org.jeecg.modules.app.service
 * @date 2020/3/26 11:24
 * @Copyright
 */
public interface BankFlowService extends IService<BankFlow> {

    List<BankFlow> examimeBankFlowByCondition(ConditionData conditionData);
    void clearBankFlow();
}
