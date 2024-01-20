package co.cuencas.usersadmin.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtProvider {
    @Value(value = "${aws.cognito.identityPoolUrl}")
    private String identityPoolUrl;

    @Value(value = "${aws.cognito.region}")
    private String region;

    @Value(value = "${aws.cognito.issuer}")
    private String issuer;

    @Value(value = "${aws.cognito.jwk}")
    private String jwtUrl;

    // Dentro del payload devuelto viene un claim llamado username (i.e.: "username": "108533294")
    private static final String USERNAME = "username";

    /**
     * Validate the JWT Token
     *
     * @param token JWT Token
     * @return status of validation
     */
    public boolean validateToken(String token) {
        try {
            // Get a verified and decoded JWT
            getDecodedJwt(token);
            // If the JWT is valid return true
            return true;
        }
        catch (JWTVerificationException exception) {
            log.error("Validate token failed: " + exception.getMessage());
        }
        return false;
    }

    /**
     * Get the username (i.e.: identification) of the token
     *
     * @param token JWT
     * @return username
     */
    public String getUserNameFromToken(String token) {
        // Get a verified and decoded JWT
        DecodedJWT decodedJWT = getDecodedJwt(token);
        // Get the attribute USERNAME from the claim (i.e.: identification) and convert to string
        String userName = decodedJWT.getClaim(USERNAME).toString();
        // Delete the quotation mark in the attribute "username"
        return userName.replace("\"", "");
    }

    /**
     * Decode JWT to verify it and obtain the information.
     *
     * @param token JWT
     * @return A verified and decoded JWT
     */
    public DecodedJWT getDecodedJwt(String token) {
        // Split the String and remove Bearer (if exists)
        String tokenWithoutBearer = token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token;
        // Obtain the URL of the provider
        RSAKeyProvider keyProvider = new AwsCognitoRSAKeyProvider(jwtUrl, region, identityPoolUrl);
        // Define the Algorithm used by the supplier
        Algorithm algorithm = Algorithm.RSA256(keyProvider);
        // Build a verifier with the specified algorithm and provider
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
        // Verify and return the verified token
        return verifier.verify(tokenWithoutBearer);
    }
}

