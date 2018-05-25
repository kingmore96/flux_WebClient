package com.example.fluxwebclient.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 保存方法上的信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodInfo {

    /**
     * 方法上请求的url
     */
    private String url;

    /**
     * 请求的方法类型
     */
    private HttpMethod httpMethod;

    /**
     * 保存@PathVariable的信息，key就是@PathVariable("id) 括号里的值
     * value就是方法的参数对应的对象
     */
    private Map<String,Object> params;

    /**
     * 保存@RequestBody注解对应的Mono对象
     */
    private Mono requestBody;

    /**
     * 返回的是Flux还是Mono，Flux为true，Mono为false
     */
    private boolean returnFlux;

    /**
     * 返回的泛型类型
     */
    private Class returnParameterizedType;

    /**
     * RequestBody里面参数的泛型类型
     */
    private Class requestBodyParameterizedType;
}
