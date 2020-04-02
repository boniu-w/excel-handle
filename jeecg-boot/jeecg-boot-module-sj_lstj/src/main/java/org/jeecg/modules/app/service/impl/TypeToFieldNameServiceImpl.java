package org.jeecg.modules.app.service.impl;

import org.jeecg.modules.app.entity.TypeToFieldName;
import org.jeecg.modules.app.mapper.TypeToFieldNameMapper;
import org.jeecg.modules.app.service.TypeToFieldNameService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wg
 * @Package org.jeecg.modules.app.service.impl
 * @date 2020/4/2 13:42
 * @Copyright
 */
@Service
public class TypeToFieldNameServiceImpl implements TypeToFieldNameService {

    @Resource
    TypeToFieldNameMapper typeToFieldNameMapper;

    @Override
    public List<TypeToFieldName> examineAll() {
        List<TypeToFieldName> typeToFieldNameList = typeToFieldNameMapper.examineAll();
        return typeToFieldNameList;
    }
}
