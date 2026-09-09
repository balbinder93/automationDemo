package framework.models;

/** Typed human-authored checkout fixture. */
public record CheckoutData(String country, String postalCode, boolean giftWrap) { }
