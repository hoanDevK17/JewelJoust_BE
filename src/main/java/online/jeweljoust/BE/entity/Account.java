package online.jeweljoust.BE.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Columns;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString

public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long userid;

    // bắt đầu thêm code
    @Column(unique = true)
    String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    String fullname;

    String address;

    @Temporal(TemporalType.DATE)
    Date birthday;

    @Column(unique = true)
    String email;

    //@Column(unique = true)
    String phone;

    String role;

    int credibility;

    //hết code thêm

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

//    @Override
//    public String getPassword() {
//        return this.password;
//    }
//
//    @Override
//    public String getUsername() {
//        return this.username;
//    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}