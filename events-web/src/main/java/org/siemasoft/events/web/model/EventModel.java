package org.siemasoft.events.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.siemasoft.events.core.domain.EventEntity;
import org.siemasoft.events.core.domain.EventPriority;
import org.siemasoft.events.web.controller.URL;
import org.siemasoft.events.web.controller.constrain.NowPlusMinutes;
import org.siemasoft.events.web.controller.constrain.StringEnumeration;
import org.siemasoft.events.web.controller.group.Create;
import org.siemasoft.events.web.controller.group.Update;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
public class EventModel implements Linkable {

    @Min(value = 1, groups = Update.class)
    private long id;

    @NotBlank(groups = Create.class)
    @Length(min = EventEntity.Constrains.TITLE_MIN_LENGTH, max = EventEntity.Constrains.TITLE_MAX_LENGTH, groups = {Create.class, Update.class})
    private String title;

    @NotNull(groups = Create.class)
    @StringEnumeration(value = EventPriority.class, groups = {Create.class, Update.class})
    private String priority;

    @NotNull(groups = Create.class)
    @NowPlusMinutes(value = 1, groups = Create.class)
    private LocalDateTime started;

    @Null(groups = Create.class)
    @NowPlusMinutes(value = 0, groups = Update.class)
    private LocalDateTime finished;

    @Length(min = EventEntity.Constrains.DESCRIPTION_MIN_LENGTH, max = EventEntity.Constrains.DESCRIPTION_MAX_LENGTH, groups = {Create.class, Update.class})
    private String description;

    @JsonIgnore
    public EventPriority getPriorityAsEnum() {
        return EventPriority.valueOf(priority);
    }

    @JsonIgnore
    @Override
    public String getResourceLink() {
        return String.format("%s/%d", URL.EVENTS, id);
    }
}
