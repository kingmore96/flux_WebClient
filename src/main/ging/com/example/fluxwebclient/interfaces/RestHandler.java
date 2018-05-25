package com.example.fluxwebclient.interfaces;

import com.example.fluxwebclient.beans.MethodInfo;
import com.example.fluxwebclient.beans.ServerInfo;

public interface RestHandler {

    /**
     * 初始化服务器信息
     *
     * @param serverInfo
     */
    void init(ServerInfo serverInfo);

    /**
     * 调用Rest服务
     * @param serverInfo
     * @param methodInfo
     * @return
     */
    Object invokeRest(MethodInfo methodInfo);
}
