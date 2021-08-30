package nextstep.jwp.controller;

import nextstep.jwp.controller.request.RegisterRequest;
import nextstep.jwp.exception.DuplicateAccountException;
import nextstep.jwp.http.common.HttpStatus;
import nextstep.jwp.http.request.HttpRequest;
import nextstep.jwp.http.response.HttpResponse;
import nextstep.jwp.service.RegisterService;
import nextstep.jwp.service.StaticResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterController extends RestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService, StaticResourceService staticResourceService) {
        super(staticResourceService);
        this.registerService = registerService;
    }

    @Override
    protected HttpResponse doPost(HttpRequest httpRequest) {
        try {
            RegisterRequest registerRequest = getRegisterRequest(httpRequest);
            registerService.registerUser(registerRequest);

            LOGGER.debug("Register Success.");

            return HttpResponse.redirect(HttpStatus.MOVED_PERMANENTLY, "/index.html");
        } catch (DuplicateAccountException e) {
            LOGGER.debug("Register Failed.");

            return HttpResponse.redirect(HttpStatus.CONFLICT, "/409.html");
        }
    }

    private RegisterRequest getRegisterRequest(HttpRequest httpRequest) {
        String account = httpRequest.getBodyParameter("account");
        String password = httpRequest.getBodyParameter("password");
        String email = httpRequest.getBodyParameter("email");

        LOGGER.debug("Register Request => account: {}, email: {}", account, email);

        return new RegisterRequest(account, password, email);
    }

    @Override
    public boolean matchUri(String uri) {
        return uri.startsWith("/register");
    }
}
