package com.example.app.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.app.beans.Department;
import com.example.app.beans.Employee;
import com.example.app.beans.Location;
import com.example.app.dao.BaseDao;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class EmployeeServlet
 */
public class EmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String methodName = request.getParameter("method");
		try {
			Method method = getClass().getDeclaredMethod(methodName,
					HttpServletRequest.class, HttpServletResponse.class);
			method.setAccessible(true);
			method.invoke(this, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BaseDao baseDao = new BaseDao();

	protected void listLocations(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String sql = "select location_id locationId,city from locations";
		List<Location> locations = baseDao.getForList(sql, Location.class);
		request.setAttribute("locations", locations);
		request.getRequestDispatcher("/WEB-INF/pages/employee.jsp").forward(
				request, response);
	}

	protected void listDepartments(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String args = request.getParameter("locationId");
		String sql = "select department_id departmentId,department_name departmentName from departments d where d.location_id=?";
		List<Department> departments = baseDao.getForList(sql,
				Department.class, Integer.parseInt(args));
		ObjectMapper objectMapper = new ObjectMapper();
		String result = objectMapper.writeValueAsString(departments);
		response.setContentType("text/javascript;charset=utf-8");
		response.getWriter().print(result);

	}

	protected void listEmployees(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String args = request.getParameter("departmentId");
		String sql = "select employee_id employeeId,last_name lastName from employees where department_id=?";
		List<Employee> employees = baseDao.getForList(sql, Employee.class,
				Integer.parseInt(args));
		ObjectMapper objectMapper = new ObjectMapper();
		String result = objectMapper.writeValueAsString(employees);
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(result);
	}

	protected void listPerson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String args = request.getParameter("employeeId");
		String sql = "select employee_id employeeId,last_name lastName,email,salary from employees where employee_id=?";
		Employee employee = baseDao.get(sql, Employee.class,
				Integer.parseInt(args));
		ObjectMapper objectMapper = new ObjectMapper();
		String result = objectMapper.writeValueAsString(employee);
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.print(result);
		writer.flush();
		writer.close();

	}

}
