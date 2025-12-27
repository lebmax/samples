package sobes.shop;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	
	private final List<Purchase> items;
	
	public Cart() {
		this(new ArrayList<>());
	}

	public Cart(List<Purchase> items) {
		this.items = new ArrayList<>(items);
	}
	
	public void addPurchase(Purchase purchase) {
		items.add(purchase);
	}
	
	public List<Purchase> getItems() {
		return List.copyOf(items);
	}
	
	public boolean isEmpty() {
		return items.isEmpty();
	}
}
