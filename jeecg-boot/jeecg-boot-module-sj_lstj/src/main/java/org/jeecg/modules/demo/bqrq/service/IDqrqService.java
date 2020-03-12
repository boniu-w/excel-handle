package org.jeecg.modules.demo.bqrq.service;

import java.util.HashMap;
import java.util.List;

import org.jeecg.modules.demo.bqrq.entity.Dqrq;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 补全流水表
 * @Author: jeecg-boot
 * @Date:   2020-03-11
 * @Version: V1.0
 */
public interface IDqrqService extends IService<Dqrq> {
	List<Dqrq> selectbqrq(HashMap hashMap);
}
