package hcmute.edu.watchstore.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mongodb.MongoException;

import hcmute.edu.watchstore.base.ServiceBase;
import hcmute.edu.watchstore.constants.ResponseCode;
import hcmute.edu.watchstore.dto.request.OrderRequest;
import hcmute.edu.watchstore.dto.response.OrderResponse;
import hcmute.edu.watchstore.dto.response.ProductItemResponse;
import hcmute.edu.watchstore.entity.Cart;
import hcmute.edu.watchstore.entity.Order;
import hcmute.edu.watchstore.entity.Product;
import hcmute.edu.watchstore.entity.ProductItem;
import hcmute.edu.watchstore.entity.User;
import hcmute.edu.watchstore.repository.CartRepository;
import hcmute.edu.watchstore.repository.OrderRepository;
import hcmute.edu.watchstore.repository.UserRepository;
import hcmute.edu.watchstore.service.OrderService;
import hcmute.edu.watchstore.service.ProductItemService;
import hcmute.edu.watchstore.service.ProductService;

@Service
public class OrderServiceImpl extends ServiceBase implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductItemService productItemService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Override
    public ResponseEntity<?> getOrderUser(ObjectId userId) {
        Optional<User> currentUser = this.userRepository.findById(userId);
        List<Order> orderList = this.orderRepository.findAll();
        List<OrderResponse> userOrder = new ArrayList<>();
        if (!currentUser.isPresent()) {
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
        }

        for(ObjectId id : currentUser.get().getOrder()) {
            Order order = findItem(id, orderList);
            if (order != null) {
                OrderResponse response = new OrderResponse(
                    order.getId().toHexString(),
                    order.getOrderDate(),
                    order.getTotalPrice(),
                    this.productItemService.findProductItemResponse(order.getProductItems()),
                    order.getUser(),
                    order.getAddress(),
                    order.getState()
                );

                userOrder.add(response);
            }
        }
        return success(userOrder);
    }

    @Override
    public ResponseEntity<?> createOrder(OrderRequest order, ObjectId userId) {
        Optional<User> currentUser = this.userRepository.findById(userId);

        if (!currentUser.isPresent()) {
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
        }

        Order newOrder = new Order(
            new ObjectId(),
            new Date(),
            calculatorTotalPrice(order.getProductItem()),
            order.getProductItem(),
            userId,
            order.getAddress(),
            "processing"
        );

        try {
            this.orderRepository.save(newOrder);
            handleManageOrderUser(order.getProductItem(), userId, "create");
            List<ObjectId> orderUser = currentUser.get().getOrder();
            orderUser.add(newOrder.getId());
            currentUser.get().setOrder(orderUser);
            this.userRepository.save(currentUser.get());

            return success("Create order success !!!");
        } catch (MongoException e) {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> cancelOrderr(ObjectId orderId, ObjectId userId) {
        Optional<Order> order = this.orderRepository.findById(orderId);
        Optional<User> currentUser = this.userRepository.findById(userId);
        if (!order.isPresent() || order.get().getState().equals("shipping")) {
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
        }

        try {
            this.orderRepository.deleteById(orderId);
            handleManageOrderUser(order.get().getProductItems(), userId, "delete");
            List<ObjectId> orderUser = currentUser.get().getOrder();
            orderUser.remove(orderId);
            currentUser.get().setOrder(orderUser);
            this.userRepository.save(currentUser.get());
            return success("Cancel order success ful !!!");
        } catch (MongoException e) {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }
    }

    public void handleManageOrderUser(List<ObjectId> productItem, ObjectId userId, String message) {
        Optional<Cart> userCart = this.cartRepository.findByUser(userId);

        if (userCart.isPresent()) {
            try {
                List<ObjectId> cartItem = userCart.get().getProductItems();

                if (message.equals("delete")) 
                    cartItem.addAll(productItem);

                if (message.equals("create")) 
                    cartItem.removeAll(productItem);
                    
                userCart.get().setProductItems(cartItem);
                this.cartRepository.save(userCart.get());
            } catch (Exception e) {
                throw new MongoException("Can't update cart user");
            }
        }

        // handle manage quantity product
        List<ProductItem> items = this.productItemService.findItemByList(productItem);
        List<Product> products = this.productService.findAllNormal();
        List<Product> updated = new ArrayList<>();
        for(ProductItem item : items) {
            Product product = findProduct(item.getProduct(), products);
            if (product != null) {
                int amount = product.getAmount();
                if (message.equals("delete")) {
                    amount = amount + item.getQuantity();
                }
                if (message.equals("create")) {
                    amount = amount - item.getQuantity();
                }
                product.setAmount(amount);
                updated.add(product);
            }
        }
        this.productService.saveProductByList(updated);
    }

    public List<ObjectId> getListProductId(List<ProductItem> productItem) {
        List<ObjectId> productId = new ArrayList<>();
        for(ProductItem item : productItem) {
            productId.add(item.getProduct());
        }
        return productId;
    }

    public Product findProduct(ObjectId id, List<Product> products) {
        return products.stream()
                 .filter(product -> product.getId().equals(id))
                 .findFirst()
                 .orElse(null);
    }

    public double calculatorTotalPrice(List<ObjectId> productItem) {
        List<ProductItemResponse> responses = this.productItemService.findProductItemResponse(productItem);
        double totalPrice = 0;

        for (ProductItemResponse resp : responses) {
            totalPrice += resp.getProduct().getPrice() * resp.getQuantity();
        }

        return totalPrice;
    }
    
    public Order findItem(ObjectId id, List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ResponseEntity<?> getByState(String state) {
        List<Order> orders = this.orderRepository.findAll();
        List<Order> result = new ArrayList<>();
        for (Order o : orders) {
            if (o.getState().equals(state)) {
                result.add(o);
            }
        }
        
        if (!result.isEmpty()) {
            return success(result);
        }
        return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
    }

    @Override
    public ResponseEntity<?> setStateOrder(ObjectId orderId, String state) {
        Order order = this.orderRepository.findById(orderId).orElse(null);
        if (order == null) 
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage()); 
        
        try {
            order.setState(state);
            this.orderRepository.save(order);
            return success("Update order state success");
        } catch (MongoException e) {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage()); 
        }
    }
}
