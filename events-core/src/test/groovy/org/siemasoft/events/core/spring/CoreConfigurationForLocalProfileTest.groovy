package org.siemasoft.events.core.spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@ActiveProfiles("local")
@ContextConfiguration(classes = CoreConfiguration.class)
class CoreConfigurationForLocalProfileTest extends Specification {

    @Autowired
    JpaTransactionManager jpaTransactionManager

    @PersistenceContext
    EntityManager entityManager


    def "spring context run successfully"() {
        expect:
        jpaTransactionManager != null
        entityManager != null
    }
}