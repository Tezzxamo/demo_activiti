package org.example;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.apache.commons.io.FileUtils;
import org.example.service.ProcessImageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.time.Instant;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageTest {

    @Autowired
    ProcessImageService imageService;

    @Autowired
    RepositoryService repositoryService;

    @Test
    public void test1() throws Exception {
        // 85ab9218-2eee-11eb-801d-d4f5ef050be7
        InputStream image = imageService.getFlowImgByProcInstId("85ab9218-2eee-11eb-801d-d4f5ef050be7");
        String imageName = Instant.now().getEpochSecond() + ".svg";
        FileUtils.copyInputStreamToFile(image, new File("src/main/resources/processes/" + imageName));// 绘图

    }

    @Test
    public void test2() throws IOException {
        BpmnModel bpmnModel = repositoryService.getBpmnModel("交易录入:12:ca0ef17d-3061-11eb-801d-d4f5ef050be7");
        String fileName = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
        ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);


        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bf = new BufferedReader(inputStreamReader);
        String line;
        StringBuilder sb = new StringBuilder();
        while((line = bf.readLine())!=null){
            sb.append(line);
        }
        System.out.println(sb);//输出bpmn文件

    }
}
