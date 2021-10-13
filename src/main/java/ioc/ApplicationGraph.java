package ioc;

import javax.inject.Singleton;
import dagger.Component;
import service.api.checkout.CheckoutService;
import service.api.tokenize.TokenizerService;

/**
 * Dagger component for providing the needed dependencies.
 */
@Singleton
@Component(modules = { MainModule.class })
public interface ApplicationGraph {
    TokenizerService tokenizerService();

    CheckoutService checkoutService();
}
