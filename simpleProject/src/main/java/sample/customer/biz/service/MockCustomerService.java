package sample.customer.biz.service;

import sample.customer.biz.domain.Customer;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("customerService")
public class MockCustomerService implements CustomerService {
    // �덉젣��Mock 援ы쁽�대�濡�    // synchronized �깆쓽 �숆린泥섎━���꾪� �섏� �딅뒗�� 
    private Map<Integer, Customer> customerMap
                            = new LinkedHashMap<Integer, Customer>();
    
    private int nextId = 1;

    private boolean isExists(int id) {
        return customerMap.containsKey(id);
    }
    
    public List<Customer> findAll() {
        return new ArrayList<Customer>(customerMap.values());
    }

    public Customer findById(int id) throws DataNotFoundException {
        if (!isExists(id)) {
            throw new DataNotFoundException();
        }
        return customerMap.get(id);
    }

    public Customer register(Customer customer) {
        customer.setId(nextId++);
        customerMap.put(customer.getId(), customer);

        return customer;
    }

    public void update(Customer customer) throws DataNotFoundException {
        if (!isExists(customer.getId())) {
            throw new DataNotFoundException();
        }
        customerMap.put(customer.getId(), customer);
    }

    public void delete(int id) throws DataNotFoundException {
        if (!isExists(id)) {
            throw new DataNotFoundException();
        }
        customerMap.remove(id);
    }

    public boolean isFreeEmailCustomer(Customer customer) {
        // ��援ы쁽�먯꽌��        // Customer#isFreeEmail���몄텧�섍퀬 寃곌낵瑜�諛섑솚�섍린留��쒕떎 
        return customer.isFreeEmail();
    }

    /*@PostConstruct
    public void initCustomer() {
        nextId = 1;

        register(new Customer("�먮！", "�쒖슱��媛뺣궓援�, "jaryong@aa.bb.cc"));
        register(new Customer("湲몃룞", "�쒖슱��媛뺤꽌援�, "kildong@aa.bb.cc"));
        register(new Customer("泥좎닔", "�쒖슱���몄썝援�, "chulsoo@aa.bb.cc"));
    }*/
}
