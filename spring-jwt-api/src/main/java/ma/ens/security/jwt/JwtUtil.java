package ma.ens.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_VALUE = "MySuperSecretKeyForJwtAuthentication123456";
    private static final long TOKEN_DURATION = 60 * 60 * 1000; // 1 heure

    private final Key signingKey;

    public JwtUtil() {
        this.signingKey = Keys.hmacShaKeyFor(SECRET_VALUE.getBytes());
    }

    public String generateToken(String username) {
        long now = System.currentTimeMillis();
        Date issuedDate = new Date(now);
        Date expiryDate = new Date(now + TOKEN_DURATION);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedDate)
                .setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        Claims claims = decodeToken(token);
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            decodeToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expiré");
        } catch (UnsupportedJwtException e) {
            System.out.println("Token non supporté");
        } catch (MalformedJwtException e) {
            System.out.println("Token mal formé");
        } catch (SignatureException e) {
            System.out.println("Signature invalide");
        } catch (IllegalArgumentException e) {
            System.out.println("Token vide ou incorrect");
        }
        return false;
    }

    private Claims decodeToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}