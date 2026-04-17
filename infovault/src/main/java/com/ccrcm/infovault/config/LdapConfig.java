package com.ccrcm.infovault.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;

@Configuration
public class LdapConfig {

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource source = new LdapContextSource();
        source.setUrl("ldap://localhost:389"); // change to your LDAP URL
        source.setBase("dc=example,dc=com");   // change base DN
        source.setUserDn("cn=admin,dc=example,dc=com"); // optional (bind user)
        source.setPassword("admin"); // optional
        source.afterPropertiesSet();
        return source;
    }

    @Bean
    public AuthenticationManager ldapAuthenticationManager(
            LdapContextSource contextSource) {

        BindAuthenticator authenticator = new BindAuthenticator(contextSource);

        // 🔥 IMPORTANT → adjust based on your LDAP structure
        authenticator.setUserDnPatterns(
                new String[]{"uid={0},ou=people"}
        );

        DefaultLdapAuthoritiesPopulator authoritiesPopulator =
                new DefaultLdapAuthoritiesPopulator(contextSource, "ou=groups");

        authoritiesPopulator.setGroupSearchFilter("(member={0})");

        LdapAuthenticationProvider provider =
                new LdapAuthenticationProvider(authenticator, authoritiesPopulator);

        return new ProviderManager(provider);
    }
}
