package hcmute.edu.watchstore.service.impl;

import java.io.UnsupportedEncodingException;
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
import hcmute.edu.watchstore.dto.request.BuyNowRequest;
import hcmute.edu.watchstore.dto.request.OrderRequest;
import hcmute.edu.watchstore.dto.response.OrderResponse;
import hcmute.edu.watchstore.dto.response.OrderResponseV2;
import hcmute.edu.watchstore.dto.response.ProductItemResponse;
import hcmute.edu.watchstore.dto.response.UserResp;
import hcmute.edu.watchstore.entity.Cart;
import hcmute.edu.watchstore.entity.Order;
import hcmute.edu.watchstore.entity.Product;
import hcmute.edu.watchstore.entity.ProductItem;
import hcmute.edu.watchstore.entity.User;
import hcmute.edu.watchstore.helper.payment.PaymentService;
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
                OrderResponse response = new OrderResponse(order);
                response.setProductItems(this.productItemService.findProductItemResponse(order.getOrderItems()));
                userOrder.add(response);
            }
        }
        return success(userOrder);
    }

    @Override
    public ResponseEntity<?> createOrder(OrderRequest order, ObjectId userId) throws UnsupportedEncodingException {
        Optional<User> currentUser = this.userRepository.findById(userId);

        if (!currentUser.isPresent()) {
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
        }

        Order newOrder = new Order(
            new ObjectId(),
            order.getProductItem(),
            order.getAddress(),
            (order.getPaymentMethod() == null) ? "cash" : order.getPaymentMethod(),
            calculatorItemsPrice(order.getProductItem()),
            30000,
            0,
            userId,
            order.getPaymentMethod().contains("vnpay"),
            (order.getPaymentMethod().contains("vnpay")) ? new Date() : null,
            false,
            null,
            new Date(),
            "processing"
        );

        newOrder.setTotalPrice(newOrder.getItemsPrice() + newOrder.getShippingPrice());
        if (order.getPaymentMethod().contains("vnpay")) {
            newOrder.setPaid(true);
            newOrder.setPaidAt(new Date());
        }
        try {
            this.orderRepository.save(newOrder);
            handleManageOrderUser(order.getProductItem(), userId, "create");
            List<ObjectId> orderUser = currentUser.get().getOrder();
            orderUser.add(newOrder.getId());
            currentUser.get().setOrder(orderUser);
            this.userRepository.save(currentUser.get());
            if (order.getPaymentMethod().contains("vnpay")) {
                return success(PaymentService.createPayment(newOrder));
            }
            return success("Create order success !!!");
        } catch (MongoException e) {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> cancelOrderr(ObjectId orderId, ObjectId userId) {
        Optional<Order> order = this.orderRepository.findById(orderId);
        if (!order.isPresent()) {
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
        }

        if (order.get().getState().equals("shipping")) {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), "This order is shipping");
        }

        try {
            if (order.get().getState().equals("cancel") || order.get().getState().equals("processing")) {
                this.productItemService.deleteItemAdvance(order.get().getOrderItems(), true);
            }
            else
                this.productItemService.deleteItemAdvance(order.get().getOrderItems(), false);

            this.orderRepository.delete(order.get());

            User user = this.userRepository.findById(userId).orElse(null);
            List<ObjectId> orders = user.getOrder();
            orders.remove(orderId);
            user.setOrder(orders);

            this.userRepository.save(user);
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
                    if (product.getState().equals("outOfStock")) {
                        product.setState("saling");
                    }
                }
                if (message.equals("create")) {
                    amount = amount - item.getQuantity();
                    if (amount < 0) {
                        amount = 0;
                        product.setState("outOfStock");
                    }
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

    public double calculatorItemsPrice(List<ObjectId> productItem) {
        List<ProductItemResponse> responses = this.productItemService.findProductItemResponse(productItem);
        double totalPrice = 0;

        for (ProductItemResponse resp : responses) {
            totalPrice += resp.getProduct().getPrice() * (100 - resp.getProduct().getDiscount()) / 100 * resp.getQuantity();
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
            if (state.equals("cancel")) {
                this.productItemService.cancelItem(order.getOrderItems());
            }
            order.setState(state);
            this.orderRepository.save(order);
            return success("Update order state success");
        } catch (MongoException e) {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage()); 
        }
    }

    @Override
    public List<ObjectId> deleteOrder(List<ObjectId> orderIds) {
        List<Order> orders = this.orderRepository.findAll();
        List<Order> userOrder = new ArrayList<>();
        List<ObjectId> itemDelete = new ArrayList<>();

        if (orders.isEmpty()) {
            return null;
        }

        for(ObjectId id : orderIds) {
            Order o = findItem(id, orders);
            if (!o.getState().equals("Shipping")) {
                itemDelete.addAll(o.getOrderItems());
                userOrder.add(o);
            }
        }

        try {
            this.orderRepository.deleteAllById(orderIds);
            return itemDelete;
        } catch (MongoException e) {
            return itemDelete;
        }
    }

    @Override
    public boolean isUserOrderShipping(ObjectId userId) {
        List<Order> orders = this.orderRepository.findByUser(userId);
        return orders.stream().anyMatch(order -> order.getState().equals("shipping"));
    }

    @Override
    public ResponseEntity<?> approvalOrder(ObjectId orderId) {
        Optional<Order> order = this.orderRepository.findById(orderId);

        if (!order.isPresent()) {
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
        }

        Order isPresentOrder = order.get();
        isPresentOrder.setState("shipping");

        try {
            this.orderRepository.save(isPresentOrder);
            return success("Order is approval");
        } catch (MongoException e) {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAll() {
        List<Order> orders = this.orderRepository.findAll();

        List<OrderResponse> orderResp = new ArrayList<>();

        for(Order o : orders) {
            OrderResponse resp = new OrderResponse(o);
            orderResp.add(resp);
        }

        return success(orderResp);
    }

    @Override
    public ResponseEntity<?> isOrderDelivered(ObjectId orderId, ObjectId userId) {
        Optional<Order> order = this.orderRepository.findById(orderId);
        
        if (!order.isPresent()) {
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
        }

        Order isPresentOrder = order.get();
        isPresentOrder.setDelivered(true);
        isPresentOrder.setDeliveredAt(new Date());
        isPresentOrder.setState("complete");

        try {
            this.orderRepository.save(isPresentOrder);
            return success("Order is delivered");
        } catch (Exception e) {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getOrderDetail(ObjectId orderId) {
        Optional<Order> order = this.orderRepository.findById(orderId);

        if (!order.isPresent()) {
            return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
        }

        OrderResponseV2 response = new OrderResponseV2(order.get());
        response.setProductItems(this.productItemService.findProductItemResponse(order.get().getOrderItems()));

        Optional<User> user = this.userRepository.findById(order.get().getUser());
        if (user.isPresent()) {
            UserResp userResp = new UserResp(user.get());
            response.setUser(userResp);
        }

        return success(response);
    }

    @Override
    public ResponseEntity<?> buyNow(BuyNowRequest request, ObjectId userId) throws UnsupportedEncodingException  {
        Optional<User> currentUser = this.userRepository.findById(userId);
        ObjectId itemId = new ObjectId();
        ProductItem item = new ProductItem();
        item.setId(itemId);
        item.setProduct(request.getProduct());
        item.setQuantity(request.getQuantity());

        this.productItemService.saveOrEditItem(item);
        Product product = this.productService.findProduct(request.getProduct());
        List<ObjectId> listItem = new ArrayList<>();
        listItem.add(itemId);
        
        Order newOrder = new Order(
            new ObjectId(),
            listItem,
            request.getAddress(),
            (request.getPaymentMethod() == null) ? "cash" : request.getPaymentMethod(),
            product.getPrice() * (100 - product.getDiscount()) / 100 * request.getQuantity(),
            30000,
            product.getPrice() * (100 - product.getDiscount()) / 100 * request.getQuantity() + 30000,
            userId,
            request.getPaymentMethod().contains("vnpay"),
            (request.getPaymentMethod().contains("vnpay")) ? new Date() : null,
            false,
            null,
            new Date(),
            "processing"
        );

        try {
            this.orderRepository.save(newOrder); // save order

            List<ObjectId> orderUser = currentUser.get().getOrder();
            orderUser.add(newOrder.getId());
            currentUser.get().setOrder(orderUser);
            this.userRepository.save(currentUser.get()); // update orders user

            int amount = product.getAmount() - request.getQuantity();
            if (amount < 0) {
                amount = 0;
                product.setState("ofOutStock");
            }
            product.setAmount(amount);
            this.productService.saveOrUpdate(product); // update amount product

            if (request.getPaymentMethod().contains("vnpay")) {
                return success(PaymentService.createPayment(newOrder));
            }
            return success("Create order success !!!");
        } catch (MongoException e) {
            return error(ResponseCode.ERROR_IN_PROCESSING.getCode(), ResponseCode.ERROR_IN_PROCESSING.getMessage());
        }
    }
}
