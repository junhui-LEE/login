package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

//    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId,
                            Model model){
//        쿠키를 받는 방법은 여러가지가 있다. HttpServletRequest에서 꺼낼수도 있고 여러가지 방법이 있지만 여기서는 스프링이 제공하는 @CookieValue를 쓰겠다.
//        스프링이 String에서 Long으로 타입 컨버팅을 해준다. @RequestParam에서도 배웠다.(쿠키 값이 String이다.)

        if(memberId == null){
//            로그인 안하면 그냥 홈화면 보여준다.
            return "home";
        }

        // 로그인에 성공한 사용자, 쿠키가 있는 사용자
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null){
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request,
                              Model model){

        // 세션 관리자에 저장된 회원 정보 조회
        Member member = (Member)sessionManager.getSession(request);
        if(member == null){
            return "home";
        }

        // 로그인
        model.addAttribute("member", member);
        return "loginHome";

    }

//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request,
                              Model model){

        // 세션이 없으면 home
        HttpSession session = request.getSession(false);
        if(session == null){
            return "home";
        }

        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        // 세션에 회원 데이터가 없으면 home
        if(loginMember == null){
            return "home";
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";

    }



//    @GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            Model model) {

        // 세션에 회원 데이터가 없으면 home
        if(loginMember == null){
            return "home";
        }
        /*
        * loginV3()에서, 검증기도 통과하고 DB에서 꺼낸 객체하고 비밀번호 비교하는 것도 통과하여 로그인에
        * 성공하면 그때 비로소 request.getSession();해서 세션 만들고 session.setAttribute(); 해서
        * 세션의 Attribute에 값도 넣는다. 로그인에 실패하면 세션자체를 만들지 않는다. 그래서 @SessionAttribute
        * 를 통해서 주입받는 loginMember가 당연히 null 이다.
        * */

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

//    *************************************************************************************************************
//    우리는 지금 @Login 이라는 애노테이션을 직접 만들고 그 다음에 @Login Member loginMember 이렇게 파라미터를 설정했을때 그냥
//    로그인 된 사용자가 파라미터 자리(loginMember)에 들어오게 할 것이다. Spring MVC에서
//    제공하는  ArgumentResolver 이라고 하는 것의 기능(파라미터를 처리해서 핸들러 어뎁터에 주는 기능)을 활용하면 된다. @Login 이라는
//    애노테이션을 만들고 ArgumentResolver로 하여금 파라미터를 @Login Member loginMember 에 들어가는 파라미터를 처리 하게끔 하면 된다.
//    -순서
//        1)@Login 애노테이션 만들기
//        2) @Login을 처리 할 수 있는 LoginMemberArgumentResolver 만들기
//        3) ArgumentResolver에 우리가 만든 LoginMemberArgumentResolver 을 등록시키기
//
    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model){
//        바로 위의 homeLoginV3Spring()에서는 인자로
//        @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember 와 같이
//        세션을 고민하고 그랬는데, 더 편리한 방법으로 @Login 만 있으면 된다. ArgumentResolver가 인증된 멤버만 인자로 넣어준다.

         // 세션에 회원 데이터가 없으면 -> 홈화면을 보여준다.
         if(loginMember == null){
             return "home";
         }
         // 세션이 유지되면 로그인으로 이동 -> 로그인이 된 사용자가 볼 수 있는 홈화면을 보여준다.
         model.addAttribute("member", loginMember);
         return "loginHome";
    }
//    *************************************************************************************************************


}