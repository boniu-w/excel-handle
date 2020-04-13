package org.jeecg.modules.app.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author wg
 * @date 2020/1/8 17:27
 * @Copyright
 */
@Data
public class RequestBean {

    private List<ConditionData> conditionDataList;
    private List<MultipartFile> fileList;
}
