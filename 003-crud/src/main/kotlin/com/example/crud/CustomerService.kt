package com.example.crud

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class CustomerService @Autowired constructor(
        private val customerRepository: CustomerRepository
) {
    fun getCustomers(): List<Customer> {
        return customerRepository.findAll().toList()
    }

    fun getCustomerById(id: Long): Customer {
        return customerRepository.findById(id).orElse(null)
    }

    fun saveCustomer(customer: Customer): Customer {
        return customerRepository.save(customer)
    }

    fun deleteCustomer(id: Long): Unit {
        return customerRepository.deleteById(id)
    }

    fun convertToDTO(customer: Customer): CustomerDTO {
        val ageInDays:Long = ChronoUnit.DAYS.between(
                customer.dateOfBirth,
                LocalDate.now()
        )
        return CustomerDTO(customer.id, customer.name, ageInDays)
    }

    fun convertToModel(customerDTO: CustomerDTO): Customer {
        val date: LocalDate = LocalDate.now().minusDays(customerDTO.ageInDays ?: 0)
        return Customer(customerDTO.id, customerDTO.name, date)
    }
}
