package org.jeecg.modules.app.entity;

import lombok.Data;



/**
 * @author wg
 * @date 2020/1/8 17:27
 * @Copyright
 */
@Data
public class ConditionData {

    private String cardEntity;
    private String startTime;
    private String endTime;
    private Float startMoney;
    private Float endMoney;
    private String counterParty; // 交易对手
    private String recoveryMark;
}
