package org.example.service.impl.image;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.image.ProcessDiagramGenerator;

import java.io.InputStream;

/**
 * bpmnModel bpmn模型
 * activityFontName 活动字体
 * labelFontName 标签字体
 * annotationFontName 标注字体
 * highLightedActivities 高亮活动节点(绿色)
 * runningActivityIdList 正在运行的活动节点(红色)
 * highLightedFlows 高亮顺序流(绿色)
 * lastFlowIdList 最后一条执行的顺序流(红色)    ——————未实现
 */
public interface ICustomProcessDiagramGenerator{


//    InputStream  generateDiagram(BpmnModel bpmnModel, String activityFontName, String labelFontName, String annotationFontName);


}
