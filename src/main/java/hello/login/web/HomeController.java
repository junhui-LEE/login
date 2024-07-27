package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/")
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

}