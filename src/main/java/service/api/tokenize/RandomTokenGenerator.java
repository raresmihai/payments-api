package service.api.tokenize;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomTokenGenerator implements TokenGenerator{
    @Override
    public String generate() {
        return RandomStringUtils.randomAlphanumeric(32);
    }
}
