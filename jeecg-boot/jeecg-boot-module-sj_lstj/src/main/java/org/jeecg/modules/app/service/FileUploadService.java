package org.jeecg.modules.app.service;

import org.jeecg.modules.app.entity.FileUpload;

import java.util.List;

/**
 * @author wg
 * @Package org.jeecg.modules.app.service
 * @date 2020/3/26 17:11
 * @Copyright
 */
public interface FileUploadService {

    String examineFileName(String fileName);
    int insertIntoFileUpload(FileUpload fileUpload);
    FileUpload examineFileUpload(String titleName);
    List<FileUpload> examineAllFileInfo();
}
