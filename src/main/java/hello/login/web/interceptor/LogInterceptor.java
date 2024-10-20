package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
//    스프링의 인터셉터를 사용하려면 HandlerInterceptor 인터페이스를 구현하면 된다.
    public static final String LOG_ID = "logId";
//    여기 자리에 uuid를 맴버변수로 만들면 큰일난다. LogInterceptor가 싱글톤으로 만들어지기 때문에 다른 트랜잭션이 오면 uuid가 바뀐다.
//    그래서 올바른 uuid를 ( REQUEST [{}][{}][{}] uuid ) 이 부분의 코드에 못넣는다.


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//     스프링 인터셉터는 인자로 HttpServletRequest가 들어온다.(여기서 부터 편하다)
//     인자로 Object handler 가 있다. 어떤 컨트롤러가 호출되는지 핸들러가 넘어온다.

        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        request.setAttribute(LOG_ID, uuid);

//     dispatcher 에서 요청URI에 따른 반환하는 핸들러(컨트롤러)의 데이터타입은 Object타입이다. SpringMVC는 Object handler와 같이
//     인자로 handler을 아무거나 들어 올 수 있도록 유연하게 만들어 놓았다. 그래서 핸들러 어댑터에 넣고 처리할 수 있는 것이다.
//     핸들러 어댑터가 대신 처리해 주기 때문에 handler에는 아무거나 다 들어갈 수 있다.

//     들어온 컨트롤러(Object handler)의 클래스 레벨에 @Controller 가 있거나 @RequestMapping 가 있으면 HandlerMethod라는
//     핸들러 어댑터가 해당 핸들러(클래스 레벨에 @Controller @RequestMapping 가 있음)에 적용이 된다.

        if(handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod)handler; // 호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
            // hm.getXXXX 이렇게 해서 들어온 핸들러(컨트롤러)에 대한 정보를 뽑을 수 있다.
        }

        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
        return true;
        // return true; 하면 뒤에 있는 컨트롤러가 호출된다. 핸들러 어댑터가 호출되고 핸들러가 실제로 호출된다.
        // return false; 하면 뒤에 있는 컨트롤러를 호출하지 않고 여기서 끝난다.
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandler [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String)request.getAttribute(LOG_ID);
        log.info("RESPONSE [{}][{}]", logId, requestURI);
        if(ex != null){
            log.error("afterCompletion error!!", ex); // 예외 같은 경우에는 {} 가 없어도 출력 된다.
        }
    }
}
