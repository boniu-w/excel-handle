package org.jeecg.modules.app.entity;

import lombok.Data;

/**
 * @Package org.jeecg.modules.app.entity
 * @author wg
 * @date 2020/4/2 10:58
 * @version
 * @Copyright
 */
@Data
public class TypeToFieldName {

    private Integer type;
    private String fieldName;
    private String  fieldCode;
    private String matchingName;
}
