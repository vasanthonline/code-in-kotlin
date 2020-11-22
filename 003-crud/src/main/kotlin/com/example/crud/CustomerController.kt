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
        return customerService.getCustomers().map { customerService.convertToDTO(it) }
    }
 
    @RequestMapping(value = ["{id}"], method = [RequestMethod.GET])
    fun getCustomerById(@PathVariable("id") id: Long): CustomerDTO {
        return customerService.convertToDTO(customerService.getCustomerById(id))
    }

    @RequestMapping(method = [RequestMethod.POST])
    fun createCustomer(@RequestBody createRequest: CustomerDTO): CustomerDTO {
        val customer:Customer = customerService.convertToModel(createRequest)
        return customerService.convertToDTO(customerService.saveCustomer(customer))
    }

    @RequestMapping(value = ["{id}"], method = [RequestMethod.DELETE])
    fun deleteCustomerById(@PathVariable("id") id: Long): Unit {
       return customerService.deleteCustomer(id)
    }

}