package framework.data;

import framework.models.UniqueUser;
import java.util.UUID;

/** Generates collision-resistant data per invocation for parallel tests. */
public final class UniqueTestDataFactory {
    private UniqueTestDataFactory() { }
    public static UniqueUser user(String emailDomain) {
        String token = UUID.randomUUID().toString();
        return new UniqueUser("user-" + token, "automation+" + token + "@" + emailDomain, token);
    }
    public static String orderReference() { return "order-" + UUID.randomUUID(); }
}
