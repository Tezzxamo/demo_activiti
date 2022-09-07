package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.example.enumeration.CodeEnum;
import org.example.exceptions.BusinessException;
import org.example.service.ProcessImageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.InputStream;
import java.io.OutputStream;


@Validated
@RestController
@RequestMapping("/v1/processImage")
@RequiredArgsConstructor
public class ProcessImageController {

    private final ProcessImageService processImageService;

    @GetMapping(value = "/{processInstanceId}/image", name = "生成流程图")
    public void processImageGet(@PathVariable("processInstanceId") @NotBlank(message = "processInstanceId不能为空") String processInstanceId,
                                HttpServletResponse httpServletResponse) {
        try (OutputStream outputStream = httpServletResponse.getOutputStream()) {
            InputStream img = processImageService.getFlowImgByProcInstId(processInstanceId);
            byte[] bytes = IOUtils.toByteArray(img);
            httpServletResponse.setContentType("image/svg+xml");
            outputStream.write(bytes);
            outputStream.flush();
        } catch (Exception e) {
            throw new BusinessException(CodeEnum.IMAGE_CREATE_FAIL, e.getMessage());
        }
    }
}