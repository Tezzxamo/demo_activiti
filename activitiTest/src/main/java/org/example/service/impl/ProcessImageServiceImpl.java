package org.example.service.impl;


import org.example.manager.ProcessImageManager;
import org.example.service.ProcessImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessImageServiceImpl implements ProcessImageService {

    ProcessImageManager processImageManager;

    @Autowired
    public ProcessImageServiceImpl(ProcessImageManager processImageManager) {
        this.processImageManager = processImageManager;
    }

    /**
     * 根据流程实例Id获取流程图
     *
     * @param procInstId 流程实例id
     * @return inputStream
     * @throws Exception exception
     */
    @Override
    public InputStream getFlowImgByProcInstId(String procInstId) throws Exception {
        return processImageManager.getFlowImgByProcInstId(procInstId);
    }
}
