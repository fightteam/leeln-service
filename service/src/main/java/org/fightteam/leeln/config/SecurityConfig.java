package org.fightteam.leeln.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdvice;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * description
 *
 * @author oyach
 * @since 0.0.1
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends GlobalMethodSecurityConfiguration {


    @Autowired
    public void registerGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("oyach").password("123456").roles("USER").and()
                .withUser("admin").password("123456").roles("ADMIN");
    }


    @Override
    public MethodInterceptor methodSecurityInterceptor() throws Exception {
        return super.methodSecurityInterceptor();
    }

    @Override
    public PreInvocationAuthorizationAdvice preInvocationAuthorizationAdvice() {
        return super.preInvocationAuthorizationAdvice();
    }
}
