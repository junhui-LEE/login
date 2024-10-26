package hello.login.web.interceptor;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
//    생각해보면 로그인 체크 인터셉터는 preHandle(), postHandle(), afterCompletion() 중에서 preHandle();만 있으면 된다.
//    HandlerInterceptor 인터페이스의 postHandle(), afterCompletion() 의 접근제어자가 default 인데
//    default는 오버라이딩 안해도 된다. default는 기본 구현이 인터페이스 안에서 되어 있다. JAVA 8 부터 default가 도입되었다.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        log.info("인증 체크 인터셉터 실행 {}", requestURI);
        HttpSession session = request.getSession(false);

//        로그인을 한 사용자는 값이 있고 로그인을 안한 사용자는 session값이 null 이기 때문에
//        session 이 null 이면 "로그인 하고 와!" 라는 로직이 있어야 한다.
        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){
            log.info("미인증 사용자 요청");
            // 로그인으로 redirect
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false; // 여기서 끝내버리는 것이다. 더 이상 진행되지 않는다. 더 이상 뒤에 핸들러어댑터나 컨트롤러로 가지 않는다.
        }
        return true;
//        서블릿 필터(LoginCheckFilter.java)로 로그인 체크를 구현한 코드를 보면 isLoginCheckPath() 메서드를 통해서
//        whitelist도 체크하고 했었는데 스프링 인터셉터는 이런 것이 필요 없다. 왜냐하면 복잡한 url, 이 경로는 로그인 인증을 하지
//        않아도 뒤에 컨트롤러까지 호출하고 이 경로는 로그인을 체크해야 하고 이런 것들을 "인터셉터 등록할때" 전부 할 수 있기 때문이다.
//
//        스프링 인터셉터는 여기(preHandle()) 들어오면 무조건 인증체크 하는 구나! 생각하고 로직을 구현하면 된다.
    }

}