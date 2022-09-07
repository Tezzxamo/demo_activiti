package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.vo.ProcessDefinitionVO;
import org.example.service.ProcessService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;


/**
 * 功能描述:测试部署
 *
 * @author Tethamo
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/v1/processDefinition")
@RequiredArgsConstructor
public class ProcessDefinitionController {

    private final ProcessService processService;

    /**
     * 测试 url：ip:port/v1/processDefinition/deploy?name=A&&bpmnPath=A
     */
    @GetMapping(value = "/deploy", name = "部署流程图")
    public ProcessDefinitionVO deploy(@RequestParam @NotBlank(message = "name不能为空") String name,
                                      @RequestParam @NotBlank(message = "bpmnPath不能为空") String bpmnPath) {
        return processService.deployProcessDefinition(name, bpmnPath);
    }


}