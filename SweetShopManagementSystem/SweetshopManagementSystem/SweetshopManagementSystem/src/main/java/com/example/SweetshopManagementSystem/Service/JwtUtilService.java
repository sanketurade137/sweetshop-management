package com.example.SweetshopManagementSystem.Service;


import com.example.SweetshopManagementSystem.Entity.User;
import com.example.SweetshopManagementSystem.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtilService {
   private final UserRepository  userRepository;

    public JwtUtilService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String key=null;
    public String getSecreateKey(){
        key="dGhpcyBpcyBhIHNlY3JldCBrZXkgZm9yIGp3dCBhdXRoZW50aWNhdGlvbiBpbiBzcHJpbmcgYm9vdA==";
        return key;
    }


    public String generateToken(User user){
        Map<String ,Object> claims=new HashMap<>();
       // claims.put("role", user.getRole());
        user =userRepository.findByUserName(user.getUserName()).get();
        String role = user.getRole();

        return Jwts.builder()
                .setSubject(user.getUserName())            // username
                .claim("role", role)             // ✅ add role as single claim
                .setIssuer("Akhil")                        // issuer
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10 min
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public SecretKey generateKey(){

        byte[] x = Decoders.BASE64.decode(getSecreateKey());

        return Keys.hmacShaKeyFor(x);

    }


    public String extractUserName(String token) {

        return extractsClaims(token, Claims::getSubject);
    }

    private <T>T extractsClaims(String token, Function<Claims,T> claimResolver) {
        Claims claims=extractClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().verifyWith(generateKey()).build().parseSignedClaims(token).getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName=extractUserName(token);

        return (userName.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpriration(token).before(new Date());
    }

    private Date extractExpriration(String token) {
        return extractsClaims(token,Claims::getExpiration);
    }
}
