package com.xiaofangmoon.learn;

import java.lang.instrument.Instrumentation;

/**
 * @author xiaofang
 */
public class PreMainTraceAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("agentArgs : " + agentArgs);
        inst.addTransformer(new DefineTransformer(), true);
    }

}
