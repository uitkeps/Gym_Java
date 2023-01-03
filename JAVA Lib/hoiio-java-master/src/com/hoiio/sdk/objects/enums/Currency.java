package com.hoiio.sdk.objects.enums;

/*
Copyright (C) 2012 Hoiio Pte Ltd (http://www.hoiio.com)

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
*/

import java.util.HashMap;
import java.util.Map;

public enum Currency {

	SGD("SGD"),
	HKD("HKD"),
	USD("USD"),
	AUD("AUD");
	
	private static final Map<String, Currency> lookup = new HashMap<String, Currency>();
	
	private String currency;
	
	private Currency(String currency) {
		this.currency = currency;
	}
	
	/**
	 * Returns a string representation of the object
	 * @return string representation of the object
	 */
	public String toString() {
		return currency;
	}
	
	static {
		for (Currency s : Currency.values()) {
			lookup.put(s.toString(), s);
		}
	}
	
	/**
	 * Converts the string to {@code Currency} object
	 * @param currency The currency in string
	 * @return {@code Currency} object
	 */	
	public static Currency fromString(String currency) {
		return lookup.get(currency);
	}
	
}
