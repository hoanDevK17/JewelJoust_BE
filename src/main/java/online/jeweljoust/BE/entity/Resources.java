package online.jeweljoust.BE.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.jeweljoust.BE.enums.ResourceTypes;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString

public class Resources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    ResourceTypes.ResourceType resourceType;

    @Column(nullable = false)
    String path;

    @Enumerated(EnumType.STRING)
    ResourceTypes.ReferenceType referenceType;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "request_id")
    AuctionRequest auctionRequestResource;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "session_id")
    AuctionSession auctionSessionResource;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "upload_by")
    Account accountResource;

    @Column(nullable = false)
    Date uploadAt;
}
