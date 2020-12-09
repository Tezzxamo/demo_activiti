package org.example.service;

import java.io.InputStream;

public interface ImageService {
    InputStream getFlowImgByProcInstId(String procInstId) throws Exception;
}
