package com.betacom.ecommerce.enums;

import java.util.Arrays;
import java.util.Optional;

public enum Supporto {
	CD, 
	Vinile, 
	MP3, 
	Video;

	public static Supporto safeValueOf(String value) {
		if (value == null)
			return null;

		return Arrays.stream(values())
				.filter(v -> v.name().equalsIgnoreCase(value))
				.findFirst()
				.orElse(null);
	}

}
