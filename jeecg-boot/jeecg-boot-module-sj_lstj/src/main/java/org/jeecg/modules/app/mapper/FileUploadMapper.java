package org.jeecg.modules.app.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.app.entity.FileUpload;

import java.util.List;

/**
 * @author wg
 * @Package org.jeecg.modules.app.mapper
 * @date 2020/3/26 16:59
 * @Copyright
 */
@Mapper
public interface FileUploadMapper {

    String examineFileName(String fileName);
    int insertIntoFileUpload(FileUpload fileUpload);
    FileUpload examineFileUpload(String titleName);
    List<FileUpload> examineAllFileInfo();
}
