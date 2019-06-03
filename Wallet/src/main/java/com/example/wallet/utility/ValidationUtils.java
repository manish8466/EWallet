package com.example.wallet.utility;

public class ValidationUtils {

	private static final Long MOBILE_NUMBER_MIN_VALUE = 5999999999L;
	private static final Long MOBILE_NUMBER_MAX_VALUE = 9999999999L;

	public static boolean isValidPhoneNo(Long phone) {
		boolean result = false;
		if (phone.compareTo(MOBILE_NUMBER_MIN_VALUE) > 0 && phone.compareTo(MOBILE_NUMBER_MAX_VALUE) <= 0) {
			result = true;
		}
		return result;
	}

}
