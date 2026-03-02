package com.mipt.arturozolin.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Аспект для логирования времени выполнения и результатов работы методов сервисного слоя.
 * Реализует AOP.
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {

  @Around("execution(* com.mipt.arturozolin.service.*.*(..))")
  public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    log.info("[AOP] Start method: {}", methodName);

    long start = System.currentTimeMillis();
    try {
      Object result = joinPoint.proceed();
      long time = System.currentTimeMillis() - start;
      log.info("[AOP] End method: {}. Execution time: {} ms. Result: {}", methodName, time, result);
      return result;
    } catch (Exception e) {
      log.error("[AOP] Method {} failed with exception: {}", methodName, e.getMessage());
      throw e;
    }
  }
}