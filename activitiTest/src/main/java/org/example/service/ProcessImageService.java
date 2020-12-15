package org.example.service;

import java.io.InputStream;

public interface ProcessImageService {
    InputStream getFlowImgByProcInstId(String procInstId) throws Exception;
}
