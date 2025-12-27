package sobes.shop;

import java.math.BigDecimal;

public class Purchase {
	
	private final long id;
	private final BigDecimal price;
	private BigDecimal priceWithDiscount;
	
	public Purchase(long id, BigDecimal price) {
		this.id = id;
		this.price = price;
		this.priceWithDiscount = price;
	}
	
	public long getId() {
		return id;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
	public BigDecimal getPriceWithDiscount() {
		return priceWithDiscount;
	}
	
	public void setPriceWithDiscount(BigDecimal priceWithDiscount) {
		this.priceWithDiscount = priceWithDiscount;
	}
}
