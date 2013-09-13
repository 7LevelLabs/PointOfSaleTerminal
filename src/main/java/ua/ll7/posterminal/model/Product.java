package ua.ll7.posterminal.model;

/**
 * PointOfSaleTerminal
 * 10.09.13 : 22:17
 * Alex Velichko
 * alex.velichko.kyiv@gmail.com
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * The Product. Store {@link #productCode} and {@link #prices}.
 * */
public class Product implements Serializable, Comparable<Product>{
	/**
	 *
	 * Product code, just one char - as is in the requirements.
	 * */
	private final Character productCode;

	/**
	 *
	 * Product prices
	 * */
	private Map<Integer,BigDecimal> prices;

	public Product(Character productCode) {
		prices = new TreeMap<Integer, BigDecimal>();
		this.productCode = productCode;
	}

	/**
	 *
	 * What is the maximum possible amount of the next box, based on the current product price list?
	 * */
	private int calcBiggestFeasible (int currentQuantity, Set<Integer> productPackCapacitySet) {
		int bf = 0;

		int i;

		for (i = currentQuantity; i >= 1; i--) {
			if (productPackCapacitySet.contains(i)) {
				bf = i;
				return bf;
			}
		}
		return bf;
	}

	/**
	 *
	 * Calc total for given Product quantity
	 * */
	public BigDecimal calcProductTotal (int productQuantity) {
		BigDecimal res = BigDecimal.valueOf(0);

		int i = productQuantity;

		int currentProductPackCapacity;

		while (i > 0) {

			currentProductPackCapacity = this.calcBiggestFeasible(
				i,
				prices.keySet());

			//increment product-total

			res = res.add(prices.get(currentProductPackCapacity));

			//decrement work product quantity
			i = i - currentProductPackCapacity;
		}

		res = res.setScale(2,BigDecimal.ROUND_HALF_DOWN);
		return res;
	}

	/**
	 *
	 * Are there price for specified quantity of the product?
	 * */
	public boolean areTherePriceForQuantity (int productPackQuantity) {
		return prices.containsKey(productPackQuantity);
	}

	/**
	 *
	 * Write price for specified quantity:<br />
	 * if this is new quantity - put new item,<br />
	 * if existent one - delete old & put new, with the same key ("update", terminal reprogramming)
	 * */
	public void writePriceForQuantity (int productPackQuantity, BigDecimal price) {
		if (price==null) {
			throw new NullPointerException("Arguments can't be null");
		}

		// (price==0) - up to upper system level

		// (productPackQuantity==0) - up to upper system level

		if (!prices.containsKey(productPackQuantity)) {
			prices.put(productPackQuantity,price);
		}

		else {
			prices.remove(productPackQuantity);
			prices.put(productPackQuantity,price);
		}

	}

	public Character getProductCode() {
		return productCode;
	}

	public Map<Integer, BigDecimal> getPrices() {
		return prices;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Product product = (Product) o;

		if (!productCode.equals(product.productCode)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return productCode.hashCode();
	}

	@Override
	public int compareTo(Product o) {
		int res = 0;
		if (o.getProductCode()>this.getProductCode()) {
			res = 1;
		}
		if (o.getProductCode()<this.getProductCode())  {
			res=-1;
		}
		return res;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Product { ");
		sb.append("productCode=").append(productCode);
		sb.append(", prices=").append(prices);
		sb.append('}');
		return sb.toString();
	}
}
