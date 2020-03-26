package org.jeecg.modules.app.service.impl;

import org.jeecg.modules.app.entity.FileUpload;
import org.jeecg.modules.app.mapper.FileUploadMapper;
import org.jeecg.modules.app.service.FileUploadService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wg
 * @Package org.jeecg.modules.app.service
 * @date 2020/3/26 17:11
 * @Copyright
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Resource
    FileUploadMapper fileUploadMapper;

    @Override
    public String examineFileName(String fileName) {

        String s = fileUploadMapper.examineFileName(fileName);
        return s;
    }

    @Override
    public int insertIntoFileUpload(FileUpload fileUpload) {
        int i = fileUploadMapper.insertIntoFileUpload(fileUpload);
        return i;
    }
}
