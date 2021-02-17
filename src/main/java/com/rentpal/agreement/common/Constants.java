package com.rentpal.agreement.common;

/**
 * The Class Constants.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jun 22, 2020 4:59:59 PM
 * This class holds the constant fields
 */
public class Constants {

	/** The Constant SUCCESS. */
	public static final String SUCCESS="success";
	
	/** The Constant FAILED. */
	public static final String FAILED="failed ";

	public static final int MINDAYS=28;

	public static final String DEAD_LETTER_EXCHANGE="dlx";

	public static final String DEAD_LETTER_ROUTING_KEY="dlrk";

	public static final String DEAD_LETTER_QUEUE_NAME="dlq";

	public static final String DEFAULT_START_PAGE="0";

	public static final String DEFAULT_LOAD_SIZE="50";

	public static final String[] MONTHS={"Jan", "Feb", "Mar", "Apr", "May", "Jun",
										"Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};

	public static final int RENT_HISTORY=12;

	public static final int TOTAL_MONTHS=12;
}
