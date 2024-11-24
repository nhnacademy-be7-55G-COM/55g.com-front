package shop.s5g.front.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class ApacheRandomStringUtilCaller implements RandomStringProvider{
    private final RandomStringUtils provider = RandomStringUtils.secure();

    @Override
    public String nextString() {
        return provider.nextAlphanumeric(45, 65);
    }
}
