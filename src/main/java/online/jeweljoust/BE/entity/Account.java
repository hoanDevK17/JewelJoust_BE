package online.jeweljoust.BE.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.jeweljoust.BE.enums.AccountRole;
import online.jeweljoust.BE.enums.AccountStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString

public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

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

    @Column(unique = true)
    String phone;

    @Enumerated(EnumType.STRING)
    AccountRole role;

    int credibility;

    @Enumerated(EnumType.STRING)
    AccountStatus status;

    @OneToOne(mappedBy = "accountWallet",cascade = CascadeType.ALL)
//            @JsonIgnore
    Wallet wallet;
    public Wallet getWallet() {
        return wallet;
    }
    @OneToMany(mappedBy = "accountRequest",cascade = CascadeType.ALL)
    @JsonIgnore
    Set<AuctionRequest> auctionRequests;

    @OneToMany(mappedBy = "managerSession",cascade = CascadeType.ALL)
    @JsonIgnore
    Set<AuctionSession> ManagerAuctionSessions;

    @OneToMany(mappedBy = "staffSession",cascade = CascadeType.ALL)
    @JsonIgnore
    Set<AuctionSession> StaffAuctionSessions;

    @OneToMany(mappedBy = "accountRegistration",cascade = CascadeType.ALL)
    @JsonIgnore
    Set<AuctionRegistration> auctionRegistrations;

    @OneToMany(mappedBy = "accountInitial",cascade = CascadeType.ALL)
    @JsonIgnore
    Set<InitialValuation> initialValuations;

    @OneToMany(mappedBy = "ultimateStaff",cascade = CascadeType.ALL)
    @JsonIgnore
    Set<UltimateValuation> ultimateStaff;

    @OneToMany(mappedBy = "ultimateManager",cascade = CascadeType.ALL)
    @JsonIgnore
    Set<UltimateValuation> ultimateManager;

    @OneToMany(mappedBy = "accountShipment",cascade = CascadeType.ALL)
    @JsonIgnore
    Set<Shipment> shipment;

    @OneToMany(mappedBy = "accountResource",cascade = CascadeType.ALL)
    @JsonIgnore
    Set<Resources> resource;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().toString()));
    }

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