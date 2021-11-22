package com.bcp.wstipocambio.service.impl;

import com.bcp.wstipocambio.exception.TokenInvalidHeaderException;
import com.bcp.wstipocambio.exception.TokenNotFoundException;
import com.bcp.wstipocambio.model.Token;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.bcp.wstipocambio.service.JWTService;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JWTServiceImpl implements JWTService {

    KeyPair keyPair;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss");

    SignatureAlgorithm signatureAlgorithm;

    @Value("${jwt.prvKey}")
    String prvKey;

    @Value("${jwt.pubKey}")
    String pubKey;

    @Value("${jwt.expDateClaimInMinutes}")
    String expDateClaimInMinutes;

    @Value("${jwt.clockSkewInMinutes}")
    String clockSkewInMinutes;

    @Value("${jwt.enableCompression}")
    String enableCompression;

    @PostConstruct
    public void init() {
        signatureAlgorithm = SignatureAlgorithm.ES256;
        // keyPair = generateKeyPair(signatureAlgorithm);
        final byte[] prvKeyEncoded = DatatypeConverter.parseBase64Binary(prvKey);
        final byte[] pubKeyEncoded = DatatypeConverter.parseBase64Binary(pubKey);
        try {
            keyPair = loadKeyPairFromProperties(prvKeyEncoded, pubKeyEncoded);
        } catch (Exception e) {
            throw new IllegalStateException("key pair can't be loaded from properties", e);
        }
        log.info("jwt data loaded");
    }

    @Override
    public KeyPair loadKeyPairFromProperties(byte[] prvKeyEncoded, byte[] pubKeyEncoded) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // see unit Test to verify
        // https://stackoverflow.com/a/22992310/2692914
        final KeyFactory factory = KeyFactory.getInstance("EC");

        final PKCS8EncodedKeySpec prvSpec = new PKCS8EncodedKeySpec(prvKeyEncoded);
        final PrivateKey privateKey = factory.generatePrivate(prvSpec);
        // System.out.println(privateKey);

        final X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubKeyEncoded);
        final PublicKey publicKey = factory.generatePublic(pubSpec);
        // System.out.println(publicKey);
        return new KeyPair(publicKey, privateKey);
    }

    @Override
    public KeyPair generateKeyPair(SignatureAlgorithm signatureAlgorithm) {
        // generate a new key every time
        return Keys.keyPairFor(signatureAlgorithm);
    }

    @Override
    public String getPublicKey() {
        return Encoders.BASE64.encode(keyPair.getPublic().getEncoded());
    }

    @Override
    public String getToken(String nuDni, String audience) {
        final Token token = buildToken(nuDni, audience);

        final JwtBuilder builder = Jwts.builder();
        if (Boolean.parseBoolean(enableCompression)) builder.compressWith(CompressionCodecs.GZIP);
        return builder
                // https://tools.ietf.org/html/rfc7519#section-4.1.1
                .setIssuer(token.getDeIssuer())
                .setSubject(token.getDeSubject())
                .setAudience(token.getDeAudience())
                .setExpiration(token.getFeExpiration())
                .setNotBefore(token.getFeNotBefore())
                .setIssuedAt(token.getFeIssuedAt())
                .signWith(keyPair.getPrivate(), signatureAlgorithm).compact();
    }

    private Token buildToken(String deSubject, String deAudience) {
        final LocalDateTime now = LocalDateTime.now();
        final Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        final LocalDateTime exp = now.plusMinutes(Integer.parseInt(expDateClaimInMinutes));
        final Date expDate = Date.from(exp.atZone(ZoneId.systemDefault()).toInstant());
        final Token token = new Token();
        token.setInEstado("1");
        token.setDeIssuer("FPS");
        token.setDeSubject(deSubject);
        token.setDeAudience(deAudience);
        token.setFeExpiration(expDate);
        token.setFeNotBefore(nowDate);
        token.setFeIssuedAt(nowDate);
        return token;
    }

    @Override
    public String extractToken(String authorizationHeader) throws TokenInvalidHeaderException, TokenNotFoundException {
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            final String[] authorizationHeaderParts = authorizationHeader.split(" ");
            if (authorizationHeaderParts.length == 2 && "Bearer".equalsIgnoreCase(authorizationHeaderParts[0]) && !authorizationHeaderParts[1].isEmpty()) {
                return authorizationHeaderParts[1];
            } else {
                throw new TokenInvalidHeaderException("invalid authorization header: " + authorizationHeader);
            }
        } else {
            throw new TokenNotFoundException("authorization header not found");
        }
    }

    @Override
    public Map<String, String> parseToken(String token) {
        return parseToken(token, null);
    }

    @Override
    public String getUsername(String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        Map<String, String> parseTokenMap = parseToken(token);
        return parseTokenMap.get("sub");
    }

    @Override
    public Map<String, String> parseToken(String token, String audience) {
        final Jws<Claims> jws;
        try {
            final JwtParserBuilder jwtParserBuilder = Jwts.parserBuilder()
                    .setAllowedClockSkewSeconds(Integer.parseInt(clockSkewInMinutes) * 60L)
                    .setSigningKey(keyPair.getPublic());
            if (audience != null && !audience.isEmpty()) {
                // claim assertion
                jwtParserBuilder.requireAudience(audience);
            }
            jws = jwtParserBuilder
                    .build()
                    .parseClaimsJws(token);
            log.info("{}", jws);

            Map<String, String> claims = new HashMap<>();
            claims.put("iss", jws.getBody().getIssuer());
            claims.put("sub", jws.getBody().getSubject());
            claims.put("aud", jws.getBody().getAudience());
            claims.put("exp", sdf.format(jws.getBody().getExpiration()));
            claims.put("nbf", sdf.format(jws.getBody().getNotBefore()));
            claims.put("iat", sdf.format(jws.getBody().getIssuedAt()));
            claims.put("jti", jws.getBody().getId());
            return claims;

        } catch (ExpiredJwtException e) {
            throw e;
        } catch (MissingClaimException mce) {
            // the parsed JWT did not have the sub field
            //log.error("JWT required claim not found");
            throw mce;
        } catch (IncorrectClaimException ice) {
            // the parsed JWT had an asserted field, but its value was not equal to required
            //log.error("JWT claim assertion failed");
            throw ice;
        } catch (JwtException e) {
            // we *cannot* use the JWT as intended by its creator
            //log.error("JWT is fake or null");
            throw e;
        }
    }
}
