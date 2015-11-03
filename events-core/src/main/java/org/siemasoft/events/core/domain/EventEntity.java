package org.siemasoft.events.core.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "EVENTS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"EVENT_TITLE", "START_DATE"}))
@EqualsAndHashCode(of = {"title", "started"})
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EVENT_ID")
    private long id;

    @Column(name = "EVENT_TITLE", length = Constrains.TITLE_MAX_LENGTH, nullable = false)
    private String title;

    @Column(name = "EVENT_PRIORITY", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EventPriority priority;

    @Column(name = "START_DATE", nullable = false)
    private LocalDateTime started;

    @Column(name = "FINISH_DATE")
    private LocalDateTime finished;

    @Column(name = "DESCRIPTION", length = Constrains.DESCRIPTION_MAX_LENGTH)
    private String description;

    public static final class Constrains {

        public static final int TITLE_MIN_LENGTH = 1;

        public static final int TITLE_MAX_LENGTH = 200;

        public static final int DESCRIPTION_MIN_LENGTH = 1;

        public static final int DESCRIPTION_MAX_LENGTH = 500;
    }
}