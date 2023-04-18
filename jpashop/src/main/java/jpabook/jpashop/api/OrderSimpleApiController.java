package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderSimpleQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * ManyToOne, OneToOne 성능 최적화를 어떻게 하는지
 * Order 조회
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;

	//엔티티를 직접 노출하는 방법은 사용하면 안된다.
	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1() {
		List<Order> all = orderRepository.findAllByString(new OrderSearch());
		for (Order order : all) {
			order.getMember().getName();
			order.getDelivery().getAddress();
		}
		return all;
	}

	//최악의 경우 쿼리가 총 1 + N + N 번 실행되기 때문에 성능이 떨어진다.
	//오더 2개를 조회하기 위해 총 5번의 쿼리가 나갈 수 있다.
	@GetMapping("/api/v2/simple-orders")
	public List<SimpleOrderDto> OrdersV2() {
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
		return orders.stream()
			.map(o -> new SimpleOrderDto(o))
			.collect(Collectors.toList());
	}

	//fetch join 을 활용한 성능 최적화 (쿼리 한번에 조회)
	@GetMapping("/api/v3/simple-orders")
	public List<SimpleOrderDto> ordersV3() {
		List<Order> orders = orderRepository.findAllWithMemberDelivery();
		return orders.stream()
			.map(o -> new SimpleOrderDto(o))
			.collect(Collectors.toList());
	}

	//JPA에서 DTO로 바로 조회하기.
	//V3에 비해서 재사용성이 떨어진다.
	//성능면에서는 V3에 비해서 좋다.
	@GetMapping("/api/v4/simple-orders")
	public List<OrderSimpleQueryDto> ordersV4() {
		return orderRepository.findOrderDtos();
	}

	@Data
	static class SimpleOrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;

		public SimpleOrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName();
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress();
		}
	}
}
