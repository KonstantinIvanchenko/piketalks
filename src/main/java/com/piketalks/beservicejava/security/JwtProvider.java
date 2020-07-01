package com.piketalks.beservicejava.security;

import com.piketalks.beservicejava.exceptions.PiketalksException;
import com.piketalks.beservicejava.model.User;
import com.piketalks.beservicejava.utils.ConsoleCredentials;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

import static io.jsonwebtoken.Jwts.parser;

@Service
public class JwtProvider {

    private KeyStore keyStore;
    private String jksPass;
    @Value("${jwt.expiration.time}")
    private Long jwtExpirationTimeInMillis;

    @Value("${jks.path}")
    private String jksPath;
    @Value("${jks.private.name}")
    private String jksPrivateKeyName;

    public String generateToken(Authentication authentication){
        //Object p = authentication.getPrincipal();
        //User principal = (User) p;

        /*
                return Jwts.builder().
                setSubject(principal.getUsername()).
                signWith(getPrivateKey()).
                compact();
         */

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return  Jwts.builder()
                .setSubject(userDetails.getUsername())
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTimeInMillis)))
                .compact();
    }

    public String generateTokenWithUserName(String username){
        return  Jwts.builder()
                .setSubject(username)
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTimeInMillis)))
                .compact();
    }

    @PostConstruct
    public void init(){
        try{
            jksPass = new ConsoleCredentials().consoleReadPassword();
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream(jksPath);
            keyStore.load(resourceAsStream, jksPass.toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e){
            throw new PiketalksException("Exception occured while loading keystore");
        }
    }

    private PrivateKey getPrivateKey() {
        try{
            Key k = keyStore.getKey(jksPrivateKeyName, jksPass.toCharArray());
            return (PrivateKey) k;

        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e){
            throw new PiketalksException("Error occured while retrieving private key from keystore");
        }
    }

    public boolean validateToken(String jwt){
        parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() {
        try{
            Key k = keyStore.getCertificate(jksPrivateKeyName).getPublicKey();
            return (PublicKey) k;

        } catch (KeyStoreException e){
            throw new PiketalksException("Error occured while retrieving public certificate from keystore");
        }
    }

    public String getUsernameFromJwt(String jwt){
        Claims claims = parser().
                setSigningKey(getPublicKey()).
                parseClaimsJws(jwt).
                getBody();

        return claims.getSubject();
    }

    public Long getJwtExpirationTimeInMillis(){
        return jwtExpirationTimeInMillis;
    }
}
