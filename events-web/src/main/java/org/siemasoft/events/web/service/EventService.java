package org.siemasoft.events.web.service;

import org.siemasoft.events.core.domain.EventEntity;
import org.siemasoft.events.core.domain.LocalDateRange;
import org.siemasoft.events.core.repository.EventRepository;
import org.siemasoft.events.web.model.EventModel;
import org.siemasoft.events.web.service.exception.*;
import org.siemasoft.events.web.service.factory.EventFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;

@Service
public class EventService {

    @Autowired
    private EventFactory eventFactory;

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public EventModel create(EventModel eventModel) {
        EventEntity event = eventFactory.createWithModel(eventModel);
        EventEntity savedEvent = save(event);
        return toModel(savedEvent);
    }

    @Transactional
    public void update(EventModel eventModel) {
        EventEntity event = getEventEntity(eventModel.getId());
        validateDates(eventModel, event);
        eventFactory.updateByModel(event, eventModel);
        save(event);
    }

    @Transactional(readOnly = true)
    public Page<EventModel> getFromRange(LocalDateRange range, Pageable pageable) {
        Page<EventEntity> page = eventRepository.findByStartedBetweenOrderByStarted(range.getFrom(), range.getTo(), pageable);
        return page.map(this::toModel);
    }

    @Transactional(readOnly = true)
    public EventModel getById(long eventId) {
        EventEntity event = getEventEntity(eventId);
        return toModel(event);
    }

    @Transactional(readOnly = true)
    public Page<EventModel> getOverdue(Pageable pageable) {
        Page<EventEntity> page = eventRepository.findByStartedBeforeAndFinishedIsNullOrderByStarted(LocalDateTime.now(), pageable);
        return page.map(this::toModel);
    }

    private void validateDates(EventModel eventModel, EventEntity event) {
        if (event.getFinished() != null) {
            throw new EventFinishedAlreadyException(event.getId(), event.getFinished());
        }
        LocalDateTime eventFinishedDate = eventModel.getFinished();
        if (eventFinishedDate != null) {
            if (event.getStarted().isAfter(eventFinishedDate)) {
                throw new FinishedBeforeStartedException(event.getId(), eventFinishedDate, event.getStarted());
            }
            LocalDateTime eventStartedDate = eventModel.getStarted();
            if (eventStartedDate != null && !event.getStarted().equals(eventStartedDate)) {
                throw new DateChangeForbiddenException("Cannot change started date when finished provided", "started", eventStartedDate);
            }
        }
    }

    private EventEntity save(EventEntity event) {
        try {
            return eventRepository.saveAndFlush(event);
        } catch (DataIntegrityViolationException e) {
            throw new EventAlreadyExistsException(event.getTitle(), event.getStarted(), e);
        } catch (OptimisticLockException e) {
            throw new EventNotFoundException(event.getId());
        }
    }

    private EventEntity getEventEntity(long eventId) {
        EventEntity event = eventRepository.findOne(eventId);
        if (event == null) {
            throw new EventNotFoundException(eventId);
        }
        return event;
    }

    private EventModel toModel(EventEntity eventEntity) {
        EventModel eventModel = new EventModel();
        eventModel.setId(eventEntity.getId());
        eventModel.setTitle(eventEntity.getTitle());
        eventModel.setPriority(eventEntity.getPriority().name());
        eventModel.setStarted(eventEntity.getStarted());
        eventModel.setFinished(eventEntity.getFinished());
        eventModel.setDescription(eventEntity.getDescription());
        return eventModel;
    }

    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
}
