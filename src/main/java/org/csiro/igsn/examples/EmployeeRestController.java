package org.csiro.igsn.examples;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 
@RestController
@RequestMapping(value = "/rest")//http://localhost:8080/CSIRO-IGSN/rest/employees
public class EmployeeRestController
{
	@RequestMapping(value = "/employees")
    public EmployeeListVO getAllEmployees()
    {
        EmployeeListVO employees = new EmployeeListVO();
         
        EmployeeVO empOne = new EmployeeVO(1,"Lokesh","Gupta","howtodoinjava@gmail.com");
        EmployeeVO empTwo = new EmployeeVO(2,"Amit","Singhal","asinghal@yahoo.com");
        EmployeeVO empThree = new EmployeeVO(3,"Kirti","Mishra","kmishra@gmail.com");
         
         
        employees.getEmployees().add(empOne);
        employees.getEmployees().add(empTwo);
        employees.getEmployees().add(empThree);
         
        return employees;
    }
    //Instead of returning the java objects directly, you can wrap them inside ResponseEntity. The ResponseEntity is a class in Spring MVC 
    //that acts as a wrapper for an object to be used as the body of the result together with a HTTP status code. This provides greater control
    //over what you are returning to client in various use cases. e.g. returning a 404 error if no employee is found for given employee id
	 @RequestMapping(value = "/employees/{id}")
    public ResponseEntity<EmployeeVO> getEmployeeById (@PathVariable("id") int id)
    {
        if (id <= 3) {
            EmployeeVO employee = new EmployeeVO(1,"Lokesh","Gupta","howtodoinjava@gmail.com");
            return new ResponseEntity<EmployeeVO>(employee, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    
    @RequestMapping(value = "/xmlinputtest")
    public ResponseEntity<EmployeeVO> getXmlInputTest (@RequestBody EmployeeListVO input)
    { 
      
       return new ResponseEntity<EmployeeVO>(input.getEmployees().get(0), HttpStatus.OK);

    }
}