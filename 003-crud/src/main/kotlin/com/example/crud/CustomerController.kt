package com.example.crud

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit


@RequestMapping("/customers")
@RestController
class CustomerController @Autowired constructor(
        private val customerService: CustomerService
) {
 
    @RequestMapping(method = [RequestMethod.GET])
    fun getAllCustomers(): List<CustomerDTO> {
        return customerService.getCustomers().map { convertToDTO(it) }
    }
 
    @RequestMapping(value = ["{id}"], method = [RequestMethod.GET])
    fun getCustomerById(@PathVariable("id") id: Long): CustomerDTO {
        return convertToDTO(customerService.getCustomerById(id))
    }

    @RequestMapping(method = [RequestMethod.POST])
    fun createCustomer(@RequestBody createRequest: CustomerDTO): CustomerDTO {
        return convertToDTO(customerService.saveCustomer(convertToModel(createRequest)))
    }

    @RequestMapping(value = ["{id}"], method = [RequestMethod.DELETE])
    fun deleteCustomerById(@PathVariable("id") id: Long): Unit {
       return customerService.deleteCustomer(id)
    }

    fun convertToDTO(customer: Customer): CustomerDTO {
        val age:Long = ChronoUnit.DAYS.between(
                customer.dateOfBirth,
                LocalDate.now()
        )
        return CustomerDTO(customer.id, customer.name, age)
    }

    fun convertToModel(customerDTO: CustomerDTO): Customer {
        val date: LocalDate = LocalDate.now().minusDays(customerDTO.age ?: 0)
        return Customer(customerDTO.id, customerDTO.name, date)
    }
}