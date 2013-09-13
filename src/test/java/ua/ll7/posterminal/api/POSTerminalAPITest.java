package ua.ll7.posterminal.api;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.ll7.posterminal.terminal.POSTerminal;

import java.math.BigDecimal;

/**
 * PointOfSaleTerminal
 * 10.09.13 : 22:14
 * Alex Velichko
 * alex.velichko.kyiv@gmail.com
 */
public class POSTerminalAPITest extends Assert {

	private POSTerminal terminal = null;

	@Before
	public void setUp() throws Exception {
		this.terminal = new POSTerminal();
		this.terminal.setPricing('A',1,1.25f);
		this.terminal.setPricing('A',3,3f);
		this.terminal.setPricing('B',1,4.25f);
		this.terminal.setPricing('C',1,1f);
		this.terminal.setPricing('C',6,5f);
		this.terminal.setPricing('D',1,0.75f);
	}

	@After
	public void tearDown() throws Exception {
		this.terminal=null;
	}

	@Test
	public void testCalculateTotal() throws Exception {

		BigDecimal bdRes;

		this.terminal.scan("ABCDABA");

		BigDecimal bd1 = BigDecimal.valueOf(13.25);
		bd1 = bd1.setScale(2,BigDecimal.ROUND_HALF_DOWN);

		bdRes = terminal.calculateTotal();

		assertTrue(bd1.equals(bdRes));

		this.terminal.scan("CCCCCCC");

		BigDecimal bd2 = BigDecimal.valueOf(6.00);
		bd2 = bd2.setScale(2,BigDecimal.ROUND_HALF_DOWN);

		bdRes = terminal.calculateTotal();

		assertTrue(bd2.equals(bdRes));

		this.terminal.scan("ABCD");

		BigDecimal bd3 = BigDecimal.valueOf(7.25);
		bd3 = bd3.setScale(2,BigDecimal.ROUND_HALF_DOWN);
		bdRes = terminal.calculateTotal();

		assertTrue(bd3.equals(bdRes));
	}
}
