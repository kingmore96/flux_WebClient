package com.example.fluxwebclient.proxys;

import com.example.fluxwebclient.annotation.ApiServer;
import com.example.fluxwebclient.beans.MethodInfo;
import com.example.fluxwebclient.beans.ServerInfo;
import com.example.fluxwebclient.interfaces.ProxyCreator;
import com.example.fluxwebclient.interfaces.RestHandler;
import com.example.fluxwebclient.resthandlers.WebClientRestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 使用Jdk动态代理创建实现类
 */
@Slf4j
public class JDKProxyCreatorImpl implements ProxyCreator {

    @Override
    public Object createProxy(Class<?> type) {
        //提取ApiServer注解上的信息
        ServerInfo serverInfo = extractServerInfo(type);
        log.info("提取serverInfo:{}",serverInfo);

        // 给每一个代理类一个实现
        RestHandler restHandler = new WebClientRestHandler();

        //初始化client的server信息
        restHandler.init(serverInfo);

        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{type}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //提取方法上的信息
                MethodInfo methodInfo = extractMethodInfo(method,args);
                log.info("提取methodInfo:{}",methodInfo);

                //调用RestHandler的invokeRest方法完成调用rest服务
                Object object = restHandler.invokeRest(methodInfo);
                return object;
            }


            /**
             * 提取方法信息
             * @param method
             * @param args
             * @return
             */
            private MethodInfo extractMethodInfo(Method method, Object[] args) {
                MethodInfo methodInfo = new MethodInfo();
                //提取方法上注解对应的信息
                methodInfo = extractMethodAnnotation(method,methodInfo);
                //提取方法上的参数@PathVariable 和 @RequestBody
                methodInfo = extractMethodParameters(method,args,methodInfo);
                //提取方法的返回信息 是返回Flux还是Mono 以及泛型类型
                methodInfo = extractMethodReturn(method,methodInfo);
                return methodInfo;
            }

            /**
             * 提取返回相关的信息
             * @param method
             * @param methodInfo
             * @return
             */
            private MethodInfo extractMethodReturn(Method method, MethodInfo methodInfo) {
                //提取返回类型
                methodInfo = extractMethodReturnType(method,methodInfo);
                //提取返回类型的泛型
                methodInfo = extractMethodReturnParameterizedType(method,methodInfo);
                return methodInfo;
            }

            /**
             * 提取返回值上的泛型类型
             * @param method
             * @param methodInfo
             * @return
             */
            private MethodInfo extractMethodReturnParameterizedType(Method method, MethodInfo methodInfo) {
                Type genericReturnType = method.getGenericReturnType();
                Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
                methodInfo.setReturnParameterizedType((Class) actualTypeArguments[0]);
                return methodInfo;
            }

            /**
             * 提取返回类型，是否是Flux
             * @param method
             * @param methodInfo
             * @return
             */
            private MethodInfo extractMethodReturnType(Method method, MethodInfo methodInfo) {
                Class<?> returnType = method.getReturnType();
                //如果是Flux
                if(returnType.isAssignableFrom(Flux.class)){
                    methodInfo.setReturnFlux(true);
                }
                else if(returnType.isAssignableFrom(Mono.class)){
                    methodInfo.setReturnFlux(false);
                }
                return methodInfo;
            }


            /**
             * 提取方法上注解对应的信息
             * @param method
             * @param methodInfo
             * @return
             */
            private MethodInfo extractMethodAnnotation(Method method, MethodInfo methodInfo) {
                for (Annotation annotation : method.getAnnotations()) {
                    //四种请求
                    if(annotation instanceof GetMapping){
                        methodInfo.setHttpMethod(HttpMethod.GET);
                        methodInfo.setUrl(((GetMapping) annotation).value()[0]);
                    }else if (annotation instanceof PostMapping){
                        methodInfo.setHttpMethod(HttpMethod.POST);
                        methodInfo.setUrl(((PostMapping) annotation).value()[0]);
                    }else if(annotation instanceof DeleteMapping){
                        methodInfo.setHttpMethod(HttpMethod.DELETE);
                        methodInfo.setUrl(((DeleteMapping) annotation).value()[0]);
                    }else if(annotation instanceof PutMapping){
                        methodInfo.setHttpMethod(HttpMethod.PUT);
                        methodInfo.setUrl(((PutMapping) annotation).value()[0]);
                    }
                }
                return methodInfo;
            }

            /**
             * 提取方法上的参数@PathVariable 和 @RequestBody
             * @param method
             * @param args
             * @param methodInfo
             * @return
             */
            private MethodInfo extractMethodParameters(Method method, Object[] args, MethodInfo methodInfo) {
                Map<String,Object> map = new LinkedHashMap<>();
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    //判断PathVariable
                    PathVariable annoPath = parameters[i].getAnnotation(PathVariable.class);
                    if(annoPath != null){
                        map.put(annoPath.value(),args[i]);
                        methodInfo.setParams(map);
                    }

                    //判断RequestBody
                    RequestBody annoRequ = parameters[i].getAnnotation(RequestBody.class);
                    if(annoRequ!= null){
                        methodInfo.setRequestBody((Mono) args[i]);
                        //获取参数泛型
                        Type[] genericParameterTypes = method.getGenericParameterTypes();
                        Type[] actualTypeArguments = ((ParameterizedType) genericParameterTypes[0]).getActualTypeArguments();
                        methodInfo.setRequestBodyParameterizedType((Class) actualTypeArguments[0]);
                    }
                }
                return methodInfo;
            }
        });
    }

    /**
     * 提取ApiServer注解的value信息，也就是url
     * @param type
     * @return
     */
    private ServerInfo extractServerInfo(Class<?> type) {
        ServerInfo serverInfo = new ServerInfo();
        ApiServer annotation = type.getAnnotation(ApiServer.class);
        serverInfo.setUrl(annotation.value());
        return serverInfo;
    }
}
