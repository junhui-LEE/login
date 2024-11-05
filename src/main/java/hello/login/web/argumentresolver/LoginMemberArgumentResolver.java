package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

//   supportsParameter() : @Login 애노테이션이 있으면서 Member 타입이면 해당 ArgumentResolver가 사용된다.
//   resolveArgument() : 컨트롤러 호출 직전에 호출 되어서 필요한 파라미터 정보를 생성해 준다. 여기서는 세션에 있는
//                       로그인 회원 정보인 member객체를 찾아서 반환해준다. 이후 스프링MVC는 컨트롤러의 메서드를
//                       호출하면서 여기에서 반환된 member 객체를 파라미터에 전달해 준다.

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");
//        파라미터 정보가 넘어오는데, 넘어온 파라미터에 애노테이션이 있는지 묻는 것이다.
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasMemberType;
//        true 이면 아래에 있는 resolveArgument 가 실행되고, false 이면 resolveArgument가 실행되지 않는다.
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        log.info("resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
        // --> HttpServletRequest 를 뽑아 내는 것이다. 이렇게 뽑을 수 있다.

        HttpSession session = request.getSession(false);
        if(session == null){
            // 로그인을 안한 사용자 이다.
            return null;
            // 세션이 null 이면 @Login Member loginMember여기 loginMember에 null을 넣어 버릴 것이다.
        }
        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }

}