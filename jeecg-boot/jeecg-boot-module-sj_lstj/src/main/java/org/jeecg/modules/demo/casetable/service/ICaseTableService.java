package org.jeecg.modules.demo.casetable.service;

import java.util.HashMap;

import org.jeecg.modules.demo.casetable.entity.CaseTable;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 案件表
 * @Author: jeecg-boot
 * @Date:   2020-02-25
 * @Version: V1.0
 */
public interface ICaseTableService extends IService<CaseTable> {
	void upCase(HashMap hashMap);

}
