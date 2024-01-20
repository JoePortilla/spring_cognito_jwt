package co.cuencas.usersadmin.security.service;

import co.cuencas.usersadmin.security.dto.SignInDto;
import co.cuencas.usersadmin.security.dto.SignUpDto;
import co.cuencas.usersadmin.security.entity.UserApp;
import co.cuencas.usersadmin.security.enums.Role;
import co.cuencas.usersadmin.security.repository.UserRepository;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final AWSCognitoIdentityProvider awsCognitoIdentityProvider;

    @Value(value = "${aws.cognito.clientId}")
    private String clientId;

    @Override
    public void createUser(UserApp user) {
        userRepository.save(user);
    }

    @Override
    public void signUpUser(SignUpDto signUpDto) {
        AttributeType attributeType = new AttributeType().withName("email")
                                                         .withValue(signUpDto.getEmail());

        // Generate register request
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.withClientId(clientId)
                     .withUsername(signUpDto.getIdentification())
                     .withPassword(signUpDto.getPassword())
                     .withUserAttributes(attributeType);
        // Pass register request to cognito
        awsCognitoIdentityProvider.signUp(signUpRequest);

        // Create new user for save it in our db
        UserApp user = new UserApp();
        user.setIdentification(signUpDto.getIdentification());
        // TODO: ¿Es necesario guardar la contraseña?
        user.setEmail(signUpDto.getEmail());
        user.setFullName(signUpDto.getFullName());
        user.setPhone(signUpDto.getPhone());
        user.setCreatedDate(OffsetDateTime.now(ZoneId.of("America/Bogota")));
        user.setOrganization(signUpDto.getOrganization());
        user.setUserRole(roleService.findByRole(Role.ROLE_USER).get());
        // Save in db
        createUser(user);
    }

    @Override
    public String signInUser(SignInDto signInDto) {
        // authParams: Stores the log parameters that must be sent to AWS for authentication.
        final Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", signInDto.getIdentification());
        authParams.put("PASSWORD", signInDto.getPassword());
        // Create a request to AWS to start authentication
        InitiateAuthRequest initiateAuthRequest = new InitiateAuthRequest()
                .withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .withClientId(clientId)
                .withAuthParameters(authParams);
        // Start the authentication
        InitiateAuthResult initiateAuthResult = awsCognitoIdentityProvider.initiateAuth(initiateAuthRequest);
        // Returns the access token
        // Options: initiateAuthResult.getAuthenticationResult().getRefreshToken() / .getExpiresIn() / .getIdToken()
        return initiateAuthResult.getAuthenticationResult().getAccessToken();
    }

    @Override
    public Optional<UserApp> findUserByIdentification(String userIdentification) {
        return userRepository.findByIdentification(userIdentification);
    }
}
