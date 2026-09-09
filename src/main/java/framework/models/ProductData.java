package framework.models;

/** Typed product fixture used by search and cart workflows. */
public record ProductData(String query, String productName, String expectedHomeHeading) { }
