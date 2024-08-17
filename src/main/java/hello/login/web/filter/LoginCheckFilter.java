package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist = {
            "/",
            "/members/add",
            "/login",
            "/logout",
            "/css/*"};


    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse)response;

        try{
            log.info("인증 체크 필터 시작 {}", requestURI);
            // 로그인(인증)을 체크 해야하는 경로니? ex: /items/1, /items/edit
            if(isLoginCheckPath(requestURI)){
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                // 로그인(인증)이 안되어 있으면
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){
                    log.info("미인증 사용자 요청 {}", requestURI);
                    // requestURI로 가려고 하는데,, 인증되지 않은 사용자이다. 그럼 로그인(인증) 하고와!!
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    /*
                    * 이렇게 코드를 써주면 /login 컨트롤러로 쿼리파라미터를 redirectURL = requestURI로
                    * 보내면서 간다. 그래서 /login 이렇게 써줘도 된다.
                    * 그런데 /login?redirectURL= + requestURI를 써줌으로서 /login컨트롤러에
                    * redirectURL=requestURI로 파라미터를 넘겨주는 이유는 뭘까? /login컨트롤러로 가서
                    * 그럼 사용자가 로그인 화면을 볼텐데, 만일 사용자가 그 로그인화면에서 로그인에 성공 했을시에
                    * 가장 최근에 있던 주소인 redirectURL=requestURI로 가게끔 처리를 하기 위해서 파라미터를
                    * 넘긴 것이다./login 컨트롤러에 일단은 사용자가 이전에 있었던 주소에 대한 정보를 넘기는 것이다.
                    * 예를들어서 내가 /items로 갔는데 로그인 안해서 로그인 페이지로 갔는데, 로그인을 성공했을시에
                    * 다시 /items로 가줬으면 좋겠다.
                    * */
                    return;  // 여기가 중요, 미인증 사용자는 다음으로 진행하지 않고 끝! 뒤에 서블릿이나 컨트롤러 호출하지 않는다.
                    /*
                    * 로그인이 안되어 있으면  LoginCheckFilter에서 반환해서 끝내 버린다.
                    * 참고로 return; 하더라도 finally부분의 코드는 항상 호출된다.
                    * */
                }
            }
            chain.doFilter(request, response);
            /*
            * 1. 로그인 인증 경로, 즉, 해당 경로가 인증 여부에 대한 검사가 필요한 경우 검사했는데 해당경로가 인증이 되어있어서
            * 다음 필터나, 서블릿을 호출해야 하는 경우 실행된다.
            *
            * 2. whitelist와 같이 특정 경로는 인증을 여부를 체크할 필요 없이 무조건 다음 필터나 서블릿을 호출해야 하는 경우
            * 실행된다.
            * */
        }catch(Exception e){
            throw e;
            /*
            * 만일 try문을 실행하다가 예외가 발생했을 경우에 예외에 대한 로깅이 가능하지만, 톰켓까지 예외를 보내 줘야 한다.
            * 왜냐하면 다음필터나, 서블릿이나, 컨트롤러에서 예외가 터져서 올라오는데 그것을 여기서 처리해 버리면 서블릿 컨테이너나
            * WAS가 정상적인 흐름이라고 판단한다. 현재 개념적으로 예외가 발생했기 때문에 비정상적인 흐름이다. WAS나 서블릿 컨테이너가
            * 정상 흐름일때 내부 시스템적으로 처리하는 것이 있을것이고 비정상 적인 흐름일때, 예외가 발생했을때 내부 시스템에서 처리하는 것이 있을것이다.
            * 지금은 예외가 발생했을 시에 비정상 적은 흐름이기 때문에 서블릿 컨테이너나, WAS까지 예외를 올려 줘야 한다.
            * */
        }finally{
            log.info("인증 체크 필터 종료 {}", requestURI);
        }

    }


    /*
    * 화이트 리스트의 경우 인증 체크X
    * */

    private boolean isLoginCheckPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
        /*
        * PatternMatchUtils는 스프링이 제공하는 객체인데,, 이것 안쓰고 직접 구현하려면
        * 너무 복잡하다. ㅋㅋ whitelist와 requestURI가 단순하게 패턴에 매칭 되는가?
        * 를 확인하는 것이다.
        * */
    }

}