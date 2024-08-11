package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request){

        HttpSession session = request.getSession(false);
        if(session == null){
            return "세션이 없습니다.";
        }

        // 세션 데이터 출력
        session.getAttributeNames()
                .asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));
        /*
        * asIterator()과 forEachRemaining()을 사용하면 배열을 루프 돌릴수 있게 된다. 이 방법 뿐만 아니라 여러가지 방법들이 있다.
        * 이 방법을 이용해서 세션에 있는 모든 Attribute를 꺼내서 확인할 수 있다.
        * */

        // 세션이 기본으로 제공하는 데이터 출력
        log.info("sessionId={}", session.getId());
        /* sessionId: 세션Id, JSESSION의 값이다.  예)6AABD48FAD04C110DAE185A4DEB4F77D  */
        log.info("maxInactiveInterval={}", session.getMaxInactiveInterval());
        /* maxInactiveInterval: 세션을 비활성화 시키는 최대 기간, 세션의 유효 시간, 예) 1800초 (30분) */
        log.info("creationTime={}", new Date(session.getCreationTime()));
        /* creationTime: 세션 생성일시 */
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));
        /* lastAccessedTime: 마지막에 이 세션에 접근한 시간, 세션과 연결된 사용자가 최근에 서버에 접근한 시간,
        *                    클라이언트에서 서버로 sessionId(JSESSION)를 요청한 경우에 갱신된다. */
        log.info("isNew={}", session.isNew());
        /* isNew: 새로 생성된 세션인지, 아니면 이미 과거에 만들어 졌고, 클라이언트에서 서버로 sessionId(JSESSIONID)를 요청해서 조회된 세션인지 여부 */

        return "세션 출력";

    }
}