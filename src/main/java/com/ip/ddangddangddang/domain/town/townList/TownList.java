package com.ip.ddangddangddang.domain.town.townList;

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
@Table(name = "town_list")
public class TownList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String city1;

    @Column
    private String city2;

    @Column
    private String city3;

    @Column
    private String city4;

    @Column
    private String city5;

    @Column
    private Double x;

    @Column
    private Double y;

    public TownList(String city1, String city2, String city3, String city4, String city5, Double x,
        Double y) {
        this.city1 = city1;
        this.city2 = city2;
        this.city3 = city3;
        this.city4 = city4;
        this.city5 = city5;
        this.x = x;
        this.y = y;
    }

}
