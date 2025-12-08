package com.codeit_team01.sb07_hrbank_team01.history.repository;

import com.codeit_team01.sb07_hrbank_team01.history.entity.History;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long>, HistoryRepositoryCustom {

  Optional<History> findTopByOrderByCreatedAtDesc();
}
