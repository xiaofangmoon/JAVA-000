package com.xiaofang.rpc.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaofang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse {
    private String responseId;
    private String requestId;
    private Boolean success;
    private Object result;
    private Throwable throwable;
}
