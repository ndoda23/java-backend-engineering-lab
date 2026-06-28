package org.example.springeshopapi.service;

import org.example.springeshopapi.dto.UserDTO;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.springeshopapi.dto.OrderDTO;
import org.example.springeshopapi.dto.OrderItemDTO;
import org.example.springeshopapi.model.*;
import org.example.springeshopapi.repository.OrderRepository;
import org.example.springeshopapi.repository.ProductRepository;
import org.example.springeshopapi.repository.UserRepository;
import org.example.springeshopapi.dto.OrderRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrderDTO createOrder(OrderRequest orderRequest){
        Order order = new Order();
       User user = userRepository.findById(orderRequest.getUserId())
               .orElseThrow(()-> new RuntimeException("User not found with id:"+orderRequest.getUserId()));

        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal totalOrderPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for(OrderRequest.OrderItemRequest itemReq : orderRequest.getItems()){
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(()->new RuntimeException("Product not found with id: "+itemReq.getProductId()));

            if(product.getStock() < itemReq.getQuantity()){
                throw new RuntimeException("Not enough stock for product:"+product.getTitle());
            }
            product.setStock(product.getStock()- itemReq.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemReq.getQuantity());
            BigDecimal itemPrice = product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            orderItem.setPrice(itemPrice);
            orderItems.add(orderItem);
            totalOrderPrice = totalOrderPrice.add(itemPrice);
        }


        order.setTotalPrice(totalOrderPrice);
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        OrderDTO dto = new OrderDTO();
        dto.setId(savedOrder.getId());
        dto.setTotalPrice(savedOrder.getTotalPrice());
        dto.setStatus(savedOrder.getStatus());
        dto.setCreatedAt(savedOrder.getCreatedAt());
        dto.setUserEmail(savedOrder.getUser().getEmail());

        List<OrderItemDTO> itemDTOs = new ArrayList<>();
        for (OrderItem item : savedOrder.getItems()) {
            OrderItemDTO itemDto = new OrderItemDTO();
            itemDto.setProductTitle(item.getProduct().getTitle());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());
            itemDTOs.add(itemDto);
        }
        dto.setItems(itemDTOs);

        return dto;



    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrderHistory(Long userId) {
         List<Order> orders = orderRepository.findByUserId(userId);
         List<OrderDTO> orderDTOs = new ArrayList<>();

         for(Order order: orders){
             OrderDTO orderDTO = new OrderDTO();
             orderDTO.setId(order.getId());
             orderDTO.setUserEmail(order.getUser().getEmail());
             orderDTO.setTotalPrice(order.getTotalPrice());
             orderDTO.setStatus(order.getStatus());
             orderDTO.setCreatedAt(order.getCreatedAt());
             List<OrderItemDTO> itemDTOs = new ArrayList<>();

             for(OrderItem item:order.getItems()){
                 OrderItemDTO itemDTO = new OrderItemDTO();
                 itemDTO.setProductTitle(item.getProduct().getTitle());
                 itemDTO.setQuantity(item.getQuantity());
                 itemDTO.setPrice(item.getPrice());
                 itemDTOs.add(itemDTO);
             }
             orderDTO.setItems(itemDTOs);
             orderDTOs.add(orderDTO);
         }

        return orderDTOs;
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new RuntimeException("Order not found with id: "+orderId));

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        OrderDTO dto = new OrderDTO();
        dto.setId(updatedOrder.getId());
        dto.setUserEmail(updatedOrder.getUser().getEmail());
        dto.setTotalPrice(updatedOrder.getTotalPrice());
        dto.setStatus(updatedOrder.getStatus());
        dto.setCreatedAt(updatedOrder.getCreatedAt());

        List<OrderItemDTO> itemDTOs = new ArrayList<>();
        for(OrderItem item: updatedOrder.getItems()){
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setProductTitle(item.getProduct().getTitle());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getPrice());
            itemDTOs.add(itemDTO);
        }
        dto.setItems(itemDTOs);
        return dto;
    }
}
