package com.ip.ddangddangddang.domain.auction.entity;

import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.common.timestamp.Timestamp;
import com.ip.ddangddangddang.domain.file.entity.File;
import com.ip.ddangddangddang.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE auctions SET deleted_at = CONVERT_TZ(NOW(), @@session.time_zone, '+09:00') WHERE id = ?")
@SQLRestriction(value = "deleted_at IS NULL")
@Entity
@Table(name = "auctions")
public class Auction extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long townId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private Long price;

    @Column
    private Long buyerId;

    @Column(nullable = false, name = "status_enum")
    @Enumerated(EnumType.STRING)
    private StatusEnum statusEnum;

    @Column
    private LocalDateTime finishedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "file_id")
    private File file;

    public Auction(AuctionRequestDto requestDto, User user, File file) {
        this.townId = user.getTown().getId();
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.price = 0L;
        this.statusEnum = StatusEnum.ON_SALE;
        this.finishedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()
            .plusMinutes(5L);
        // this.finishedAt = LocalDateTime.now().plusMinutes(5L);
        this.user = user;
        this.file = file;
    }

    public void updateStatusToHold() {
        this.statusEnum = StatusEnum.HOLD;
    }

    public void updateStatusToComplete() {
        this.statusEnum = StatusEnum.COMPLETED;
    }

    public void updateBid(Long price, Long buyerId) {
        this.price = price;
        this.buyerId = buyerId;
    }

}
