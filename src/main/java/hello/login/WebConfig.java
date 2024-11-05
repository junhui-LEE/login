package hello.login;

import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
//        우리가 만든 LoginMemberArgumentResolver을 등록하는 것이다.
        resolvers.add(new LoginMemberArgumentResolver());
    }

    // 스프링 컨테이너에 내가 만든 LogInterceptor 를 빈으로 등록을 하는 것이다.
    // 스프링이 오버라이딩을 함으로서 빈으로 등록하도록 하는 방법을 지원하는 것이다.
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // InterceptorRegistry 이라고 스프링이 지원을 해주고 이것은 체인 형식으로 지원된다.
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")    // 전체 경로에 대해서 모두 적용시키려면 /** 로 해줘야 한다. 서블릿 필터와 조금 다르다.
                .excludePathPatterns("/css/**", "/*.ico", "/error");  // 전체 경로는 다 인터셉터 적용되지"만" 이 경로는 인터셉터 적용시키지 마!!

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/add", "/login", "/logout",
                        "/css/**", "/*.ico", "/error");
    }


//    @Bean
    public FilterRegistrationBean logFilter(){
        FilterRegistrationBean<Filter>  filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        /*
        * 필터 등록 빈 만들고 거기에 우리가 만든 필터를 넣어주면 된다.
        * */
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

//    @Bean
    public FilterRegistrationBean loginCheckFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");
        /*
        * addUrlPatterns("/*"); : 모든 요청에 로그인 필터를 적용한다.
        * 어차피 whitelist 넣어 놓아서 whitelist에서 거르고 있다. addUrlPatterns(); 여기서 거를 수도 있는데, 우리가 지금 구현하려는 것은
        * 미래까지 전부, whitelist는 빼고 미래에 새로운 기능이 만들어지면 그래도 설정은 안바꾸고 싶은 것이다. whitelist 뺴고 나머지는 인증체크를
        * 할 것으로 정책을 가져갔다.
        * */
        return filterRegistrationBean;
    }

    /*
    * 참고로 필터 한번더 호출되니까 성능이 더 떨어지지 않나요?? --> 이것은 바다의 모래알 같은 것이다. 메모리 참조 되어 있는것
    * 메모리에서 한번 호출하는 것은 웹애플리케이션에서는 메모리에 있는 메서드 하나 호출하는 것 정도는 성능 저하가 없다.
    * 참고로 웹애플리케이셔 하나 띄우면 메서드 수십개 호출된다.
    * */

}
