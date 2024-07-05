package com.vitor.dscommerce.repositories;

import com.vitor.dscommerce.entities.OrderItem;
import com.vitor.dscommerce.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
}
