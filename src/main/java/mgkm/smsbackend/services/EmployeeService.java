package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Employee;
import mgkm.smsbackend.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return (List<Employee>) this.employeeRepository.findAll();
    }

    public void addNewEmployee(Employee employee) {
        this.employeeRepository.save(employee);
    }

    public void deleteEmployee(Employee employee) {
        this.employeeRepository.delete(employee);
    }

}
