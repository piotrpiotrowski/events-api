package org.siemasoft.events.core.repository;

import org.siemasoft.events.core.domain.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    Page<EventEntity> findByStartedBetweenOrderByStarted(LocalDateTime from, LocalDateTime to, Pageable pageable);

    Page<EventEntity> findByStartedBeforeAndFinishedIsNullOrderByStarted(LocalDateTime dateTime, Pageable pageable);
}
