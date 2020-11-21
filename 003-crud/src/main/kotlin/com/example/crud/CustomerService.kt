package com.example.crud

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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
}
