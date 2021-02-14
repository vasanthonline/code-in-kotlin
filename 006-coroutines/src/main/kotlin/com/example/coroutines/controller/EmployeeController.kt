package com.example.coroutines.controller

import com.example.coroutines.dto.EmployeeDTO
import com.example.coroutines.model.Employee
import com.example.coroutines.service.BankAccountService
import com.example.coroutines.service.EmployeeService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/employee")
@RestController
class EmployeeController @Autowired constructor(
    private val employeeService: EmployeeService,
    private val bankAccountService: BankAccountService
)  {
    @RequestMapping(method = [RequestMethod.POST])
    suspend fun createEmployees(@RequestBody employeeList: List<EmployeeDTO>): String {
        employeeService.createEmployees(employeeList)
        return "Request accepted."
    }

    @RequestMapping(method = [RequestMethod.GET])
    fun getEmployees(): List<EmployeeDTO> {
        return employeeService.getEmployees()
    }
}