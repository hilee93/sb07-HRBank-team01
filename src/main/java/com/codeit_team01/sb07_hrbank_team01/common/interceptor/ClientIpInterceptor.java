package com.codeit_team01.sb07_hrbank_team01.common.interceptor; // 실제 패키지명으로 변경하세요

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ClientIpInterceptor implements HandlerInterceptor {

  private static final Logger log = LoggerFactory.getLogger(ClientIpInterceptor.class);

  // 인터셉터에서 request 처리 전에 실행되는 메소드
  @Override
  public boolean preHandle(HttpServletRequest request,
      HttpServletResponse response,
      Object handler) throws Exception {

    String clientIp = extractClientIp(request);

    log.debug("[Interceptor] Client IP Extracted: {}", clientIp);

    // 추출된 IP를 "clientIp"라는 이름으로 request attribute에 저장
    request.setAttribute("clientIp", clientIp);

    // true를 반환하여 요청 처리(컨트롤러)를 계속 진행하도록 허용
    return true;
  }

  /**
   * 클라이언트의 실제 IP 주소를 추출합니다.
   * 로드 밸런서나 프록시 환경을 고려하여 X-Forwarded-For 헤더를 우선 확인합니다.
   */
  private String extractClientIp(HttpServletRequest request) {
    // 1. X-Forwarded-For 헤더 확인 (프록시/로드 밸런서 환경)
    String xffHeader = request.getHeader("X-Forwarded-For");
    if (xffHeader != null && !xffHeader.isBlank() && !"unknown".equalsIgnoreCase(xffHeader)) {
      // 여러 IP가 콤마로 구분되어 있을 경우, 가장 앞의 IP가 실제 클라이언트 IP임
      return xffHeader.split(",")[0].trim();
    }

    // 2. 다른 일반적인 프록시 헤더 확인 (선택 사항)
    String ip = request.getHeader("Proxy-Client-IP");
    if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }

    // 3. 위의 모든 헤더가 없을 경우 (로컬 환경 또는 직접 접근 시)
    if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }

    return ip;
  }

  // postHandle 및 afterCompletion은 여기서는 구현하지 않았습니다. (필요 시 추가)
}