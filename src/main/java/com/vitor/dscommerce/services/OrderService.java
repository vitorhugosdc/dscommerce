package com.vitor.dscommerce.services;

import com.vitor.dscommerce.dto.OrderDTO;
import com.vitor.dscommerce.dto.OrderItemDTO;
import com.vitor.dscommerce.entities.Order;
import com.vitor.dscommerce.entities.OrderItem;
import com.vitor.dscommerce.entities.OrderStatus;
import com.vitor.dscommerce.entities.Product;
import com.vitor.dscommerce.repositories.OrderItemRepository;
import com.vitor.dscommerce.repositories.OrderRepository;
import com.vitor.dscommerce.repositories.ProductRepository;
import com.vitor.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        Order order = new Order();
        order.setMoment(Instant.now());
        /*Por padrão, o pedido vai estar aguardando pagamento*/
        order.setStatus(OrderStatus.WAITING_PAYMENT);
        /*O usuário será o usuário atualmente autenticado*/
        order.setClient(userService.authenticated());

        /*Basicamente do DTO a gente só precisa os items do pedido, o resto pode ser instanciado na hora, com ofeito acima*/
        for (OrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.getReferenceById(itemDTO.getProductId());
            /*Note que é o preço do PRODUTO, não do DTO (que nem vai ser recebido na requisição)*/
            order.getItems().add(new OrderItem(order, product, itemDTO.getQuantity(), product.getPrice()));
        }
        order = repository.save(order);
        /*Tem que salvar os itens de pedido também, se não ao buscar o pedido (Order) não vai retornar nada*/
        orderItemRepository.saveAll(order.getItems());
        return new OrderDTO(order);
    }
}
