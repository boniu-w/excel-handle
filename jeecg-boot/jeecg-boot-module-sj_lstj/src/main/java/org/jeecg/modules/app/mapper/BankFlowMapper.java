package org.jeecg.modules.app.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.app.entity.BankFlow;
import org.jeecg.modules.app.entity.TableData;

import java.util.List;

/**
 * @author wg
 * @Package org.jeecg.modules.app.mapper
 * @date 2020/3/26 11:28
 * @Copyright
 */
@Mapper
public interface BankFlowMapper {

    List<BankFlow> examimeBankFlowByCondition(TableData tableData);

}
