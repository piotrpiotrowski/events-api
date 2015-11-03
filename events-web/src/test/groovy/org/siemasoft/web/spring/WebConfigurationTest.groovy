package org.siemasoft.web.spring

import org.siemasoft.events.core.repository.EventRepository
import org.siemasoft.events.web.spring.WebConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification


@ActiveProfiles("local")
@WebAppConfiguration
@ContextConfiguration(classes = [WebConfiguration.class])
class WebConfigurationTest extends Specification {

    @Autowired
    EventRepository eventRepository

    def "should run web configuration"() {
        expect:
        eventRepository != null
    }
}
