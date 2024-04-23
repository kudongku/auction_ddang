package com.ip.ddangddangddang.domain.file.entity;

import com.ip.ddangddangddang.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String objectName;

    @Column
    private String keyName;

    @Column
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public File(String objectName, String keyName, String filePath, User user) {
        this.objectName = objectName;
        this.keyName = keyName;
        this.filePath = filePath;
        this.user = user;
    }
}
