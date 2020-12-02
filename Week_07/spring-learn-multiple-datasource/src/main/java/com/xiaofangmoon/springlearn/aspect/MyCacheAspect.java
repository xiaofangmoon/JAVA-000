package com.xiaofangmoon.springlearn.aspect;

import com.xiaofangmoon.springlearn.entity.CacheInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//定义日志记录切面
@Aspect
@Component
@SuppressWarnings("all")
public class MyCacheAspect {

    private static final Logger logger = LoggerFactory.getLogger(MyCacheAspect.class);

    static Map<Method, CacheInfo> map = new HashMap<>();

    @Pointcut("@annotation(com.xiaofangmoon.springlearn.aspect.MyCache)")
    public void myCacheAspect() {
    }

    @Before("myCacheAspect()")
    public void doBefore(JoinPoint joinPoint) {
        logger.info("myCacheAspect-before");
    }

    @After("myCacheAspect()")
    public void doAfter(JoinPoint point) {
        logger.info("myCacheAspect-after");
        String methodName = point.getSignature().getName();
        List<Object> args = Arrays.asList(point.getArgs());
    }

    @AfterThrowing(pointcut = "myCacheAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        logger.error("执行错误:{}", e);
    }

    @Around("myCacheAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        logger.info("****myCacheAspect-round-start");
        //执行方法
        Method method = getMethod(joinPoint);
        MyCache annotation = method.getAnnotation(MyCache.class);
        int value = annotation.value();
        logger.info("缓存时间={}", value);
        Object result;
        if (map.get(method) == null || System.currentTimeMillis() - map.get(method).getTimestamp() > value * 1000) {
            logger.info("缓存失效，获取新值");
            result = joinPoint.proceed();
            map.put(method, new CacheInfo(System.currentTimeMillis(), result));
        } else {
            logger.info("返回缓存");
            result = map.get(method).getData();
        }
        logger.info("****myCacheAspect-round-end");
        return result;
    }

    public Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        MethodSignature msig = null;
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) signature;
        Object target = joinPoint.getTarget();

        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        return currentMethod;
    }
}
