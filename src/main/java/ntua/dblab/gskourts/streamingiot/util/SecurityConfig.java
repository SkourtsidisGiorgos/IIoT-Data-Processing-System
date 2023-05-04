//package ntua.dblab.gskourts.streamingiot.util;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
///*
// * This class is used to disable Spring Security when the application.security.enabled property is set to false.
// */
//@Configuration
//@ConditionalOnProperty(name="application.security.enabled", havingValue="false")
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeRequests().antMatchers("/").permitAll();
//    }
//
//    @Bean
//    @ConditionalOnProperty(name="application.security.enabled", havingValue="true")
//    public UserDetailsService users() {
//            UserDetails user = User.builder()
//            .username("user")
//            .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
//            .roles("USER")
//            .build();
//            UserDetails admin = User.builder()
//            .username("admin")
//            .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
//            .roles("USER", "ADMIN")
//            .build();
//            return new InMemoryUserDetailsManager(user, admin);
//        }
//}
