package at.ac.fhcampuswien.se.group1.carservice.event;

import at.ac.fhcampuswien.se.group1.carservice.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OrderStatusSuccessEvent {
    private String transactionId;
    private Order order;
}
