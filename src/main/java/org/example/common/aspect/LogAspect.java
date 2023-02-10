package org.example.common.aspect;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.enumeration.CodeEnum;
import org.example.exceptions.BusinessException;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * 日志切面<br/>
     * 注意：<br/>
     * ①写在interface中的注解，此处无法解析到，所以Operation写在controller中，不写在Facade中；
     * ②GetMapping、PostMapping用于将其他日志获取，如swagger、getHealth；
     */
    @Pointcut(value = "(@annotation(io.swagger.v3.oas.annotations.Operation)) || " +
            " (@annotation(org.springframework.web.bind.annotation.GetMapping)) ||" +
            " (@annotation(org.springframework.web.bind.annotation.PostMapping))")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        MDC.put("traceId", UUID.randomUUID().toString(true));
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Operation operation = method.getAnnotation(Operation.class);
        log.info("「接口请求」:「正常开始」—— 请求方式：{} | 请求地址：{} | 请求描述：{} | 请求参数：{}", request.getMethod(), request.getRequestURL().toString(), ObjectUtil.isNull(operation) ? "暂无" : operation.summary(), Arrays.toString(joinPoint.getArgs()));
        try {
            StopWatch stopWatch = new StopWatch(request.getRequestURI());
            stopWatch.start();
            Object proceed = joinPoint.proceed();
            stopWatch.stop();
            log.info("「接口请求」:「正常结束」—— 请求耗时：{}ms", stopWatch.getTotalTimeMillis());
            return proceed;
        } catch (BusinessException e) {
            log.error("「接口请求」:「失败结束」—— 返回内容：{} ", e.getLocalizedMessage());
            throw e;
        } catch (Exception e) {
            log.error("「接口请求」:「失败结束」—— 返回内容：{} ", e.getLocalizedMessage());
            throw new BusinessException(CodeEnum.INTERFACE_CALL_ERROR, e.getLocalizedMessage());
        } finally {
            // 清除MDC中的traceId
            MDC.clear();
            // 请求结束后清除线程变量中的用户信息
            // delete user info
        }
    }

}
