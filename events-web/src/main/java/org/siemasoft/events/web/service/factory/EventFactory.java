package org.siemasoft.events.web.service.factory;

import org.siemasoft.events.core.domain.EventEntity;
import org.siemasoft.events.web.model.EventModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class EventFactory {

    public EventEntity createWithModel(EventModel eventModel) {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setTitle(eventModel.getTitle());
        eventEntity.setPriority(eventModel.getPriorityAsEnum());
        eventEntity.setStarted(truncatedToMinutes(eventModel.getStarted()));
        eventEntity.setDescription(eventModel.getDescription());
        return eventEntity;
    }

    public void updateByModel(EventEntity eventEntity, EventModel eventModel) {
        eventEntity.setId(eventModel.getId());
        if (eventModel.getTitle() != null) {
            eventEntity.setTitle(eventModel.getTitle());
        }
        if (eventModel.getPriority() != null) {
            eventEntity.setPriority(eventModel.getPriorityAsEnum());
        }
        if (eventModel.getStarted() != null) {
            eventEntity.setStarted(truncatedToMinutes(eventModel.getStarted()));
        }
        if (eventModel.getDescription() != null) {
            eventEntity.setDescription(eventModel.getDescription());
        }
        if (eventModel.getFinished() != null) {
            eventEntity.setFinished(truncatedToMinutes(eventModel.getFinished()));
        }
    }

    private LocalDateTime truncatedToMinutes(LocalDateTime date) {
        return date.truncatedTo(ChronoUnit.MINUTES);
    }
}
