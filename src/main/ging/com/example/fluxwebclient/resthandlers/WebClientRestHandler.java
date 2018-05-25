package com.example.fluxwebclient.resthandlers;

import com.example.fluxwebclient.beans.MethodInfo;
import com.example.fluxwebclient.beans.ServerInfo;
import com.example.fluxwebclient.interfaces.RestHandler;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.xml.ws.WebEndpoint;

/**
 * 使用WebClient方式调用服务
 */
public class WebClientRestHandler implements RestHandler {

    private WebClient webClient;

    private WebClient.RequestBodySpec request;

    @Override
    public void init(ServerInfo serverInfo) {
        //创建webclient
        this.webClient = WebClient.create(serverInfo.getUrl());
    }

    @Override
    public Object invokeRest(MethodInfo methodInfo) {
        Object result = null;
        WebClient.RequestBodySpec requestBodySpec = null;
        WebClient.ResponseSpec responseSpec = null;


        WebClient.RequestBodyUriSpec requestBodyUriSpec = webClient.method(methodInfo.getHttpMethod());

        //处理有无参数的情况
        if(methodInfo.getParams() == null){
            requestBodySpec = requestBodyUriSpec.uri(methodInfo.getUrl());
        }
        else{
            requestBodySpec = requestBodyUriSpec.uri(methodInfo.getUrl(),methodInfo.getParams());
        }

        requestBodySpec = requestBodySpec.contentType(MediaType.APPLICATION_JSON);
        //加入判断是否有body，有body的需要加body这步，没有body的直接retrieve
        if(methodInfo.getRequestBody()!= null){
            responseSpec =requestBodySpec.body(methodInfo.getRequestBody(),methodInfo.getRequestBodyParameterizedType())
                    .retrieve();
        }else{
            responseSpec = requestBodySpec.retrieve();
        }


//        responseSpec.onStatus(httpStatus -> httpStatus.value() == 404,response -> Mono.just(new RuntimeException("404错误")));
//        responseSpec.onStatus(httpStatus -> httpStatus.value() == 400,response -> Mono.just(new RuntimeException("400错误")));



        //这里需要补充是返回的Mono还是Flux，并且返回的泛型类;
        if(methodInfo.isReturnFlux()){
            result = responseSpec.bodyToFlux(methodInfo.getReturnParameterizedType());
        }else{
            result = responseSpec.bodyToMono(methodInfo.getReturnParameterizedType());
        }
        return result;
    }



//    /**
//     * 处理rest请求
//     */
//    @Override
//    public Object invokeRest(ServerInfo serverInfo,MethodInfo methodInfo) {
//        // 返回结果
//        Object result = null;
//
//        client = WebClient.create(serverInfo.getUrl());
//
//        request = this.client
//                // 请求方法
//                .method(methodInfo.getHttpMethod())
//                // 请求url 和 参数
//                .uri(methodInfo.getUrl())
//                //
//                .accept(MediaType.APPLICATION_JSON);
//
//        WebClient.ResponseSpec retrieve = null;
//
//        // 判断是否带了body
//        if (methodInfo.getRequestBody() != null) {
//            // 发出请求
//            retrieve = request
//                    .body(methodInfo.getRequestBody(), methodInfo.getRequestBodyParameterizedType())
//                    .retrieve();
//        } else {
//            retrieve = request.retrieve();
//        }
//
//        // 处理异常
//        retrieve.onStatus(status -> status.value() == 404,
//                response -> Mono.just(new RuntimeException("Not Found")));
//
//        // 处理body
//        if (methodInfo.isReturnFlux()) {
//            result = retrieve.bodyToFlux(methodInfo.getReturnParameterizedType());
//        } else {
//            result = retrieve.bodyToMono(methodInfo.getReturnParameterizedType());
//        }
//
//        return result;
//    }
}
