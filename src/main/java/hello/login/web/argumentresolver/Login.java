package hello.login.web.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
//    @Target(ElementType.PARAMETER) : 파라미터에만 사용
//    @Retention(RetentionPolicy.RUNTIME) : 실제 동작할때 까지 애노테이션을 남겨둔다.
//        (리플렉션 등을 활용할 수 있도록 런타임까지 애노테이션 정보가 남아있다.,,
//         강사님께서 리플렉션에 대해서 따로 설명하시지는 않으셨고 한번 읽으셨다.)


}
