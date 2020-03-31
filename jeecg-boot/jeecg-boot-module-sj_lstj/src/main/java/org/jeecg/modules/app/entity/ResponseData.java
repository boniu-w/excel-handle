package org.jeecg.modules.app.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wg
 * @date 2020/1/8 17:28
 * @Copyright
 */
@Data
public class ResponseData {

    private String message;
    private String[] titles;
    private Integer count;
    private String fileMessage;
    private Map titleMap;
    private List<BankFlow> bankFlowList;
    private String fileName;
    private String conditionExcelMessage;


}
