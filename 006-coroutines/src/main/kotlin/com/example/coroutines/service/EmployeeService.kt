package com.example.coroutines.service

import com.example.coroutines.dto.EmployeeDTO
import com.example.coroutines.model.Employee
import com.example.coroutines.repository.EmployeeRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EmployeeService @Autowired constructor(
    private val employeeRepository: EmployeeRepository
) {
    fun createEmployee(employee: Employee) {
        employeeRepository.save(employee)
    }

    suspend fun createEmployees(employeeList: List<EmployeeDTO>) {
        employeeList.forEach {
            GlobalScope.async {
                createEmployee(Employee(it.id, it.firstName, it.lastName))
            }
        }
    }

    fun getEmployees(): List<EmployeeDTO> {
        return employeeRepository.findAll().map {
            modelToDTO(it)
        };
    }

    fun modelToDTO(employee: Employee): EmployeeDTO {
        return EmployeeDTO(employee.id, employee.firstName, employee.lastName)
    }
}