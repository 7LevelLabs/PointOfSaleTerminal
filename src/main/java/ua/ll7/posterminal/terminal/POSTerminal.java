package ua.ll7.posterminal.terminal;

import ua.ll7.posterminal.api.POSTerminalAPI;
import ua.ll7.posterminal.model.Product;

import java.math.BigDecimal;
import java.util.*;

/**
 * PointOfSaleTerminal
 * 10.09.13 : 22:58
 * Alex Velichko
 * alex.velichko.kyiv@gmail.com
 */
public class POSTerminal implements POSTerminalAPI {

	/**
	 *
	 * Terminal id
	 * */
	private final String terminalID;

	/**
	 *
	 * Products {@link Product} - codes, prices (for packs), etc.
	 * */
	private Map<Character,Product> products;

	/**
	 *
	 * Accumulate orders
	 * */
	private String order;

	/**
	 *
	 * Oder total behind (may be - multiple) {@link #scan(String)} and {@link #resetOders()} (i.e. {@link #calculateTotal()})
	 * */
	private BigDecimal orderTotal;

	/**
	 *
	 * Errors list.
	 * */
	private ArrayList<String> lastErrors;

	/**
	 *
	 * Constructor. Without parameters, as is in the requirements.
	 * */
	public POSTerminal () {
		this.orderTotal = BigDecimal.valueOf(0);
		this.products = new TreeMap<Character, Product>();
		this.order = "";

		this.lastErrors = new ArrayList<String>();

		UUID idOne = UUID.randomUUID();
		this.terminalID = idOne.toString();
	}

	@Override
	public BigDecimal calculateTotal() {
		BigDecimal res = BigDecimal.valueOf(0);
		Product product;

		Map<Character,Integer> orderChars = this.getOrderChars();

		Integer pi;

		for (Character pch : orderChars.keySet()) {

			pi = orderChars.get(pch);

			if (this.products.get(pch)!=null) {
				//calc total for this product
				product = this.products.get(pch);
				res = res.add(product.calcProductTotal(pi));
			}
		}
		this.resetOders();
		return res;
	}

	@Override
	public void scan(String order) {
		if (order == null) {
			throw new NullPointerException("Arguments can't be null.");
		}
		if (order.length() == 0) {
			throw new IllegalArgumentException("Arguments can't be empty.");
		}

		StringBuffer output = new StringBuffer();

		output.append(this.order);
		output.append(order);
		this.order = output.toString();
	}

	@Override
	public void setPricing(Character productCode, int productQuantity, float productPackPrice) {
		Product product = null;
		product = this.locateProductByCode(productCode);

		if (product==null) {
			product = new Product(productCode);
			product.writePriceForQuantity(productQuantity, BigDecimal.valueOf(productPackPrice));

			products.put(productCode, product);
		} else {
			product.writePriceForQuantity(productQuantity, BigDecimal.valueOf(productPackPrice));
		}
	}

	@Override
	public boolean isReadyForGetTotal() {
		boolean res = true;

		this.getLastErrors().clear();

		Set<Character> orderChars = this.getOrderChars().keySet();
		Iterator<Character> iterator = orderChars.iterator();

		Character ch = null;
		Product p = null;

		while (iterator.hasNext()) {
			ch = iterator.next();
			p = this.locateProductByCode(ch);
			if (p==null) {
				//write ch to lastErrors
				this.getLastErrors().add(ERR_PRODUCT_ABSENT+ch);
				res = false;
			}
			if (p != null) {
				if (!p.getPrices().containsKey(1)) {
					//write ch to lastErrors
					this.getLastErrors().add(ERR_PRODUCT_1_PRICE_ABSENT+ch);
					res = false;
				}
			}
			p = null;
			ch = null;
		}
		return res;
	}

	@Override
	public ArrayList<String> getLastErrors() {
		return this.lastErrors;
	}

	/**
	 *
	 * Locate Product among known
	 * @param codeToSearch Code product to search
	 * @return {@link Product}, where {@link Product#productCode}==codeToSearch , or null
	 * */
	public Product locateProductByCode(Character codeToSearch) {
		return this.products.get(codeToSearch);
	}

	/**
	 *
	 * Reset the {@link #order} and {@link #orderTotal} at the end of {@link #calculateTotal()}.<br />
	 * */
	private void resetOders() {
		this.orderTotal = BigDecimal.valueOf(0);
		this.order=null;
		this.order="";
		this.lastErrors.clear();
	}

	private Map<Character,Integer> getOrderChars () {
		Map<Character,Integer> orderChars = new TreeMap<Character, Integer>();

		int i;

		for (i = 0; i < this.order.length(); i++) {

			Character ch = this.order.charAt(i);

			if (orderChars.containsKey(ch)) {
				Integer integer = orderChars.get(ch)+1;
				orderChars.remove(ch);
				orderChars.put(ch,integer);
			} else {
				orderChars.put(ch,1);
			}
		}
		return  orderChars;
	}

	/**
	 *
	 * To check known products
	 * */
	public Map<Character,Product> getProducts() {
		return products;
	}

	public String getTerminalID() {
		return terminalID;
	}

	public String getOrder() {
		return order;
	}

	/**
	 *
	 * The uniqueness of the terminal is determined by {@link #terminalID}
	 * */
	@Override
	public int hashCode() {
		return Objects.hash(terminalID);
	}

	/**
	 *
	 * The uniqueness of the terminal is determined by {@link #terminalID}
	 * */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final POSTerminal other = (POSTerminal) obj;
		return Objects.equals(this.terminalID, other.terminalID);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("POSTerminal { ");
		sb.append("terminalID='").append(terminalID).append('\'');
		sb.append(", products=").append(products);
		sb.append(", order='").append(order).append('\'');
		sb.append(", orderTotal=").append(orderTotal);
		sb.append('}');
		return sb.toString();
	}

	/**
	 *
	 * Error code : Product is absent
	 * */
	public final static String ERR_PRODUCT_ABSENT = "P ? : ";

	/**
	 *
	 * Error code : Product price for 1-item-pack is absent
	 * */
	public final static String ERR_PRODUCT_1_PRICE_ABSENT = "P-1P ? : ";

}
