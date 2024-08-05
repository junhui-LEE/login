package hello.login.web;

public class SessionConst {
    public static final String LOGIN_MEMBER = "loginMember";
    /*
    * new로 객체를 생성하지 않고 static 변수에 접근하는데에 쓰이는 클래스이다. 그래서
    * public abstract class SessionConst {...} 이렇게 추상클래스로 만들어서
    * 생성되지 않게 하거나 [public] interface SessionConst {...} 이렇게 인터페이스로
    * 쓰는 것이 더 좋다. 참고로 인터페이스는 내부에 상수 선언시에 public static final
    * 이 생략이 가능하다.
    * */
}
