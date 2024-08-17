package hello.login;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WebConfig {

    @Bean
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

    @Bean
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
