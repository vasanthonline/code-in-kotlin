package com.example.eventsourcing.repository

import com.example.eventsourcing.models.Activity
import org.springframework.data.repository.CrudRepository

interface EventStoreRepository: CrudRepository<Activity, Long> {
    fun findByAccountIdOrderByCreatedAtAsc(accountId: String): Array<Activity>
}