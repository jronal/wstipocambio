package com.bcp.wstipocambio;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.EllipticCurveProvider;
import org.junit.Test;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class TestECKeys {
    @Test
    public void testGenerateECKeyPair() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        final KeyPair keyPair = EllipticCurveProvider.generateKeyPair(SignatureAlgorithm.ES256);

        final byte[] pubEncoded = keyPair.getPublic().getEncoded();
        Files.write(Paths.get("pub.key"), pubEncoded);
        Files.write(Paths.get("pub.key.base64"), DatatypeConverter.printBase64Binary(pubEncoded).getBytes());

        final byte[] prvEncoded = keyPair.getPrivate().getEncoded();
        Files.write(Paths.get("prv.key"), prvEncoded);
        Files.write(Paths.get("prv.key.base64"), DatatypeConverter.printBase64Binary(prvEncoded).getBytes());
        System.out.println("Ok");

        // https://stackoverflow.com/a/22992310/2692914
        final KeyFactory factory = KeyFactory.getInstance("EC");
        final PKCS8EncodedKeySpec prvSpec = new PKCS8EncodedKeySpec(prvEncoded);
        final PrivateKey privateKey = factory.generatePrivate(prvSpec);
        System.out.println(privateKey);

        final X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubEncoded);
        final PublicKey publicKey = factory.generatePublic(pubSpec);
        System.out.println(publicKey);
    }

    @Test
    public void testGenerateECKeyPairPureJava() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, IOException, InvalidKeySpecException {
        // code extracted from io.jsonwebtoken.impl.crypto.EllipticCurveProvider
        final SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(new byte[64]);
        final ECGenParameterSpec spec = new ECGenParameterSpec("secp256r1");  // secp384r1, secp521r1
        final KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
        generator.initialize(spec, secureRandom);
        final KeyPair keyPair = generator.generateKeyPair();

        final byte[] pubEncoded = keyPair.getPublic().getEncoded();
        Files.write(Paths.get("pub.key"), pubEncoded);
        Files.write(Paths.get("pub.key.base64"), DatatypeConverter.printBase64Binary(pubEncoded).getBytes());

        final byte[] prvEncoded = keyPair.getPrivate().getEncoded();
        Files.write(Paths.get("prv.key"), prvEncoded);
        Files.write(Paths.get("prv.key.base64"), DatatypeConverter.printBase64Binary(prvEncoded).getBytes());
        System.out.println("Ok");

        // https://stackoverflow.com/a/22992310/2692914
        final KeyFactory factory = KeyFactory.getInstance("EC");
        final PKCS8EncodedKeySpec prvSpec = new PKCS8EncodedKeySpec(prvEncoded);
        final PrivateKey privateKey = factory.generatePrivate(prvSpec);
        System.out.println(privateKey);

        final X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubEncoded);
        final PublicKey publicKey = factory.generatePublic(pubSpec);
        System.out.println(publicKey);
    }
}
