package com.example.crud

import org.springframework.data.repository.CrudRepository

interface PersonRepository : CrudRepository<Person, Long> {

}