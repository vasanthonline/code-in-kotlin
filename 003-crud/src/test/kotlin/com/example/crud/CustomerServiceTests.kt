package com.example.crud

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.test.context.ActiveProfiles

import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
class CustomerServiceTests @Autowired constructor(
        private val customerService: CustomerService
){

    @Test
    fun convertDTOtoModelTest(){
        val customer = Customer(1, "sample name", LocalDate.now().minusDays(10))
        val customerDTO: CustomerDTO = customerService.convertToDTO(customer)
        assertEquals(customerDTO.id, customer.id)
        assertEquals(customerDTO.name, customer.name)
        assertEquals(customerDTO.ageInDays, 10)
    }

    @Test
    fun convertModeltoDTOTest(){
        val customerDTO = CustomerDTO(1, "sample name", 10)
        val customer: Customer = customerService.convertToModel(customerDTO)
        assertEquals(customer.id, customer.id)
        assertEquals(customer.name, customer.name)
        assertEquals(customer.dateOfBirth, LocalDate.now().minusDays(10))
    }
}