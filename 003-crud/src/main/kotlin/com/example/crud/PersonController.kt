package com.example.crud

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RequestMapping("/person")
@RestController
class PersonController @Autowired constructor(
        private val personService: PersonService
) {
 
    @RequestMapping(method = [RequestMethod.GET])
    fun getAllPersons(): List<PersonDTO> {
        return personService.getPersons().map { personService.convertToDTO(it) }
    }
 
    @RequestMapping(value = ["{id}"], method = [RequestMethod.GET])
    fun getPersonById(@PathVariable("id") id: Long): PersonDTO {
        return personService.convertToDTO(personService.getPersonById(id))
    }

    @RequestMapping(method = [RequestMethod.POST])
    fun createPerson(@RequestBody createRequest: PersonDTO): PersonDTO {
        val person:Person = personService.convertToModel(createRequest)
        return personService.convertToDTO(personService.savePerson(person))
    }

    @RequestMapping(value = ["{id}"], method = [RequestMethod.DELETE])
    fun deletePersonById(@PathVariable("id") id: Long): Unit {
       return personService.deletePerson(id)
    }

}