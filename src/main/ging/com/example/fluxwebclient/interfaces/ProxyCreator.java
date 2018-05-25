package com.example.fluxwebclient.interfaces;

/**
 * 生成代理对象的接口，方便修改实现，比如：jdk动态代理修改为cglib
 */
public interface ProxyCreator {

    /**
     * 生成代理对象
     * @param type
     * @return
     */
    Object createProxy(Class<?> type);
}
