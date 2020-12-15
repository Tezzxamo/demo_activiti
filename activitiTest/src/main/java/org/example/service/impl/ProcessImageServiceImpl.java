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

    @Override
    public InputStream getFlowImgByProcInstId(String procInstId) throws Exception {
        return processImageManager.getFlowImgByProcInstId(procInstId);
    }
}
