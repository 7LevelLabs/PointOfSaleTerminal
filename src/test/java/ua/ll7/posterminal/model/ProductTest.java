package ua.ll7.posterminal.model;

import org.junit.*;

import java.math.BigDecimal;

/**
 * PointOfSaleTerminal
 * 11.09.13 : 17:15
 * Alex Velichko
 * alex.velichko.kyiv@gmail.com
 */
public class ProductTest extends Assert {

	private Product product;

	@Before
	public void setUp() throws Exception {
		Character pc = 'A';
		product = new Product(pc);
		product.writePriceForQuantity(1, BigDecimal.valueOf(10.00));
		product.writePriceForQuantity(5, BigDecimal.valueOf(25.00));
		product.writePriceForQuantity(7, BigDecimal.valueOf(50.00));
	}

	@After
	public void tearDown() throws Exception {
		product = null;
	}

	@Test
	public void testCalcProductTotal() throws Exception {
		BigDecimal bd1 = product.calcProductTotal(5);
		BigDecimal bd2 = BigDecimal.valueOf(25.00);
		bd2 = bd2.setScale(2,BigDecimal.ROUND_HALF_DOWN);

		assertTrue(bd1.equals(bd2));

		bd1 = product.calcProductTotal(9);
		bd2 = BigDecimal.valueOf(70.00);
		bd2 = bd2.setScale(2,BigDecimal.ROUND_HALF_DOWN);

		assertTrue(bd1.equals(bd2));
	}

	@Test
	public void testAreTherePriceForQuantity() throws Exception {
		assertTrue(product.areTherePriceForQuantity(1));
		assertTrue(!product.areTherePriceForQuantity(15));
	}

	@Test
	public void testWritePriceForQuantity() throws Exception {
		BigDecimal bd1 = BigDecimal.valueOf(150.00);
		product.writePriceForQuantity(7, bd1);
		assertTrue(product.getPrices().get(7).equals(bd1));
	}

	@Test
	public void testGetProductCode() throws Exception {
		assertTrue(product.getProductCode()=='A');
	}

	@Test
	public void testEquals() throws Exception {
		Product product1 = new Product('A');
		assertTrue(this.product.equals(product1));
		product1 = null;

		product1 = new Product('B');
		assertTrue(!this.product.equals(product1));
	}

	@Test
	public void testToString() throws Exception {
		String assertString = "Product { productCode=A, prices={1=10.0, 5=25.0, 7=50.0}}";
		assertTrue(product.toString().equalsIgnoreCase(assertString));
	}
}
