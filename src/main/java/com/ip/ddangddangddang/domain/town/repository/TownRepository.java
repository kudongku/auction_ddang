package com.ip.ddangddangddang.domain.town.repository;

import com.ip.ddangddangddang.domain.town.entity.Town;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TownRepository extends JpaRepository<Town, Long> {

    Optional<Town> findByName(String address);

}
