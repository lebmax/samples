package sobes.shop;

public class UserDiscountServiceTest implements UserDiscountService {
	@Override
	public Discount getDiscount(long userId) {
		return switch ((int) userId) {
			case 1 -> new Discount(DiscountType.PERCENT, 50);
			case 2, 3 -> new Discount(DiscountType.FIX, 10);
			default -> null;
		};
	}
}
