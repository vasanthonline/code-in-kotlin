package com.example.crud

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate


@SpringBootTest
@ActiveProfiles("test")
class PersonControllerTests @Autowired constructor(
        private val personController: PersonController,
) {
    @field:Autowired
    private lateinit var truncateDatabaseService: DatabaseCleanupService

    @BeforeEach
    fun cleanupBeforeEach() {
        truncateDatabaseService.truncate()
    }
    @AfterEach
    fun cleanupAfterEach() {
        truncateDatabaseService.truncate()
    }

    @Test
    fun createPersonTest(){
        val date = LocalDate.now()
        val personDTO = PersonDTO(id = 1, name = "sample name", date)
        val createdPersonDTO: PersonDTO = personController.createPerson(personDTO)
        assertEquals(createdPersonDTO.id, 1)
        assertEquals(createdPersonDTO.dateOfBirth, date)
    }

    @Test
    fun getPersonsTest(){
        val date = LocalDate.now()
        val personDTO = PersonDTO(name = "sample name", dateOfBirth = date)
        personController.createPerson(personDTO)
        val personDTOs: List<PersonDTO> = personController.getAllPersons()
        assertEquals(personDTOs.size, 1)
        assertNotNull(personDTOs[0].id)
        assertEquals(personDTOs[0].name, "sample name")
        assertEquals(personDTOs[0].dateOfBirth, date)
    }

    @Test
    fun getPersonByIdTest(){
        val date = LocalDate.now()
        val personDTO = PersonDTO(name = "sample name", dateOfBirth = date)
        val createdPersonDTO :PersonDTO = personController.createPerson(personDTO)
        val personDTOById: PersonDTO = personController.getPersonById(createdPersonDTO.id)
        assertNotNull(personDTOById.id)
        assertEquals(personDTOById.name, "sample name")
        assertEquals(personDTOById.dateOfBirth, date)
    }

    @Test
    fun deletePersonByIdTest(){
        val date = LocalDate.now()
        val personDTO = PersonDTO(name = "sample name", dateOfBirth = date)
        val createdPersonDTO :PersonDTO = personController.createPerson(personDTO)
        personController.deletePersonById(createdPersonDTO.id)
        val personDTOs: List<PersonDTO> = personController.getAllPersons()
        assertEquals(personDTOs.size, 0)
    }
}