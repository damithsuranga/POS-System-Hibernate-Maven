package lk.ijse.pos.business.custom.impl;

import lk.ijse.pos.business.custom.CustomerBO;
import lk.ijse.pos.dao.custom.CustomerDAO;
import lk.ijse.pos.db.HibernateUtil;
import lk.ijse.pos.dto.CustomerDTO;
import lk.ijse.pos.entity.Customer;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerBOImpl implements CustomerBO {

    @Autowired
    private CustomerDAO customerDAO;

    @Override
    public List<CustomerDTO> getAllCustomers() throws Exception {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            customerDAO.setSession(session);
            List<CustomerDTO> customerDTOStream = customerDAO.findAll().stream().map(customer -> new CustomerDTO(customer.getId(), customer.getName(), customer.getAddress())).collect(Collectors.toList());
            session.getTransaction().commit();
            return customerDTOStream;
        }
    }

    @Override
    public boolean saveCustomer(CustomerDTO dto) throws Exception {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            customerDAO.setSession(session);
            customerDAO.save(new Customer(dto.getId(),dto.getName(),dto.getAddress()));
            session.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public boolean updateCustomer(CustomerDTO dto) throws Exception {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            customerDAO.setSession(session);
            customerDAO.update(new Customer(dto.getId(),dto.getName(),dto.getAddress()));
            session.getTransaction().commit();
            return true;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public boolean removeCustomer(String id) throws Exception {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
                session.beginTransaction();
                customerDAO.setSession(session);
                customerDAO.delete(id);
                session.getTransaction().commit();
                return true;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public CustomerDTO getCustomerById(String id) throws Exception {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
                session.beginTransaction();
                customerDAO.setSession(session);
            Customer customer = customerDAO.find(id);
            CustomerDTO customerDTO = new CustomerDTO(customer.getId(), customer.getName(), customer.getAddress());
            session.getTransaction().commit();
            return customerDTO;
        }
    }

//    public CustomerBOImpl(){
//        String lk.ijse.pos.dao = DAOFactory.getInstance().<String>getDAO(DAOTypes.CUSTOMER);
//    }

    //    public CustomerBOImpl(){
//        String lk.ijse.pos.dao = DAOFactory.getInstance().<String>getDAO(DAOTypes.CUSTOMER);
//    }


}
