package ioc;
import javax.inject.Singleton;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import dagger.Component;
import service.api.checkout.CheckoutService;
import service.api.tokenize.TokenizerService;

@Singleton
@Component(modules = { MainModule.class })
public interface ApplicationGraph {
    TokenizerService tokenizerService();

    CheckoutService checkoutService();
}
