package org.jeecg.modules.app.service;



import org.jeecg.modules.app.entity.MatchingToWordbook;
import org.jeecg.modules.app.entity.Wordbook;

import java.util.HashMap;
import java.util.List;

/**
 * @author wg
 * @Package org.jeecg.modules.demo.datawash.service
 * @date 2020/1/8 17:56
 * @Copyright
 */
public interface WordbookInterface {
    List<Wordbook> examineWordbookAll();
    List<MatchingToWordbook> examineMatchingToWordbook();
    int insertBankFlow(HashMap hashMap);
    String examineWordbookFieldNameByType(Integer type);
    String examineWordbookFieldCodeByType(Integer type);
    List<String> examineFiledNameFromMatchingToWordbook();
}
