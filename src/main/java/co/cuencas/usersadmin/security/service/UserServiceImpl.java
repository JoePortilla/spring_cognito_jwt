package co.cuencas.usersadmin.security.service;

import co.cuencas.usersadmin.security.dto.SignInDto;
import co.cuencas.usersadmin.security.dto.SignUpDto;
import co.cuencas.usersadmin.security.entity.UserApp;
import co.cuencas.usersadmin.security.entity.UserDetailsImpl;
import co.cuencas.usersadmin.security.enums.Role;
import co.cuencas.usersadmin.security.repository.UserRepository;
import co.cuencas.usersadmin.security.service.UseCase.RoleService;
import co.cuencas.usersadmin.security.service.UseCase.UserService;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final AWSCognitoIdentityProvider awsCognitoIdentityProvider;

    @Value(value = "${aws.cognito.clientId}")
    private String clientId;

    @Override
    public void signUpUser(SignUpDto signUpDto) {
        // Generate register request
        SignUpRequest signUpRequest = new SignUpRequest();
        // Build email attribute
        AttributeType attributeType = new AttributeType().withName("email")
                                                         .withValue(signUpDto.getEmail());
        // Fill the register request
        signUpRequest.withClientId(clientId)
                     .withUsername(signUpDto.getIdentification())
                     .withPassword(signUpDto.getPassword())
                     .withUserAttributes(attributeType);
        // Send register request to cognito
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
        userRepository.save(user);
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
    public Optional<UserApp> getUserByIdentification(String userIdentification) {
        return userRepository.findByIdentification(userIdentification);
    }

    /**
     * Method to load the user by its username
     */
    @Override
    public UserDetails loadUserByUsername(String identification) throws UsernameNotFoundException {
        // Search for the user in the database
        UserApp user = getUserByIdentification(identification)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con la c.c.:" +
                                                                         " " + identification));
        // Build the user details with their username (i.e.: identification) and their authorizations.
        return UserDetailsImpl.buildUserDetails(user);
    }
}
