package com.example.coroutines.repository

import com.example.coroutines.model.Employee
import org.springframework.data.repository.CrudRepository

interface EmployeeRepository : CrudRepository<Employee, Long> {

}