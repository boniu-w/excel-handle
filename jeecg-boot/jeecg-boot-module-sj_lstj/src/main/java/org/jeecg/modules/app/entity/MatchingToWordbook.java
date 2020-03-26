package org.jeecg.modules.app.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author wg
 * @date 2020/1/8 17:25
 * @Copyright
 */
@Data
public class MatchingToWordbook {
    private String id;
    private String fieldName;
    private String fieldCode;
    private Integer type;
    private Date createTime;
    private Date updateTime;
    private String createId;

}
