package online.jeweljoust.BE.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.jeweljoust.BE.enums.ResourceTypes;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString

public class Resources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @Enumerated(EnumType.STRING)
    ResourceTypes.ResourceType resourceType;

    @Column(nullable = false)
    String path;

    @Enumerated(EnumType.STRING)
    ResourceTypes.ReferenceType referenceType;

//    @Column(name = "reference_id", nullable = false)
//    Long referenceId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reference_id", insertable = false, updatable = false)
    AuctionRequest auctionRequestResource;

//    @ManyToOne
//    @JsonIgnore
//    @JoinColumn(name = "reference_id", insertable = false, updatable = false)
//    AuctionSession auctionSessionResource;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "upload_by")
    Account accountResource;

    @Column(nullable = false)
    LocalDateTime uploadAt;
}
