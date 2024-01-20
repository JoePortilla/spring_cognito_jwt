package co.cuencas.usersadmin.security.jwt;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.interfaces.RSAKeyProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class AwsCognitoRSAKeyProvider implements RSAKeyProvider {
    private final URL awsStoreUrl;
    private final JwkProvider provider;

    /**
     * Method to obtain the URL of the provider
     */
    public AwsCognitoRSAKeyProvider(String jwtUrl, String awsCognitoRegion, String identityPoolUrl) {
        String url = String.format(jwtUrl, awsCognitoRegion, identityPoolUrl);
        try {
            awsStoreUrl = new URL(url);
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(String.format("Invalid URL provided, URL=%s", url));
        }
        // Build the URL of the provider
        provider = new JwkProviderBuilder(awsStoreUrl).build();
    }


    /**
     * Method to obtain the key from the provider
     *
     * @param keyId the KeyId specified in the Token's Header or null if none is available.
     *              Provides a hint on which Public Key to use to verify the token's signature.
     * @return Key obtained from provider for use in JWT
     */
    @Override
    public RSAPublicKey getPublicKeyById(String keyId) {
        try {
            return (RSAPublicKey) provider.get(keyId).getPublicKey();
        }
        catch (JwkException e) {
            throw new RuntimeException(String.format("Failed to get JWT kid=%s from aws_kid_store_url=%s", keyId, awsStoreUrl));
        }
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return null;
    }

    @Override
    public String getPrivateKeyId() {
        return null;
    }
}
