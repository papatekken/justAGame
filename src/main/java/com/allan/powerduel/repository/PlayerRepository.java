package com.allan.powerduel.repository;

import com.allan.powerduel.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByGoogleSub(String googleSub);
}
