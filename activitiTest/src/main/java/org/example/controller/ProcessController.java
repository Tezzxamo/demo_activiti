package org.example.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 功能描述:流程控制器
 *
 * @author Jelly
 * @created 2019年11月19日
 * @version 1.0.0
 */
@Controller
@RequestMapping("/process")
public class ProcessController extends BaseController {

    /**
     * 测试 url：http://localhost:8080/process/deploy?name=A&&resource=A
     * @param request 网页请求
     * @return 返回成功与否
     */
    @RequestMapping("/deploy")
    public String deploy(HttpServletRequest request, HttpServletResponse response){
        String name = request.getParameter("name");
        String resource = request.getParameter("resource");
        if (StringUtils.isEmpty(resource)||StringUtils.isEmpty(name)){
            System.out.println("param error");
            return "";
        }
        //重复部署会增加 版本 VERSION_ 的值
        try{
            //创建部署对象
            Deployment deploy = repositoryService.createDeployment()
                    .name(name)
                    .addClasspathResource("processes/"+resource+".bpmn20.xml")
                    .deploy();
        }catch (Exception e){
            System.out.println("fail");
            return "";
        }
        System.out.println("success");
        return "";
    }



}