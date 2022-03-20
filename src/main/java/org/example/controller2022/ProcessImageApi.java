package org.example.controller2022;

import org.apache.commons.io.IOUtils;
import org.example.exceptions.ActivitiCommonException;
import org.example.exceptions.ReturnMessageAndTemplateDef;
import org.example.service.ProcessImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Tethamo
 */
@RestController
public class ProcessImageApi {

    ProcessImageService processImageService;

    @Autowired
    public ProcessImageApi(ProcessImageService processImageService) {
        this.processImageService = processImageService;
    }

    /**
     * 生成流程图
     * @param processInstanceId 流程实例ID
     * @param httpServletResponse response entity
     */
    @GetMapping("/{processInstanceId}/image")
    public void processImageGet(@PathVariable String processInstanceId, HttpServletResponse httpServletResponse){
        if (StringUtils.isEmpty(processInstanceId)) {
            throw new ActivitiCommonException(ReturnMessageAndTemplateDef.Errors.UNKNOWN_PROCESS_NAME);
        }
        try {
            InputStream img = processImageService.getFlowImgByProcInstId(processInstanceId);
            byte[] bytes = IOUtils.toByteArray(img);
            httpServletResponse.setContentType("image/svg+xml");
            OutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            throw new ActivitiCommonException(ReturnMessageAndTemplateDef.Errors.IMAGE_CREATE_FAIL,e);
        }
    }
}