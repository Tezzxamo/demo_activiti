package org.example.controller.global;

import org.example.common.factory.RFactory;
import org.example.model.base.R;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * 统一封装请求返回体
 */
@RestControllerAdvice(basePackages = "org.example.controller")
public class ControllerResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 这个对象的类型是否是 R.class
        boolean typeIsR = returnType.getParameterType().isAssignableFrom(R.class);
        // 必须对象类型不是 R.class
        return !typeIsR;
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 如果数据data是String类型，就会触发另一个String的beforeBodyWrite，所以如果这个地方返回的是对象，String的beforeBodyWrite就会触发强转，导致报错
        // 所以当前办法是，String类型的数据，要么不要统一包装，要么在接口处手动调用ReturnMsgFactory.newSuccess(data)即可。
        // ————————————————— //
        // 包装数据统一返回
        // String数据不包装，直接返回
        if (data instanceof String) {
            return data;
        }
        return RFactory.newSuccess(data);
    }
}
