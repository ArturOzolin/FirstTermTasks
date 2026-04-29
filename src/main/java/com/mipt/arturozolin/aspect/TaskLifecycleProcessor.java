package com.mipt.arturozolin.aspect;

import com.mipt.arturozolin.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import com.mipt.arturozolin.service.TaskService;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Процессор жизненного цикла бинов.
 * Логирует этапы инициализации для сервисов и репозиториев задач.
 */
@Slf4j
@Component
public class TaskLifecycleProcessor implements BeanPostProcessor {

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof TaskService || bean instanceof TaskRepository) {
      log.info("[BeanPostProcessor] Before Init: {}", beanName);
    }
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof TaskService || bean instanceof TaskRepository) {
      log.info("[BeanPostProcessor] After Init: {}", beanName);
    }
    return bean;
  }
}