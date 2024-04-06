package com.ip.ddangddangddang.domain.file.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String objectName;

    @Column
    private String keyName;

    @Column(name = "user_id")
    private Long userId;

    public File(String objectName, String keyName, Long userId) {
        this.objectName = objectName;
        this.keyName = keyName;
        this.userId = userId;
    }
}
