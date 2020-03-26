package org.jeecg.modules.app.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author wg
 * @date 2020/1/8 17:25
 * @Copyright
 */
@Data
public class Wordbook {
    private String id;
    private String fieldName;
    private String fieldCode;
    private String createId;
    private Date createTime;
    private Integer type;

}
