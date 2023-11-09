package org.example.enums;

import org.example.enums.base.BaseEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zzx
 */

public enum CodeEnum implements BaseEnum {
    // 通用部分:0-10
    SUCCESS(0, "成功"),
    ERROR(1, "失败"),
    VALIDATION_ERROR(2, "入参校验异常："),
    SYS_INIT_ERROR(3, "系统初始化异常"),
    UNSUPPORTED_OPERATION(4, "不支持的操作"),


    ////////////////////////////////////////////////////////////////////////////////////////////
    INTERFACE_CALL_ERROR(400, "接口调用失败"),
    AUTH_NO_AUTH_ERROR(401, "没有权限执行操作"),
    // JWT token错误码
    JWT_ERROR(403, "JWT错误"),


    ////////////////////////////////////////////////////////////////////////////////////////////
    // 特殊情况：
    UNKNOWN_ABNORMAL(9999, "未知异常"),
    NULL_POINTER_ERROR(9998, "发生空指针异常，请联系开发排查问题，错误详情："),

    ////////////////////////////////////////////////////////////////////////
    // 特殊异常
    IMAGE_CREATE_FAIL(1000, "未能创建流程图,具体错误:"),
    UNKNOWN_PROCESS_NAME(1001, "未知的流程名称或流程ID:"),
    PROCESS_XML_ERROR(1002, "文件不符合bpmn规范请重新上传"),
    INVALID_FILTER_TYPE(1003, "错误的流程过滤器类型[%s]"),
    MULTI_PROCESS_BY_NAME(1004, "根据给定的流程名称或流程ID[%s], 查找到多个流程定义"),
    UNABLE_START_PROCESS(1005, "无法启动流程[%s], 请检查流程权限与场景限制或联系管理员查看系统设置"),
    UNABLE_COMPLETE_TASK(1006, "无法完成事项[%s], 请检查用户流程权限与场景限制或联系管理员查看系统设置"),
    UNKNOWN_ACTION_CLASS(1007, "未找到相应的代理行为[%s], 请联系管理员检查流程定义"),
    UNKNOWN_MODIFIER_CLASS(1008, "未找到相应的修改流程中数据的方式[%s], 请联系管理员检查检查流程定义"),
    ACTION_OBJECT_NOT_FOUND_IN_SPRING_CONTAINER(1009, "没有在spring container中找到相应的代理行为对象[%s], 请联系开发人员"),
    MODIFIER_OBJECT_NOT_FOUND_IN_SPRING_CONTAINER(1010, "没有在spring container中找到相应的Modifier对象[%s], 请联系开发人员"),
    SPRING_EMPTY_CONTEXT(1011, "application context未注入, 请联系开发人员"),
    UNKNOWN_PROCESS_STARTABLE_FILTER(1012, "没有找到相应的ProcessStartableFilter[%s], 请联系开发人员"),
    UNKNOWN_TASK_COMPLETABLE_FILTER(1013, "没有找到相应的TaskCompletableFilter[%s], 请联系开发人员"),
    UNKNOWN_TASK_READABLE_FILTER(1014, "没有找到相应的TaskReadableFilter[%s], 请联系开发人员"),
    PROCESS_STARTABLE_FILTER_NOT_FOUND_IN_SPRING_CONTAINER(1015, "未在Spring container 中找到相应的ProcessStartableFilter对象[%s], 请联系开发人员"),
    TASK_COMPLETABLE_FILTER_NOT_FOUND_IN_SPRING_CONTAINER(1016, "未在Spring container 中找到相应的TaskCompletableFilter对象[%s], 请联系开发人员"),
    TASK_READABLE_FILTER_NOT_FOUND_IN_SPRING_CONTAINER(1017, "未在Spring container 中找到相应的TaskReadableFilter对象[%s], 请联系开发人员"),
    INVALID_TASK_TYPE(1018, "错误的任务类型[%s]"),
    INVALID_PROCESS_INSTANCE_STATUS(1019, "错误的流程实例状态[%s]"),
    INVALID_PROCESS_INSTANCE_USER_PERSPECTIVE(1020, "错误的流程实例查询类型[%s]"),
    TASK_NODE_EXIST_ERROR(1021, "编辑流程结点名称不允许重名，请修改后保存"),
    UNKNOWN_TASK_ACTION(1022, "没有找到相应的TaskAction[%s], 请联系开发人员"),
    TASK_ACTION_NOT_FOUND_IN_SPRING_CONTAINER(1023, "未在Spring container 中找到相应的TaskAction对象[%s], 请联系开发人员"),
    HISTORY_NODE_INFO(1025, "流程实例[%s]没有历史节点信息"),
    MULTI_TASKS(1026, "查找到多个活跃的任务, 请确认业务逻辑"),
    TASK_NOT_FOUND(1027, "该审批单已被处理，请刷新页面。"),
    APPROVE_GROUP_REPEAT(1028, "根据给定的名称或ID[%s], 查找到相同名称"),
    APPROVE_GROUP_NOT_FOUND(1029, "根据给定的名称或ID[%s], 未查找到"),
    PARAM_NOT_FOUND(1030, "请求参数不可为空,请重新请求"),
    UNABLE_COMPLETE_TASK_REQUEST(1031, "无法完成事项[%s], 流程未配置结束请求"),
    DELETE_GROUP_ERROR(1032, "未能删除审批组 -> 该审批组绑定的任务节点只指定该审批组"),
    AUTH_APPROVAL_GROUP(1033, "未找到审批组管理权限信息"),
    AUTH_APPROVAL_GROUP_INFO(1034, "未查找到审批组：[%s]的权限信息"),
    FILE_UPLOAD_ERROR(1035, "上传失败,请检测文件大小后重新上传(最大文件限制为100MB)"),
    FILE_NOT_FOUND(1036, "根据附件ID未找到附件信息"),
    FILE_DOWNLOAD_ERROR(1037, "文件下载失败,请联系管理员或重新下载"),
    BIND_TASK_ERROR(1038, "任务节点至少需要绑定一个审批组"),
    PROCESS_USED(1039, "流程[%s]仍有未结束流程实例,请全部结束后更新"),
    PROCESS_NODE_ERROR(1040, "流程节点至少包含一个复核节点,有且仅有一个录入节点与修改节点"),
    PROCESS_BPMN_CREATE(1041, "流程动态生成流程配置文件失败"),
    TASK_FIND_GROUP_ERROR(1042, "绑定任务节点前请确认审批组是否存在"),
    TASK_FIND_PROCESS_ERROR(1043, "根据给定的流程名[%s],未找到相应任务节点"),
    GROUP_FIND_ERROR(1044, "根据给定的流程名[%s],未找到相应审批组或关联信息"),
    PARAM_NOT_VALID(1045, "该参数无效, 具体信息为[%s]"),
    NODE_TYPE_ERROR(1046, "不支持该类型的节点"),
    BPMN_MODEL_NOT_FOUND(1047, "根据给定的processDefinitionId[%s], 没有找到相应的BPMN模型"),
    UNKNOWN_PROCESS_INSTANCE(1048, "该流程实例[%s]已经完成审批,请刷新后查看"),
    MULTI_PROCESS_INSTANCE_BYID(1049, "根据给定的流程实例ID[%s], 查找到多个流程实例"),
    MISSING_INPUT_TASK(1050, "缺少输入任务[%s]"),
    MISSING_MODIFY_TASK(1051, "缺少修改任务[%s]"),
    MULTI_INPUT_TASKS(1052, "找到多个输入任务[%s]"),
    MULTI_MODIFY_TASKS(1053, "找到多个修改任务[%s]"),
    MISSING_TASKS(1054, "该流程中没有任务节点[%s]"),
    MISSING_REVIEW_TASKS(1055, "该流程中没有审批任务节点[%s]"),
    INVALID_TASKS(1056, "自定义流程任务节点不合规，要求必须有且仅有一个录入类型节点，有且仅有一个更新类型节点，以及至少一个复核节点"),
    INVALID_CONFIG(1057, "无效的流程配置输入, 请检查请求参数"),
    INVALID_TASK_NODE(1058, "无效的自定义流程节点, 请检查请求参数"),
    FILE_UPLOAD_AGAIN_ERROR(1059, "重新上传失败,失败原因：[%s]"),
    INVALID_OPERATION_TYPE(1060, "错误的运算类型[%s]"),
    UNKNOWN_INDEX(1061, "没有找到相应的Index[%s], 请联系开发人员"),
    INDEX_NOT_FOUND_IN_SPRING_CONTAINER(1062, "未在Spring container 中找到相应的Index对象[%s], 请联系开发人员"),
    TRIGGER_ERROR(1063, "触发器或内部条件不存在"),
    PARAM_TYPE_ERROR(1064, "指标类型转换错误:[%s]"),
    TRIGGER_EXIST_ERROR(1065, "已存在同名触发器或内部条件"),
    TRIGGER_DELETE_ERROR(1066, "该触发器已经绑定了以下流程：%s,无法删除"),
    INDEX_DATA_ERROR(1067, "根据传入数据中未找到匹配数据,无法计算指标"),
    AUTH_PROCESS_DEFINITION(1068, "未找到流程定义权限信息"),
    AUTH_TRIGGER(1069, "未找到触发器管理权限信息"),
    AUTH_TRIGGER_INFO(1070, "未查找到触发器：[%s]的权限信息"),
    COUNTER_SIGN_GROUP_UPDATE_INFO(1071, "该审批组绑定的会签节点存在正在进行的任务,请结束后更新"),
    SETTLEMENT_UPDATE_ERROR(1072, "结算审批：持仓编号%s,生命周期已经改变，请重新发起审批"),
    AMEND_UPDATE_ERROR(1073, "交易要素修改审批：持仓编号%s,生命周期已经改变，请重新发起审批"),
    UNKNOWN_PROCESS_ERROR(1074, "部署的流程图错误请查看日志,更改后重新部署"),
    NOT_FOUND_PROCESS_ERROR(1075, "根据路径未找到流程图,请重新确认文件路径"),
    DEPLOY_PROCESS_ERROR(1076,"部署流程图失败"),

    ;

    private final int code;
    private final String description;

    /**
     * 构造方法
     *
     * @param code        code
     * @param description 描述信息
     */
    CodeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static String getMsgByCodeInt(int codeInt) {
        for (CodeEnum e : CodeEnum.values()) {
            if (e.getCode() == codeInt) {
                return e.description;
            }
        }
        throw new IllegalArgumentException("未定义的code码:" + codeInt);
    }

    private static final Map<Integer, CodeEnum> CODE_ENUM_MAP = new HashMap<>();

    static {
        for (CodeEnum a : CodeEnum.values()) {
            CODE_ENUM_MAP.put(a.getCode(), a);
        }
    }

    /**
     * 通过code获取CodeEnum
     *
     * @param code code
     * @return CodeEnum
     */
    public static CodeEnum getEnumByCode(Integer code) {
        return CODE_ENUM_MAP.get(code);
    }

}
