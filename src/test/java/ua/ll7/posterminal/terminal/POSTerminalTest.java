package ua.ll7.posterminal.terminal;

import org.junit.*;

import java.util.ArrayList;

/**
 * PointOfSaleTerminal
 * 11.09.13 : 20:36
 * Alex Velichko
 * alex.velichko.kyiv@gmail.com
 */
public class POSTerminalTest extends Assert {

	private POSTerminal terminal;

	@Before
	public void setUp() throws Exception {
		this.terminal = new POSTerminal();

		this.terminal.setPricing('A',1,10);
		this.terminal.setPricing('A',2,7);
		this.terminal.setPricing('B',1,7);
		this.terminal.setPricing('B',5,25);
		this.terminal.setPricing('C',1,4);
		this.terminal.setPricing('D',3,2);

		terminal.scan("FAADZBBAC");
		terminal.scan("ABZBAC");

	}

	@After
	public void tearDown() throws Exception {
		this.terminal = null;
	}

	@Test
	public void testScan() throws Exception {
		String assertString = "FAADZBBACABZBAC";
		assertTrue(assertString.equals(this.terminal.getOrder()));
	}

	@Test
	public void testIsReadyForGetTotal() throws Exception {
		boolean isReady = this.terminal.isReadyForGetTotal();
		assertTrue(!isReady);
	}

	@Test
	public void testGetLastErrors() throws Exception {
		//last errors
		ArrayList<String> lastErrors;

		String assertString1, assertString2, assertString3;

		//we have to know future
		this.terminal.isReadyForGetTotal();

		//last errors
		lastErrors = this.terminal.getLastErrors();

		assertString1 = "[P-1P ? : D, P ? : F, P ? : Z]";
		assertTrue(assertString1.equalsIgnoreCase(lastErrors.toString()));

		//Product D, adding 1-item-pack price
		this.terminal.setPricing('D',1,1);

		this.terminal.isReadyForGetTotal();
		lastErrors = this.terminal.getLastErrors();

		assertString2 = "[P ? : F, P ? : Z]";
		assertTrue(assertString2.equalsIgnoreCase(lastErrors.toString()));

		//Product Z, registering product
		this.terminal.setPricing('Z',1,21);

		this.terminal.isReadyForGetTotal();
		lastErrors = this.terminal.getLastErrors();

		assertString3 = "[P ? : F]";
		assertTrue(assertString3.equalsIgnoreCase(lastErrors.toString()));


	}
}
