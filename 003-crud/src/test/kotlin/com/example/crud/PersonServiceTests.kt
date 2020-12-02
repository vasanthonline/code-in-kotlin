package com.example.crud

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.test.context.ActiveProfiles

import java.time.LocalDate
import java.time.ZoneOffset

@SpringBootTest
@ActiveProfiles("test")
class PersonServiceTests @Autowired constructor(
        private val personService: PersonService
){

    @Test
    fun shouldConvertModelToDTO(){
        val date = LocalDate.now()
        val person = Person(1, "sample name", date.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000)
        val personDTO: PersonDTO = personService.convertToDTO(person)
        assertEquals(personDTO.id, person.id)
        assertEquals(personDTO.name, person.name)
        assertEquals(personDTO.dateOfBirth, date)
    }

    @Test
    fun shouldConvertDTOToModel(){
        val date = LocalDate.now()
        val personDTO = PersonDTO(1, "sample name", date)
        val person: Person = personService.convertToModel(personDTO)
        assertEquals(person.id, personDTO.id)
        assertEquals(person.name, personDTO.name)
        assertEquals(person.dateOfBirthInMs, date.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000)
    }
}