package com.ip.ddangddangddang.domain.auction.entity;

import com.ip.ddangddangddang.domain.auction.dto.request.AuctionRequestDto;
import com.ip.ddangddangddang.domain.common.timestamp.Timestamp;
import com.ip.ddangddangddang.domain.result.entity.Result;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE auctions SET deleted_at = NOW() WHERE id = ?")
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

    @Column(nullable = false, name = "status_enum")
    @Enumerated(EnumType.STRING)
    private StatusEnum statusEnum;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime finishedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "auction")
    private Result result;

    @Column
    private Long fileId;

    public Auction(AuctionRequestDto requestDto, User user) {
        this.townId = user.getTown().getId();
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.statusEnum = StatusEnum.ON_SALE;
        this.finishedAt = LocalDateTime.now().plusDays(1);
        this.user = user;
        this.fileId = requestDto.getFileId();
    }

}
