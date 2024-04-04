package com.ip.ddangddangddang.domain.town.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "towns")
public class Town {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String neighborIdList;

    public Town(String name, String idList) {
        this.name = name;
        this.neighborIdList = idList;
    }

}

//    @Column
//    @Convert(converter = StringListConverter.class, attributeName = "neighbor")
//    private List<String> neighbor;
