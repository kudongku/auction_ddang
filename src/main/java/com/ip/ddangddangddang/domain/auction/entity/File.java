package com.ip.ddangddangddang.domain.auction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "key_name")
    private String keyName;

    @Column(name = "original_file_name")
    private String originalFileName;

    public File(String filePath, String keyName, String originalFileName) {
        this.filePath = filePath;
        this.keyName = keyName;
        this.originalFileName = originalFileName;
    }
}
