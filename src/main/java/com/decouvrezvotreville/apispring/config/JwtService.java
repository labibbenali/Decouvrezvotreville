package com.decouvrezvotreville.apispring.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static  final String SECRET_KEY="InLZd5JyF1hDGwA9rzv27GHvJUXRm61//jgmPDr/IE/" +
            "LAS27wv+k8Cf4/mzRGNosFdNNPphL7mDK7CJh3jLtpk3pqTMNSrkZ40GHwQZRTcs4yodsz+" +
            "YyM0vn+U+KNL48PSUhZ131bshZKK6k3BBOnqUS29idgDYNrzQbQOlwQDMOFEZ/G48yJpH/" +
            "cWrrzdP5Hq4cArsA4GuNHObyRzmSpo4VmrlGS9ktRO3yTGpFUG/27D46kmNMWkpMd6AKVvBF/" +
            "LtF++UWD46E60h2KD+SbehknbhOzanvArdmem9sTKkgz02FKZQAUeklIOSj9KIdXUYVJeXa+" +
            "dPgSttgtH1f2ooUwVl2HxE7IcbVydf8gHo=";


    public String extractUsername(String token) {
        return extractClaim(token , Claims::getSubject);

    }
    public <T> T extractClaim(String token , Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }
    public String generateToken(Map<String , Object> extraClaims, UserDetails userDetails){
        return  Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //set la periode de validité de token ici 24 heurs +1000 mili seconde
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                //the signingkey the secret that is used to sign jwt , it use
                // to verify the sender of jwt it who it claim to ensure  that message
                //not changed
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
