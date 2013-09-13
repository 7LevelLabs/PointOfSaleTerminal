package ua.ll7.posterminal.api;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * PointOfSaleTerminal
 * 10.09.13 : 22:07
 * Alex Velichko
 * alex.velichko.kyiv@gmail.com
 */
public interface POSTerminalAPI {

	/**
	 *
	 * The required part of the API
	 * */
	public BigDecimal calculateTotal();

	/**
	 *
	 * The required part of the API<br />
	 * Here we use String, as is in the requirements:<br />
	 * <blockquote>Scan these items in this order: ABCDABA;<blockquote/>
	 * */
	public void scan (String order);

	/**
	 *
	 * The required part of the API<br />
	 * Set (write) product pricing
	 * @param productCode product code. We have to use Character, as in the requirements.
	 * @param productQuantity Quantity of products in one pack. May be 1. 0 - is up to upper system level.
	 * @param productPackPrice Price for such product pack. 0 - is up to upper system level.
	 * */
	public void setPricing (Character productCode, int productQuantity, float productPackPrice);

	/**
	 *
	 * If the terminal not know about anything in the order, or <br />
	 * if the terminal not know price for quantity=1 in the pack
	 * return false.
	 * */
	public boolean isReadyForGetTotal ();

	/**
	 *
	 * Get last errors, if any.
	 * @return ArrayList<String> of errors descriptors (if any errors from scanning orders) or empty list (i.e. with 0 size).<br />
	 * <strong>Achtung!</strong> Never returns null, if errors are absent - result will be just empty.
	 * */
	public ArrayList<String> getLastErrors ();

}
