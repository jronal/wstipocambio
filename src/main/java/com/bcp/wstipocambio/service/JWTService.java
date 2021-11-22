package com.bcp.wstipocambio.service;

import com.bcp.wstipocambio.exception.TokenInvalidHeaderException;
import com.bcp.wstipocambio.exception.TokenNotFoundException;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public interface JWTService {

    KeyPair loadKeyPairFromProperties(byte[] prvKeyEncoded, byte[] pubKeyEncoded) throws NoSuchAlgorithmException, InvalidKeySpecException;

    KeyPair generateKeyPair(SignatureAlgorithm signatureAlgorithm);

    String getPublicKey();

    String getToken(String nuDni, String audience);

    String extractToken(String authorizationHeader) throws TokenInvalidHeaderException, TokenNotFoundException;

    Map<String, String> parseToken(String token);

    String getUsername(String authorizationHeader);

    Map<String, String> parseToken(String token, String audience);
}
