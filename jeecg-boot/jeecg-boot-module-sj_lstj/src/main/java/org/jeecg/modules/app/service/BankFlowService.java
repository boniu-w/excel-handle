package org.jeecg.modules.app.service;

import org.jeecg.modules.app.entity.BankFlow;
import org.jeecg.modules.app.entity.TableData;

import java.util.List;

/**
 * @author wg
 * @Package org.jeecg.modules.app.service
 * @date 2020/3/26 11:24
 * @Copyright
 */
public interface BankFlowService {

    List<BankFlow> examimeBankFlowByCondition(TableData tableData);
}
