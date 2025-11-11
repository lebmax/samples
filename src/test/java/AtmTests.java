import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import sobes.atm.ATM;
import sobes.atm.Balance;
import sobes.atm.Result;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class AtmTests {
	
	@Test
	public void testSuccessGetMoney() {
		Balance balance = new Balance(100);
		ATM atm = new ATM(balance);
		Result result = atm.getMoney(50);
		Map<Integer, Integer> numCells = result.getNumCells();
		Assertions.assertFalse(numCells.isEmpty());
		numCells.forEach(
				(numCell, count)  -> Assertions.assertEquals(0, numCell)
		);
		Assertions.assertEquals(50, balance.getSum());
	}
	
	@Test
	public void testSuccessGetMoney1() {
		Balance balance = new Balance(6000);
		ATM atm = new ATM(balance);
		Result result = atm.getMoney(1400);
		Map<Integer, Integer> numCells = result.getNumCells();
		Assertions.assertFalse(numCells.isEmpty());
	}
	
	@Test
	public void testFailedGetMoney() {
		Assertions.assertThrows(IllegalStateException.class, () -> {
			Balance balance = new Balance(50);
			ATM atm = new ATM(balance);
			atm.getMoney(100);
		});
	}
	
	@Test
	public void testIncorrectSum() {
		Assertions.assertThrows(IllegalStateException.class, () -> {
			Balance balance = new Balance(50);
			ATM atm = new ATM(balance);
			atm.getMoney(55);
		});
	}
}
