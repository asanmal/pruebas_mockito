package org.iesvdm.employee;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test doubles that are "fakes" must be tested
 *
 *
 */
public class EmployeeInMemoryRepositoryTest {

	private EmployeeInMemoryRepository employeeRepository;
	private List<Employee> employees;

	@BeforeEach
	public void setup() {
		employees = new ArrayList<>();
		employeeRepository = new EmployeeInMemoryRepository(employees);
	}

	/**
	 * Descripcion del test:
	 * crea 2 Employee diferentes
	 * aniadelos a la coleccion de employees
	 * comprueba que cuando llamas a employeeRepository.findAll
	 * obtienes los empleados aniadidos en el paso anterior
	 */
	@Test
	public void testEmployeeRepositoryFindAll() {
		Employee employee1 = new Employee("1", 1000.0d);
		Employee employee2 = new Employee("2", 1100.0d);
		employees.add(employee1);
		employees.add(employee2);

		assertThat(employeeRepository.findAll()).containsExactly(employee1, employee2);

	}

	/**
	 * Descripcion del test:
	 * salva un Employee mediante el metodo
	 * employeeRepository.save y comprueba que la coleccion
	 * employees contiene solo ese Employee
	 */
	@Test
	public void testEmployeeRepositorySaveNewEmployee() {
		Employee employee1 = new Employee("1", 1000.0d);
		employeeRepository.save(employee1);
		assertThat(employeeRepository.findAll()).containsExactly(employee1);
	}

	/**
	 * Descripcion del tets:
	 * crea un par de Employee diferentes
	 * aniadelos a la coleccion de employees.
	 * A continuacion, mediante employeeRepository.save
	 * salva los Employee anteriores (mismo id) con cambios
	 * en el salario y comprueba que la coleccion employees
	 * los contiene actualizados.
	 */
	@Test
	public void testEmployeeRepositorySaveExistingEmployee() {

		Employee employee1 = new Employee("1", 1000.0d);
		Employee employee2 = new Employee("2", 1100.0d);
		employees.add(employee1);
		employees.add(employee2);

		employee1.setSalary(1500.0d);
		employeeRepository.save(employee1);
		employee2.setSalary(1500.0d);
		employeeRepository.save(employee2);

		assertThat(employees.get(0).getSalary()).isEqualTo(1500.0d);

	}
}
