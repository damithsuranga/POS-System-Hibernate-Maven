package lk.ijse.pos.business.custom.impl;

import lk.ijse.pos.business.custom.OrderBO;
import lk.ijse.pos.dao.custom.CustomerDAO;
import lk.ijse.pos.dao.custom.ItemDAO;
import lk.ijse.pos.dao.custom.OrderDAO;
import lk.ijse.pos.dao.custom.OrderDetailDAO;
import lk.ijse.pos.db.HibernateUtil;
import lk.ijse.pos.dto.OrderDTO;
import lk.ijse.pos.dto.OrderDetailDTO;
import lk.ijse.pos.entity.Customer;
import lk.ijse.pos.entity.Item;
import lk.ijse.pos.entity.Order;
import lk.ijse.pos.entity.OrderDetail;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderBOImpl implements OrderBO {

    @Autowired
    private OrderDAO orderDAO ;
    @Autowired
    private OrderDetailDAO orderDetailDAO;
    @Autowired
    private ItemDAO itemDAO;
    @Autowired
    private CustomerDAO customerDAO;


    @Override
    public void placeOrder(OrderDTO order) throws Exception {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            customerDAO.setSession(session);
            itemDAO.setSession(session);
            orderDAO.setSession(session);
            orderDetailDAO.setSession(session);

            Customer customer = customerDAO.find(order.getCustomerId());
            orderDAO.save(new Order(order.getOrderId(),order.getOrderDate(),customer));
            for(OrderDetailDTO dto : order.getOrderDetails()){
                orderDetailDAO.save(new OrderDetail(dto.getOrderId(),dto.getItemCode(),dto.getQty(),dto.getUnitPrice()));
                Item item = itemDAO.find(dto.getItemCode());
                int qty = item.getQtyOnHand()-dto.getQty();
                item.setQtyOnHand(qty);
            }
            session.getTransaction().commit();

        }
    }

    @Override
    public int generateOrderId() throws Exception {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            orderDAO.setSession(session);
                    return orderDAO.getLastOrderId()+1;
        }catch (NullPointerException e){
            return 1;
        }
    }
}
