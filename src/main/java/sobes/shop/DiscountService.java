package sobes.shop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Вы — backend-разработчик в интернет-магазине.
 * Дела идут в гору и магазин решил повысить лояльность покупателей, предоставляя им персональные скидки.
 * К вам обратился product owner с задачей создать простую систему лояльности,
 * которая предоставляет процентную или фиксированную скидку на корзину.
 * Размер скидки зависит от покупателя.
 * Аналитики уже определили, какие скидки должны быть предоставлены покупателям.
 * <p>
 * ## Определения
 * <p>
 * Корзина - список покупок покупателя.
 * <p>
 * Покупка:
 * - id товара
 * - цена
 * - итоговая стоимость c учетом скидки
 * <p>
 * ## Задача
 * Написать часть новой системы лояльности, которая применяет скидку покупателя к корзине.
 * - на вход получает id покупателя и корзину
 * - вычисляет и применяет скидки
 * - возвращает корзину, в которой учтены скидки
 * - все расчёты в рублях, точность до копеек. Округления по математическим правилам
 * <p>
 * Есть два вида скидки (не могут прийти одновременно):
 * - процент от суммы корзины
 * - фиксированная скидка
 * <p>
 * Сумма скидки распределяется равномерно между товарами, пропорционально цене товара.
 * <p>
 * Примеры:
 * - скидка 50%, в корзине товары на 50р и 100р. Скидка 25р и 50р
 * - скидка 10р, в корзине товары на 20р и 30р. Скидка 4р и 6р
 * - скидка 10р, в корзине товары на 1р и 99р. Скидка 10коп и 9р 90коп
 */
public class DiscountService {
	
	private static final int SCALE = 2; // точность до копеек
	private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP; // математическое округление
	
	private final UserDiscountService userDiscountService;
	
	public DiscountService(UserDiscountService userDiscountService) {
		this.userDiscountService = userDiscountService;
	}
	
	public Cart applyDiscount(long userId, Cart cart) {
		if (cart.isEmpty()) {
			return cart;
		}
		
		Discount discount = userDiscountService.getDiscount(userId);
		if (discount == null) {
			return cart;
		}
		
		List<Purchase> purchases = cart.getItems();
		BigDecimal totalCartPrice = calculateCartTotalPrice(purchases);
		
		// Вычисляем общую сумму скидки
		BigDecimal totalDiscount = calculateTotalDiscount(discount, totalCartPrice);
		
		// Если скидка >= суммы корзины, все товары бесплатны
		if (totalDiscount.compareTo(totalCartPrice) >= 0) {
			purchases.forEach(p -> p.setPriceWithDiscount(BigDecimal.ZERO));
			return cart;
		}
		
		// Распределяем скидку пропорционально ценам товаров
		for (Purchase purchase : purchases) {
			BigDecimal percent = totalDiscount.divide(BigDecimal.valueOf(100), SCALE, ROUNDING_MODE);
			BigDecimal itemDiscount = purchase.getPrice().multiply(percent);
			BigDecimal finalPrice = purchase.getPrice().subtract(itemDiscount);
			// Защита от отрицательных цен
			if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
				finalPrice = BigDecimal.ZERO;
			}
			purchase.setPriceWithDiscount(finalPrice.setScale(SCALE, ROUNDING_MODE));
		}
		
		return cart;
	}
	
	/**
	 * Вычисляет общую стоимость корзины
	 */
	private BigDecimal calculateCartTotalPrice(List<Purchase> purchases) {
		return purchases.stream()
						.map(Purchase::getPrice)
						.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	/**
	 * Вычисляет общую сумму скидки
	 */
	private BigDecimal calculateTotalDiscount(Discount discount, BigDecimal totalPrice) {
		var discountValue = BigDecimal.valueOf(discount.value());
		if (discount.discountType() == DiscountType.FIX) {
			return discountValue
					.divide(totalPrice, SCALE, ROUNDING_MODE)
					.multiply(BigDecimal.valueOf(100));
		} else {
			return discountValue;
		}
	}
}
