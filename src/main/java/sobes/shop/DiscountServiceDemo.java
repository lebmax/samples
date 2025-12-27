package sobes.shop;

import java.math.BigDecimal;

public class DiscountServiceDemo {
	
	public static void main(String[] args) {
		UserDiscountService userDiscountService = new UserDiscountServiceTest();
		DiscountService discountService = new DiscountService(userDiscountService);
		
		System.out.println("=== Пример 1: скидка 50%, товары 50р и 100р ===");
		Cart cart1 = new Cart();
		cart1.addPurchase(new Purchase(1, BigDecimal.valueOf(50)));
		cart1.addPurchase(new Purchase(2, BigDecimal.valueOf(100)));
		
		Cart result1 = discountService.applyDiscount(1, cart1);
		printCart(result1);
		
		System.out.println("\n=== Пример 2: скидка 10р, товары 20р и 30р ===");
		Cart cart2 = new Cart();
		cart2.addPurchase(new Purchase(3, BigDecimal.valueOf(20)));
		cart2.addPurchase(new Purchase(4, BigDecimal.valueOf(30)));
		
		Cart result2 = discountService.applyDiscount(2, cart2);
		printCart(result2);
		
		System.out.println("\n=== Пример 3: скидка 10р, товары 1р и 99р ===");
		Cart cart3 = new Cart();
		cart3.addPurchase(new Purchase(5, BigDecimal.valueOf(1)));
		cart3.addPurchase(new Purchase(6, BigDecimal.valueOf(99)));
		
		Cart result3 = discountService.applyDiscount(3, cart3);
		printCart(result3);
	}
	
	private static void printCart(Cart cart) {
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal finalTotal = BigDecimal.ZERO;
		
		for (Purchase p : cart.getItems()) {
			total = total.add(p.getPrice());
			finalTotal = finalTotal.add(p.getPriceWithDiscount());
			System.out.printf("  %s: %s руб -> %s руб (скидка: %s руб)%n",
							  p.getId(),
							  p.getPrice(),
							  p.getPriceWithDiscount(),
							  p.getPrice().subtract(p.getPriceWithDiscount()));
		}
		System.out.printf("Итого: %s руб -> %s руб (скидка: %s руб)%n",
						  total, finalTotal, total.subtract(finalTotal));
	}
}
