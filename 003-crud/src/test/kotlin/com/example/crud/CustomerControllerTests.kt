package com.example.crud

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional


@SpringBootTest
@ActiveProfiles("test")
class CustomerControllerTests @Autowired constructor(
        private val customerController: CustomerController,
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
    fun createCustomerTest(){
        val customerDTO = CustomerDTO(id = 1, name = "sample name", ageInDays = 1000)
        val createdCustomerDTO: CustomerDTO = customerController.createCustomer(customerDTO)
        assertEquals(createdCustomerDTO.id, 1)
        assertEquals(createdCustomerDTO.ageInDays, 1000)
    }

    @Test
    fun getCustomersTest(){
        val customerDTO = CustomerDTO(name = "sample name", ageInDays = 1000)
        customerController.createCustomer(customerDTO)
        val customerDTOs: List<CustomerDTO> = customerController.getAllCustomers()
        assertEquals(customerDTOs.size, 1)
        assertNotNull(customerDTOs[0].id)
        assertEquals(customerDTOs[0].name, "sample name")
        assertEquals(customerDTOs[0].ageInDays, 1000)
    }

    @Test
    fun getCustomerByIdTest(){
        val customerDTO = CustomerDTO(name = "sample name", ageInDays = 1000)
        val createdCustomerDTO :CustomerDTO = customerController.createCustomer(customerDTO)
        val customerDTOById: CustomerDTO = customerController.getCustomerById(createdCustomerDTO.id)
        assertNotNull(customerDTOById.id)
        assertEquals(customerDTOById.name, "sample name")
        assertEquals(customerDTOById.ageInDays, 1000)
    }

    @Test
    fun deleteCustomerByIdTest(){
        val customerDTO = CustomerDTO(name = "sample name", ageInDays = 1000)
        val createdCustomerDTO :CustomerDTO = customerController.createCustomer(customerDTO)
        customerController.deleteCustomerById(createdCustomerDTO.id)
        val customerDTOs: List<CustomerDTO> = customerController.getAllCustomers()
        assertEquals(customerDTOs.size, 0)
    }
}