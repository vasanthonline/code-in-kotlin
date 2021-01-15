package com.example.eventsourcing.services

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.Table
import javax.persistence.metamodel.Metamodel
import kotlin.reflect.full.findAnnotation

@Service
@Profile("test")
class DatabaseCleanupService @Autowired constructor(private val entityManager: EntityManager) : InitializingBean {
    private lateinit var tableNames: List<String>

    override fun afterPropertiesSet() {
        val metaModel: Metamodel = entityManager.metamodel
        tableNames = metaModel.managedTypes
            .filter {
                it.javaType.kotlin.findAnnotation<Table>() != null
            }
            .map {
                val tableAnnotation: Table? = it.javaType.kotlin.findAnnotation()
                tableAnnotation?.name ?: throw IllegalStateException("should never get here")
            }
    }

    /**
     * Utility method that truncates all identified tables
     */
    @Transactional
    fun truncate() {
        entityManager.flush()
        entityManager.createNativeQuery("SET @@foreign_key_checks = 0;").executeUpdate()
        tableNames.forEach { tableName ->
            entityManager.createNativeQuery("TRUNCATE TABLE $tableName;").executeUpdate()
        }
        entityManager.createNativeQuery("SET @@foreign_key_checks = 1;").executeUpdate()
    }
}