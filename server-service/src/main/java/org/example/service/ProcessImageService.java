package org.example.service;

import java.io.InputStream;

/**
 * 流程图service
 *
 * @author Tethamo_zzx
 */
public interface ProcessImageService {

    /**
     * 根据流程实例Id获取流程图
     *
     * @param procInstId 流程实例id
     * @return inputStream
     * @throws Exception exception
     */
    InputStream getFlowImgByProcInstId(String procInstId) throws Exception;
}
