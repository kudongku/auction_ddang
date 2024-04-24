package com.ip.ddangddangddang.domain.user.entity;

import com.ip.ddangddangddang.domain.common.timestamp.Timestamp;
import com.ip.ddangddangddang.domain.town.entity.Town;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@SQLDelete(sql = "UPDATE users SET deleted_at = CONVERT_TZ(NOW(), @@session.time_zone, '+09:00') WHERE id = ?")@SQLRestriction(value = "deleted_at IS NULL")
@Entity
@Table(name = "users")
public class User extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "town_id")
    private Town town;

    public User(Long userId, String email) {
        this.id = userId;
        this.email = email;
    }

    public User(String email, String nickname, String password, Town town) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.town = town;
    }

    public void updateUser(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public void updateLocation(Town town) {
        this.town = town;
    }

}
