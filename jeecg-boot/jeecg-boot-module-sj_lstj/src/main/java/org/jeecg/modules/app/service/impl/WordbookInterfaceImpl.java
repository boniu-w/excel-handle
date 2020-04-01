package org.jeecg.modules.app.service.impl;

import org.jeecg.modules.app.entity.MatchingToWordbook;
import org.jeecg.modules.app.entity.Wordbook;
import org.jeecg.modules.app.mapper.WordbookMapper;
import org.jeecg.modules.app.service.WordbookInterface;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wg
 * @Package org.jeecg.modules.demo.datawash.service.impl
 * @date 2020/1/8 17:56
 * @Copyright
 */
@Service
public class WordbookInterfaceImpl implements WordbookInterface {



    @Resource
    WordbookMapper wordbookMapper;

    @Override
    public List<Wordbook> examineWordbookAll(){
        List<Wordbook> wordbooks = wordbookMapper.examineWordbookAll();
        return wordbooks;
    }

    @Override
    public List<MatchingToWordbook> examineMatchingToWordbook() {
        List<MatchingToWordbook> matchingToWordbooks = wordbookMapper.examineMatchingToWordbook();
        return matchingToWordbooks;
    }

    @Override
    public int insertBankFlow(Map map) {
        wordbookMapper.insertBankFlow(map);
        return 0;
    }

    @Override
    public String examineWordbookFieldNameByType(Integer type) {
        String fieldName = wordbookMapper.examineWordbookFieldNameByType(type);
        return fieldName;
    }

    @Override
    public String examineWordbookFieldCodeByType(Integer type) {
        String fieldCode = wordbookMapper.examineWordbookFieldCodeByType(type);
        return fieldCode;
    }

    @Override
    public List<String> examineFiledNameFromMatchingToWordbook() {
        List<String> stringList = wordbookMapper.examineFiledNameFromMatchingToWordbook();
        return stringList;
    }

}
