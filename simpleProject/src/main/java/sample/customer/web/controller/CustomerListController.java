package sample.customer.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import sample.customer.biz.domain.Customer;
import sample.customer.biz.service.CustomerService;

@Controller
public class CustomerListController {
	
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(value="/customer", method= GET)
	public String showAllCustomers(Model model){
		//List<Customer>customers = customerService.findAll();
		//model.addAttribute("customers",customers);
		return "customer/list";
	}
	
	@RequestMapping(value="/", method=GET)
	public String home(){
		return "forward:/customer";
	}

}
