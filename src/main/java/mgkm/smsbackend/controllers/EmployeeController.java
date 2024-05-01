package mgkm.smsbackend.controllers;

import lombok.AllArgsConstructor;
import mgkm.smsbackend.models.Employee;
import mgkm.smsbackend.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/employees")
@AllArgsConstructor
public class EmployeeController extends BaseController {

    private final EmployeeService employeeService;

    @GetMapping("/allEmployees")
    public List<Employee> getAllEmployees() {
        return this.employeeService.getAllEmployees();
    }

    @PostMapping("/addNewEmployee")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addNewEmployee(@RequestBody Employee employee) {
        this.employeeService.addNewEmployee(employee);
    }

    @DeleteMapping("/deleteEmployee")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteEmployee(@RequestBody Employee employee) {
        this.employeeService.deleteEmployee(employee);
    }

}
