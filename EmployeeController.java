package com.info.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.info.entity.Employee;
import com.info.service.IEmployeeService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private IEmployeeService service;

	/***
	 * If End user enters /register in addressbar this method is called and loads
	 * EmployeeRegister.html page from /template folder
	 */
	@GetMapping("/register")
	public String showRegister() {
		return "EmployeeRegister";
	}

	/**
	 * On Form Submit (/save+POST), Read data as Object using @ModelAttribute Call
	 * service layer with object, read ID back Create message as String use Model
	 * memory, send message to UI Return back to EmployeeRegister.html
	 */
	@PostMapping("/save")
	public String saveEmployee(@ModelAttribute Employee employee, Model model) {
		Integer id = service.saveEmployee(employee);
		String message = "Employee '" + id + "' Created";
		model.addAttribute("message", message);
		return "EmployeeRegister";
	}

	// @GetMapping("/all")
	@RequestMapping(path = "/all", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewAllEmployees(Model model, HttpServletRequest request, @ModelAttribute("message") String message) {
		List<Employee> list = service.getAllEmployees();
		model.addAttribute("list", list);
		// request.
		model.addAttribute("message", message);
		return "EmployeeData";
	}

	@GetMapping(value = "/delete")
	public String deleteEmployee(@RequestParam int id, Model model, RedirectAttributes redirectAttrs) {

		boolean is = service.isPresent(id);
		// System.out.println("isPresent "+is);
		if (is) {
			service.deleteEmployee(id);
			redirectAttrs.addAttribute("message", "Record deleted successfully");
			return "redirect:all";
		} else {
			// redirectAttrs.addAttribute("message", "Given id is not exits");
			return "redirect:all";
		}
	}

	@GetMapping("/edit")
	public String empEdit(@RequestParam int id, Model model) {
		// System.out.println("Id is "+ id);
		boolean is = service.isPresent(id);
		// System.out.println("isPresent "+is);
		if (is) {
			Employee emp = service.editEmployee(id);
			System.out.println("Employee Data " + emp);
			model.addAttribute("employee", emp);
			return "EmployeeEdit";
		} else {
			// redirectAttrs.addAttribute("message", "Given id is not exits");
			return "redirect:all";
		}
		// return "";
	}

	@PostMapping("/update")
	public String updateEmployee(@ModelAttribute Employee employee, RedirectAttributes redirectAttrs) {
		service.updateEmployee(employee);
		redirectAttrs.addAttribute("message", "Record updated successfully");
		return "redirect:all";
	}
}
