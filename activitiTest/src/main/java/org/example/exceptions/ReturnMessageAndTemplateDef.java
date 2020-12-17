package org.example.exceptions;

public interface ReturnMessageAndTemplateDef {
    enum Errors{

        UNKNOWN_ERROR(                                      "00000001", "未知错误", false),
        INVALID_FILTER_TYPE(                                "00000002", "错误的流程过滤器类型[%s]", true),
        UNKNOWN_PROCESS_NAME(                               "00000003", "未知的流程名称或流程ID[%s]", true),
        MULTI_PROCESS_BYNAME(                               "00000004", "根据给定的流程名称或流程ID[%s], 查找到多个流程定义", true),
        UNABLE_START_PROCESS(                               "00000005", "无法启动流程[%s], 请检查流程权限与场景限制或联系管理员查看系统设置", true),
        UNABLE_COMPLETE_TASK(                               "00000006", "无法完成事项[%s], 请检查用户流程权限与场景限制或联系管理员查看系统设置", true),
        UNKNOWN_ACTION_CLASS(                               "00000007", "未找到相应的代理行为[%s], 请联系管理员检查流程定义", true),
        UNKNOWN_MODIFIER_CLASS(                             "00000008", "未找到相应的修改流程中数据的方式[%s], 请联系管理员检查检查流程定义", true),
        ACTION_OBJECT_NOT_FOUND_IN_SPRING_CONTAINER(        "00000009", "没有在spring container中找到相应的代理行为对象[%s], 请联系开发人员", true),
        MODIFIER_OBJECT_NOT_FOUND_IN_SPRING_CONTAINER(      "000000010", "没有在spring container中找到相应的Modifier对象[%s], 请联系开发人员", true),
        SPRING_EMPTY_CONTEXT(                               "000000011", "application context未注入, 请联系开发人员", false),

        UNKNOWN_PROCESS_STARTABLE_FILTER(                   "000000012", "没有找到相应的ProcessStartableFilter[%s], 请联系开发人员", true),
        UNKNOWN_TASK_COMPLETABLE_FILTER(                    "000000013", "没有找到相应的ProcessStartableFilter[%s], 请联系开发人员", true),
        UNKNOWN_TASK_READABLE_FILTER(                       "000000014", "没有找到相应的ProcessStartableFilter[%s], 请联系开发人员", true),

        PROCESS_STARTABLE_FILTER_NOT_FOUND_IN_SPRING_CONTAINER( "000000015", "未在Spring container 中找到相应的ProcessStartableFilter对象[%s], 请联系开发人员", true),
        TASK_COMPLETABLE_FILTER_NOT_FOUND_IN_SPRING_CONTAINER(  "000000016", "未在Spring container 中找到相应的TaskCompletableFilter对象[%s], 请联系开发人员", true),
        TASK_READABLE_FILTER_NOT_FOUND_IN_SPRING_CONTAINER(     "000000017", "未在Spring container 中找到相应的TaskReadableFilter对象[%s], 请联系开发人员", true),

        INVALID_TASK_TYPE(                                "000000018", "错误的任务类型[%s]", true),
        INVALID_PROCESS_INSTANCE_STATUS(                  "000000019", "错误的流程实例状态[%s]", true),
        INVALID_PROCESS_INSTANCE_USER_PERSPECTIVE(        "000000020", "错误的流程实例查询类型[%s]", true),

        UNKNOWN_TASK_ACTION(                              "000000022", "没有找到相应的TaskAction[%s], 请联系开发人员", true),
        TASK_ACTION_NOT_FOUND_IN_SPRING_CONTAINER(        "000000023", "未在Spring container 中找到相应的TaskAction对象[%s], 请联系开发人员", true),
        IMAGE_CREATE_FAIL(                                "000000024", "未能创建流程图,具体错误[%s]", true),
        HISTORY_NODE_INFO(                                "000000025", "流程实例[%s]没有历史节点信息", true),

