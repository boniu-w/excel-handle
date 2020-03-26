package org.jeecg.modules.app.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.app.entity.MatchingToWordbook;
import org.jeecg.modules.app.entity.Wordbook;

import java.util.HashMap;
import java.util.List;

/**
 * @author wg
 * @date 2019/12/11 9:54
 * @Copyright
 */
@Mapper
public interface WordbookMapper {

    List<Wordbook> examineWordbookAll();

    List<MatchingToWordbook> examineMatchingToWordbook();

    int insertBankFlow(HashMap<String, Object> hashMap);

    String examineWordbookFieldNameByType(Integer type);

    String examineWordbookFieldCodeByType(Integer type);

    List<String> examineFiledNameFromMatchingToWordbook();
}
