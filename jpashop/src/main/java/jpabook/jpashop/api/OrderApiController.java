package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

	private final OrderRepository orderRepository;

	//엔티티 직접 노출 - 잘못된 방식
	@GetMapping("/api/v1/orders")
	public List<Order> ordersV1() {
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
		for (Order order : orders) {
			order.getMember().getName();
			order.getDelivery().getAddress();
			List<OrderItem> orderItems = order.getOrderItems();
			orderItems.stream().forEach(o -> o.getItem().getName());
		}
		return orders;
	}

	@GetMapping("/api/v2/orders")
	public List<OrderDto> ordersV2() {
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
		return orders.stream()
			.map(o -> new OrderDto(o))
			.collect(Collectors.toList());
	}

	@Getter
	static class OrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
		private List<OrderItemDto> orderItems;

		public OrderDto(final Order order) {
			orderId = order.getId();
			name = order.getMember().getName();
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress();
			orderItems = order.getOrderItems().stream()
				.map(orderItem -> new OrderItemDto(orderItem))
				.collect(Collectors.toList());
		}
	}

	@Getter
	static class OrderItemDto {

		private String itemName;
		private int orderPrice;
		private int count;

		public OrderItemDto(final OrderItem orderItem) {
			itemName = orderItem.getItem().getName();
			orderPrice = orderItem.getOrderPrice();
			count = orderItem.getCount();
		}
	}
}