package jpabook.jpashop;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitDb {

	private final InitService initService;

	@PostConstruct
	public void init() {
		initService.dbInit1();
		initService.dbInit2();
	}

	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService {
		private final EntityManager em;
		public void dbInit1() {
			Member member = createMember("userA", "학동로", "11111");
			em.persist(member);

			Book book1 = createBook("Clean Code", 10000, 100);
			em.persist(book1);

			Book book2 = createBook("Effective Java", 20000, 200);
			em.persist(book2);

			OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
			OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

			Delivery delivery = createDelivery(member);
			Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
			em.persist(order);
		}
		public void dbInit2() {
			Member member = createMember("userB", "압구정로", "22222");
			em.persist(member);

			Book book1 = createBook("Toby's Spring", 20000, 200);
			em.persist(book1);

			Book book2 = createBook("This is JAVA", 40000, 300);
			em.persist(book2);

			OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
			OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

			Delivery delivery = createDelivery(member);
			Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
			em.persist(order);
		}

		private static Delivery createDelivery(Member member) {
			Delivery delivery = new Delivery();
			delivery.setAddress(member.getAddress());
			return delivery;
		}

		private static Book createBook(String name, int price, int stockQuantity) {
			Book book1 = new Book();
			book1.setName(name);
			book1.setPrice(price);
			book1.setStockQuantity(stockQuantity);
			return book1;
		}

		private static Member createMember(String name, String street, String zipcode) {
			Member member = new Member();
			member.setName(name);
			member.setAddress(new Address("서울", street, zipcode));
			return member;
		}
	}
}

