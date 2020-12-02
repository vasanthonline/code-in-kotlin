package com.example.crud

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneOffset

@Service
class PersonService @Autowired constructor(
        private val personRepository: PersonRepository
) {
    fun getPersons(): List<Person> {
        return personRepository.findAll().toList()
    }

    fun getPersonById(id: Long): Person {
        return personRepository.findById(id).orElse(null)
    }

    fun savePerson(person: Person): Person {
        return personRepository.save(person)
    }

    fun deletePerson(id: Long): Unit {
        return personRepository.deleteById(id)
    }

    fun convertToDTO(person: Person): PersonDTO {
        val dateOfBirth = Instant.ofEpochMilli(person.dateOfBirthInMs)
                .atZone(ZoneOffset.UTC).toLocalDate()
        return PersonDTO(person.id, person.name, dateOfBirth)
    }

    fun convertToModel(personDTO: PersonDTO): Person {
        val dateInSeconds =  personDTO.dateOfBirth.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
        return Person(personDTO.id, personDTO.name, dateInSeconds * 1000)
    }
}
