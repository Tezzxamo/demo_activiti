package org.example.service.impl.image;

import org.activiti.bpmn.model.BpmnModel;

import java.io.InputStream;
import java.util.List;

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

    //指定字体
    InputStream generateDiagramCustom(BpmnModel bpmnModel,
                                      List<String> highLightedActivities,
                                      List<String> runningActivityIdList,
                                      List<String> highLightedFlows,
                                      List<String> runningActivityFlowsIds,
                                      String activityFontName,
                                      String labelFontName,
                                      String annotationFontName);

    InputStream generateDiagramCustom(BpmnModel bpmnModel,
                                      List<String> highLightedActivities,
                                      List<String> runningActivityIdList,
                                      List<String> highLightedFlows,
                                      String activityFontName,
                                      String labelFontName,
                                      String annotationFontName);

    InputStream generateDiagramCustom(BpmnModel bpmnModel,
                                      List<String> highLightedActivities,
                                      List<String> highLightedFlows,
                                      String activityFontName,
                                      String labelFontName,
                                      String annotationFontName);

    //无指定字体，使用默认字体
    InputStream generateDiagramCustom(BpmnModel bpmnModel,
                                      List<String> highLightedActivities,
                                      List<String> runningActivityIdList,
                                      List<String> highLightedFlows,
                                      List<String> runningActivityFlowsIds);

    InputStream generateDiagramCustom(BpmnModel bpmnModel,
                                      List<String> highLightedActivities,
                                      List<String> runningActivityIdList,
                                      List<String> highLightedFlows);

    InputStream generateDiagramCustom(BpmnModel bpmnModel,
                                      List<String> highLightedActivities,
                                      List<String> highLightedFlows);

}
