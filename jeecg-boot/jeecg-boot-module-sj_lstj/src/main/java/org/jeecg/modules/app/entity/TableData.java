package org.jeecg.modules.app.entity;

import lombok.Data;



/**
 * @author wg
 * @date 2020/1/8 17:27
 * @Copyright
 */
@Data
public class TableData {

    private String cardEntity;
    private String startTime;
    private String endTime;
    private Double startMoney;
    private Double endMoney;
    private String counterParty;
    private String recoveryMark;
}
