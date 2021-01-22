package org.example.manager;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.Execution;
import org.example.service.impl.ProcessImageServiceImpl;
import org.example.service.impl.image.CustomProcessDiagramGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProcessImageManager {
    private final Logger logger = LoggerFactory.getLogger(ProcessImageManager.class);

    RepositoryService repositoryService;
    ProcessHistoryManager processHistoryManager;
    ProcessInstanceManager processInstanceManager;

    @Autowired
    public ProcessImageManager(RepositoryService repositoryService,
                               ProcessHistoryManager processHistoryManager,
                               ProcessInstanceManager processInstanceManager) {
        this.repositoryService = repositoryService;
        this.processHistoryManager = processHistoryManager;
        this.processInstanceManager = processInstanceManager;
    }

    public InputStream getFlowImgByProcInstId(String procInstId) throws Exception {
        if (StringUtils.isEmpty(procInstId)) {
            logger.error("[错误]-传入的参数procInstId为空！");
            throw new Exception("[异常]-传入的参数procInstId为空！");
        }
        InputStream imageStream = null;
        try {
            // 通过流程实例ID获取历史流程实例
            HistoricProcessInstance historicProcessInstance = processHistoryManager.getHistoricProcessInstance(procInstId);

            // 通过流程实例ID获取流程中已经执行的节点，按照执行先后顺序排序
            List<HistoricActivityInstance> historicActivityInstanceList = processHistoryManager.getHistoricActivityInstancesAsc(procInstId);

            // 将已经执行的节点放入高亮显示节点集合
            List<String> highLightedActivityIdList = historicActivityInstanceList.stream()
                    .map(HistoricActivityInstance::getActivityId)
                    .collect(Collectors.toList());

            // 通过流程实例ID获取流程中正在执行的节点
            List<Execution> runningActivityInstanceList = processInstanceManager.getRunningActivityInstance(procInstId);
            List<String> runningActivityIdList = new ArrayList<>();
            for (Execution execution : runningActivityInstanceList) {
                if (!StringUtils.isEmpty(execution.getActivityId())) {
                    runningActivityIdList.add(execution.getActivityId());
                }
            }

            // 定义流程画布生成器
            CustomProcessDiagramGenerator processDiagramGenerator = new CustomProcessDiagramGenerator();

            // 获取流程定义Model对象
            BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

            // 获取已经流经的流程线，需要高亮显示流程已经发生流转的线id集合
//            List<String> highLightedFlowsIds = getHighLightedFlows(bpmnModel, historicActivityInstanceList);
            List<String> highLightedFlowsIds = getHighLightedFlowsByIncomingFlows(bpmnModel, historicActivityInstanceList);

            // 根据runningActivityIdList获取runningActivityFlowsIds
            List<String> runningActivityFlowsIds = getRunningActivityFlowsIds(bpmnModel, runningActivityIdList, historicActivityInstanceList);

            // 使用默认配置获得流程图表生成器，并生成追踪图片字符流
            imageStream = processDiagramGenerator.generateDiagramCustom(
                    bpmnModel,
                    highLightedActivityIdList, runningActivityIdList, highLightedFlowsIds, runningActivityFlowsIds,
                    "宋体", "微软雅黑", "黑体");
            return imageStream;
        } catch (Exception e) {
            logger.error("通过流程实例ID【{}】获取流程图时出现异常！", e.getMessage());
            throw new Exception("通过流程实例ID" + procInstId + "获取流程图时出现异常！", e);
        } finally {
            if (imageStream != null) {
                imageStream.close();
            }
        }
    }

    /**
     * @param bpmnModel                    bpmnModel
     * @param historicActivityInstanceList historicActivityInstanceList
     * @return HighLightedFlows
     */
    public List<String> getHighLightedFlows(BpmnModel bpmnModel,
                                            List<HistoricActivityInstance> historicActivityInstanceList) {
        //historicActivityInstanceList 是 流程中已经执行的历史活动实例
        // 已经流经的顺序流，需要高亮显示
        List<String> highFlows = new ArrayList<>();

        // 全部活动节点
        List<FlowNode> allHistoricActivityNodeList = new ArrayList<>();

        // 拥有endTime的历史活动实例，即已经完成了的节点
        List<HistoricActivityInstance> finishedActivityInstancesList = new ArrayList<>();

        /*
         * 循环的目的：
         *           获取所有的历史节点FlowNode并放入allHistoricActivityNodeList
         *           获取所有确定结束了的历史节点finishedActivityInstancesList
         */
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            // 获取流程节点
            // bpmnModel.getMainProcess()获取一个Process对象
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
            allHistoricActivityNodeList.add(flowNode);

            // 如果结束时间不为空，表示当前节点已经完成
            if (historicActivityInstance.getEndTime() != null) {
                finishedActivityInstancesList.add(historicActivityInstance);
            }
        }

        FlowNode currentFlowNode;
        FlowNode targetFlowNode;
        HistoricActivityInstance currentActivityInstance;

        // 遍历已经完成的活动实例，从每个实例的outgoingFlows中找到已经执行的
        for (int k = 0; k < finishedActivityInstancesList.size(); k++) {
            currentActivityInstance = finishedActivityInstancesList.get(k);

            // 获得当前活动对应的节点信息以及outgoingFlows信息
            currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currentActivityInstance.getActivityId(), true);

            // 当前节点的所有流出线
            List<SequenceFlow> outgoingFlowList = currentFlowNode.getOutgoingFlows();

            /*
             * 遍历outgoingFlows并找到已经流转的  满足如下条件认为已经流转：
             * 1、当前节点是并行网关或者兼容网关，则通过outgoingFlows能够在历史活动中找到的全部节点均为已经流转
             * 2、当前节点是以上两种类型之外的，通过outgoingFlows查找到的时间最早的流转节点视为有效流转
             * (第二点有问题，有过驳回的，会只绘制驳回的流程线，通过走向下一级的流程线没有高亮显示)
             */
            if ("parallelGateway".equals(currentActivityInstance.getActivityType()) ||
                    "inclusiveGateway".equals(currentActivityInstance.getActivityType())) {
                // 遍历历史活动节点，找到匹配流程目标节点的
                for (SequenceFlow outgoingFlow : outgoingFlowList) {
                    // 获取当前节点流程线对应的下一级节点
                    targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(outgoingFlow.getTargetRef(), true);

                    // 如果下级节点包含在所有历史节点中，则将当前节点的流出线高亮显示
                    if (allHistoricActivityNodeList.contains(targetFlowNode)) {
                        highFlows.add(outgoingFlow.getId());
                    }
                }
            } else {
                /*
                 * 2、当前节点不是并行网关或者兼容网关
                 * 【已解决-问题】如果当前节点有驳回功能，驳回到申请节点，
                 * 则因为申请节点在历史节点中，导致当前节点驳回到申请节点的流程线被高亮显示，但实际并没有进行驳回操作
                 */
                List<Map<String, Object>> tempMapList = new ArrayList<>();

                // 当前节点ID
                String currentActivityId = currentActivityInstance.getActivityId();

                int size = historicActivityInstanceList.size();
                boolean ifStartFind = false;
                boolean ifFinded = false;
                HistoricActivityInstance historicActivityInstance;

                // 循环当前节点的所有流出线
                // 循环所有的历史节点
//                logger.info("【开始】-匹配当前节点-ActivityId=【{}】需要高亮显示的流出线", currentActivityId);
//                logger.info("循环历史节点");

                for (int i = 0; i < size; i++) {
                    // // 如果当前节点流程线对应的下级节点在历史节点中，则该条流程线进行高亮显示（【问题】有驳回流程线时，即使没有进行驳回操作，因为申请节点在历史节点中，也会将驳回流程线高亮显示-_-||）
                    // if (historicActivityInstance.getActivityId().equals(sequenceFlow.getTargetRef())) {
                    // Map<String, Object> map = new HashMap<>();
                    // map.put("highLightedFlowId", sequenceFlow.getId());
                    // map.put("highLightedFlowStartTime", historicActivityInstance.getStartTime().getTime());
                    // tempMapList.add(map);
                    // // highFlows.add(sequenceFlow.getId());
                    // }

                    // 历史节点
                    historicActivityInstance = historicActivityInstanceList.get(i);
//                    logger.info("第【{}/{}】个历史节点-ActivityId=【{}】", i + 1, size, historicActivityInstance.getActivityId());

                    // 如果循环历史节点中的id等于当前节点id，从当前历史节点继续先后查找是否有当前流程线等于的节点
                    // 历史节点的序号需要大于等于已经完成历史节点的序号，放置驳回重审一个节点经过两次时只取第一次的流出线高亮显示，第二次的不显示
                    if (i >= k && historicActivityInstance.getActivityId().equals(currentActivityId)) {
//                        logger.info("第【{}】个历史节点和当前节点一致-ActivityId=【{}】", i + 1, historicActivityInstance.getActivityId());
                        ifStartFind = true;
                        // 跳过当前节点继续查找下一个节点
                        continue;
                    }
                    if (ifStartFind) {
//                        logger.info("[开始]-循环当前节点-ActivityId=【{}】的所有流出线", currentActivityId);

                        ifFinded = false;
                        for (SequenceFlow sequenceFlow : outgoingFlowList) {
                            // 如果当前节点流程线对应的下级节点在其后面的历史节点中，则该条流程线进行高亮显示
                            // 【问题】
//                            logger.info("当前流出线的下级节点=[{}]", sequenceFlow.getTargetRef());
                            if (historicActivityInstance.getActivityId().equals(sequenceFlow.getTargetRef())) {
//                                logger.info("当前节点[{}]需高亮显示的流出线=[{}]", currentActivityId, sequenceFlow.getId());
                                highFlows.add(sequenceFlow.getId());
                                // 暂时默认找到离当前节点最近的下一级节点即退出循环，否则有多条流出线时将全部被高亮显示
                                ifFinded = true;
                                break;
                            }
                        }
//                        logger.info("[完成]-循环当前节点-ActivityId=【{}】的所有流出线", currentActivityId);
                    }
                    if (ifFinded) {
                        // 暂时默认找到离当前节点最近的下一级节点即退出历史节点循环，否则有多条流出线时将全部被高亮显示
                        break;
                    }
                }
//                logger.info("【完成】-匹配当前节点-ActivityId=【{}】需要高亮显示的流出线", currentActivityId);
                // if (!CollectionUtils.isEmpty(tempMapList)) {
                // // 遍历匹配的集合，取得开始时间最早的一个
                // long earliestStamp = 0L;
                // String highLightedFlowId = null;
                // for (Map<String, Object> map : tempMapList) {
                // long highLightedFlowStartTime = Long.valueOf(map.get("highLightedFlowStartTime").toString());
                // if (earliestStamp == 0 || earliestStamp <= highLightedFlowStartTime) {
                // highLightedFlowId = map.get("highLightedFlowId").toString();
                // earliestStamp = highLightedFlowStartTime;
                // }
                // }
                // highFlows.add(highLightedFlowId);
                // }
            }
        }
        return highFlows;
    }

    /**
     * @param bpmnModel                    bpmnModel
     * @param historicActivityInstanceList historicActivityInstanceList
     * @return HighLightedFlows
     */
    public List<String> getHighLightedFlowsByIncomingFlows(BpmnModel bpmnModel,
                                                           List<HistoricActivityInstance> historicActivityInstanceList) {
        //historicActivityInstanceList 是 流程中已经执行的历史活动实例
        // 已经流经的顺序流，需要高亮显示
        List<String> highFlows = new ArrayList<>();

        // 全部活动节点(包括正在执行的和未执行的)
        List<FlowNode> allHistoricActivityNodeList = new ArrayList<>();

        /*
         * 循环的目的：
         *           获取所有的历史节点FlowNode并放入allHistoricActivityNodeList
         *           获取所有确定结束了的历史节点finishedActivityInstancesList
         */
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            // 获取流程节点
            // bpmnModel.getMainProcess()获取一个Process对象
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
            allHistoricActivityNodeList.add(flowNode);
        }

        FlowNode currentFlowNode;
        FlowNode targetFlowNode;
        HistoricActivityInstance currentActivityInstance;

        // 循环活动节点
        for (FlowNode flowNode : allHistoricActivityNodeList) {
            // 获取每个活动节点的输入线
            List<SequenceFlow> incomingFlows = flowNode.getIncomingFlows();

            // 循环输入线，如果输入线的源头处于全部活动节点中，则将其包含在内
            for (SequenceFlow sequenceFlow : incomingFlows) {
                if (allHistoricActivityNodeList.stream().map(BaseElement::getId).collect(Collectors.toList()).contains(sequenceFlow.getSourceFlowElement().getId())){
                    highFlows.add(sequenceFlow.getId());
                }
            }

        }

        return highFlows;
    }


    private List<String> getRunningActivityFlowsIds(BpmnModel bpmnModel, List<String> runningActivityIdList, List<HistoricActivityInstance> historicActivityInstanceList) {
        List<String> runningActivityFlowsIds = new ArrayList<>();
        List<String> runningActivityIds = new ArrayList<>(runningActivityIdList);
        // 逆序寻找，因为historicActivityInstanceList有序
        if (CollectionUtils.isEmpty(runningActivityIds)) {
            return runningActivityFlowsIds;
        }
        for (int i = historicActivityInstanceList.size() - 1; i >= 0; i--) {
            HistoricActivityInstance historicActivityInstance = historicActivityInstanceList.get(i);
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
            // 如果当前节点是未完成的节点
            if (runningActivityIds.contains(flowNode.getId())) {
                continue;
            }
            // 当前节点的所有流出线
            List<SequenceFlow> outgoingFlowList = flowNode.getOutgoingFlows();
            // 遍历所有的流出线
            for (SequenceFlow outgoingFlow : outgoingFlowList) {
                // 获取当前节点流程线对应的下一级节点
                FlowNode targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(outgoingFlow.getTargetRef(), true);
                // 如果找到流出线的目标是runningActivityIdList中的，那么添加后将其移除，避免找到重复的都指向runningActivityIdList的流出线
                if (runningActivityIds.contains(targetFlowNode.getId())) {
                    runningActivityFlowsIds.add(outgoingFlow.getId());
                    runningActivityIds.remove(targetFlowNode.getId());
                }
            }

        }
        return runningActivityFlowsIds;
    }

}
