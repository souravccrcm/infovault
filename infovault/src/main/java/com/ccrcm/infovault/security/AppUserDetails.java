package com.ccrcm.infovault.security;


import com.ccrcm.infovault.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class AppUserDetails implements UserDetails {
    private final Long userId;
    private final String username;
    //private final String password;
    private final boolean enabled;
    private final List<GrantedAuthority> authorities;

    public AppUserDetails(User appUser) {
        this.userId = appUser.getId();
        this.username = appUser.getFirstName();
        //this.password = appUser.getPasswordHash();
        this.enabled = appUser.getActive();
        // Role name -> ROLE_{NAME}
        String roleName = appUser.getRole() != null ? appUser.getRole().getName() : "USER";
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()));
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    // change the return to password
    @Override
    public String getPassword() {
        return null;
    }
    // @Override public String getPassword() { return password; }

    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }
}
