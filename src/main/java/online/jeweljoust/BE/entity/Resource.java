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

public class Resource {
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

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "auctionrequest_id")
    AuctionRequest requestResource;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "upload_by")
    Account acconutResource;

    @Column(nullable = false)
    LocalDateTime uploadAt;
}
