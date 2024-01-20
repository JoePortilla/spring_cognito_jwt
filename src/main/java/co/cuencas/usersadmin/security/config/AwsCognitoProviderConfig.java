package co.cuencas.usersadmin.security.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración del proveedor (aws cognito)
 */
@Configuration
public class AwsCognitoProviderConfig {
    @Value(value = "${aws.access.key}")
    private String accessKey;
    @Value(value = "${aws.secret.key}")
    private String secretKey;
    @Value(value = "${aws.cognito.region}")
    private String region;

    @Bean
    public AWSCognitoIdentityProvider awsCognitoIdentityProvider() {
        // Credenciales de usuario IAM para poder utilizar los servicios de AWS
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        return AWSCognitoIdentityProviderClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(region)
                .build();
    }
}