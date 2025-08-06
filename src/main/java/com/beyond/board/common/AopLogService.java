package com.beyond.board.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Aspect     // aop 관련 코드임을 명시
@Component
@Slf4j
public class AopLogService {
    // 인터셉터, aop(공통 작업의)의 대상이 되는 controller, service 등의 위치를 명시
    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controllerPointCut() {

    }

    // controller에 진입하기 전에 사용자 요청을 log 출력, 핵심 로직 수행 후 log 출력
    // 방법 1. around 어노테이션을 통해 controller에 걸쳐 있는 코드 패턴 사용
//    @Around("controllerPointCut()")
    // joinPoint는 사용자가 실행하려고 하는 코드를 의미하고, 위에서 정의한 pointcut을 의미
//    public Object controllerLogger(ProceedingJoinPoint joinPoint) throws Throwable {
//        log.info("aop start");
//
//        // 사용 한 메서드명 출력
//        log.info("method명: " + joinPoint.getSignature().getName());
//
//        // 직접 Http ServletRequest 객체에서 사용자 요청정보 추출
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        log.info("HTTP 메서드: " + request.getMethod());
//
//        Map<String, String[]> parmeterMap = request.getParameterMap();
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode objectNode = objectMapper.valueToTree(parmeterMap);
//        log.info("사용자 input 값: " + objectNode);
//
//        // 대상이 되는 controller 로직 수행
//        Object object = joinPoint.proceed();
//
//        log.info("aop end");
//        return object;
//    }

    // 방법 2. Before, After 어노테이션 사용
    @Before("controllerPointCut()")
    public void beforeController(JoinPoint joinPoint) {
        log.info("aop start");
        log.info("method명: " + joinPoint.getSignature().getName());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("HTTP 메서드: " + request.getMethod());
        Map<String, String[]> parmeterMap = request.getParameterMap();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.valueToTree(parmeterMap);
        log.info("사용자 input 값: " + objectNode);
    }

    @After("controllerPointCut()")
    public void afterController() {
        log.info("aop end");
    }
}
