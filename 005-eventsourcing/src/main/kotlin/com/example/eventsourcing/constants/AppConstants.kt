package com.example.eventsourcing.constants

object AppConstants {

    enum class ActivityType {
        CREATE_ACCOUNT, CREDIT_TO_ACCOUNT, DEBIT_FROM_ACCOUNT, GET_ACCOUNT, GET_EVENTS_FOR_ACCOUNT
    }
}