        MULTI_TASKS(                                      "000000026", "查找到多个活跃的任务, 请确认业务逻辑", false),
        TASK_NOT_FOUND(                                   "000000027", "该审批单已被处理，请刷新页面。", false),
        APPROVE_GROUP_REPEAT(                             "000000028", "根据给定的名称或ID[%s], 查找到相同名称", true),
        APPROVE_GROUP_NOT_FOUND(                          "000000029", "根据给定的名称或ID[%s], 未查找到", true),
        PARAM_NOT_FOUND(                                  "000000030", "请求参数不可为空,请重新请求", false),
        UNABLE_COMPLETE_TASK_REQUEST(                     "000000031", "无法完成事项[%s], 流程未配置结束请求", true),
        DELETE_GROUP_ERROR(                               "000000032", "未能删除审批组 -> 该审批组绑定的任务节点只指定该审批组", false),
        AUTH_APPROVAL_GROUP(                              "000000033", "未找到审批组管理权限信息", false),
        AUTH_APPROVAL_GROUP_INFO(                         "000000034", "未查找到审批组：[%s]的权限信息", true),
        FILE_UPLOAD_ERROR(                                "000000035", "上传失败,请检测文件大小后重新上传(最大文件限制为100MB)", false),
        FILE_NOT_FOUND(                                   "000000036", "根据附件ID未找到附件信息", false),
        FILE_DOWNLOAD_ERROR(                              "000000037", "文件下载失败,请联系管理员或重新下载", false),
        BIND_TASK_ERROR(                                  "000000038", "任务节点至少需要绑定一个审批组", false),
        PROCESS_USED(                                     "000000039", "流程[%s]仍有未结束流程实例,请全部结束后更新", true),
        PROCESS_NODE_ERROR(                               "000000040", "流程节点至少包含一个复核节点,有且仅有一个录入节点与修改节点", false),
        PROCESS_BPMN_CREATE(                              "000000041", "流程动态生成流程配置文件失败", false),
        TASK_FIND_GROUP_ERROR(                            "000000042", "绑定任务节点前请确认审批组是否存在", false),
        TASK_FIND_PROCESS_ERROR(                          "000000043", "根据给定的流程名[%s],未找到相应任务节点", true),
        GROUP_FIND_ERROR(                                 "000000044", "根据给定的流程名[%s],未找到相应审批组或关联信息", true),
        PARAM_NOT_VALID(                                  "000000045", "该参数无效, 具体信息为[%s]", true),
        NODE_TYPE_ERROR(                                  "000000046", "不支持该类型的节点", false),
        BPMN_MODEL_NOT_FOUND(                             "000000047", "根据给定的processDefinitionId[%s], 没有找到相应的BPMN模型", true),
        UNKNOWN_PROCESS_INSTANCE(                         "000000048", "该流程实例[%s]已经完成审批,请刷新后查看", true),
        MULTI_PROCESS_INSTANCE_BYID(                      "000000049", "根据给定的流程实例ID[%s], 查找到多个流程实例", true),
        MISSING_INPUT_TASK(                               "000000050", "缺少输入任务[%s]", true),
        MISSING_MODIFY_TASK(                              "000000051", "缺少修改任务[%s]", true),
        MULTI_INPUT_TASKS(                                "000000052", "找到多个输入任务[%s]", true),
        MULTI_MODIFY_TASKS(                               "000000053", "找到多个修改任务[%s]", true),
        MISSING_TASKS(                                    "000000054", "该流程中没有任务节点[%s]", true),
        MISSING_REVIEW_TASKS(                             "000000055", "该流程中没有审批任务节点[%s]", true),
        INVALID_TASKS(                                    "000000056", "自定义流程任务节点不合规，要求必须有且仅有一个录入类型节点，有且仅有一个更新类型节点，以及至少一个复核节点", false),
        INVALID_CONFIG(                                   "000000057", "无效的流程配置输入, 请检查请求参数", false),
        INVALID_TASK_NODE(                                "000000058", "无效的自定义流程节点, 请检查请求参数", false),
        FILE_UPLOAD_AGAIN_ERROR(                          "000000059", "重新上传失败,失败原因：[%s]", true),
        INVALID_OPERATION_TYPE(                           "000000060", "错误的运算类型[%s]", true),
        UNKNOWN_INDEX(                                    "000000061", "没有找到相应的Index[%s], 请联系开发人员", true),
        INDEX_NOT_FOUND_IN_SPRING_CONTAINER(              "000000062", "未在Spring container 中找到相应的Index对象[%s], 请联系开发人员", true),
        TRIGGER_ERROR(                                    "000000063", "触发器或内部条件不存在", false),
        PARAM_TYPE_ERROR(                                 "000000064", "指标类型转换错误:[%s]", true),
        TRIGGER_EXIST_ERROR(                              "000000065", "已存在同名触发器或内部条件", false),
        TRIGGER_DELETE_ERROR(                             "000000066", "该触发器已经绑定了以下流程：%s,无法删除", true),
        INDEX_DATA_ERROR(                                 "000000067", "根据传入数据中未找到匹配数据,无法计算指标", false),
        AUTH_PROCESS_DEFINITION(                          "000000068", "未找到流程定义权限信息", false),
        AUTH_TRIGGER(                                     "000000069", "未找到触发器管理权限信息", false),
        AUTH_TRIGGER_INFO(                                "000000070", "未查找到触发器：[%s]的权限信息", true),
        COUNTER_SIGN_GROUP_UPDATE_INFO(                   "000000071", "该审批组绑定的会签节点存在正在进行的任务,请结束后更新", true),
        SETTLEMENT_UPDATE_ERROR(                          "000000072", "结算审批：持仓编号%s,生命周期已经改变，请重新发起审批", true),
        Amend_UPDATE_ERROR(                               "000000073", "交易要素修改审批：持仓编号%s,生命周期已经改变，请重新发起审批", true),
        UNKNOWN_PROCESS_ERROR(                            "000000074", "部署的流程图错误请查看日志,更改后重新部署", false),
        NOT_FOUND_PROCESS_ERROR(                          "000000075", "根据路径未找到流程图,请重新确认文件路径", false),
        PROCESS_XML_ERROR(                                "000000076", "文件不符合bpmn规范请重新上传", false),
        TASK_NODE_EXIST_ERROR(                            "000000077", "编辑流程结点名称不允许重名，请修改后保存",false),
        ;


        private String detailedErrorCode;
        private String message;
        private Boolean isTemplate;

        Errors(String detailedErrorCode, String message, Boolean isTemplate){
            this.detailedErrorCode = detailedErrorCode;
            this.message = message;
            this.isTemplate = isTemplate;
        }

        public String getDetailedErrorCode(){
            return detailedErrorCode;
        }

        public String getMessage(Object... params){
            return isTemplate
                    ? String.format(message, params)
                    : message;
        }
    }
}
