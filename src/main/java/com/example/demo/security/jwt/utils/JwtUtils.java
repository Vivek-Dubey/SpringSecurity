package com.example.demo.security.jwt.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.example.demo.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtils {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String AUTHORIZATION_HEADER_VALUE_PREFIX = "Bearer";
	public static final String CLAIM_USER_ID = "userId";
	public static final String CLAIM_USER_NAME = "username";
	
	private String SECRET_KEY = "secret";

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Object extractClaimByKey(String token, String key) {
		final Claims claims = extractAllClaims(token);
		return claims.get(key);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(User userDetails) {
		Map<String, Object> claims = getClaims(userDetails);
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() +1000*60)) // 10 hours
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
	
	private Map<String, Object> getClaims(User userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_USER_ID, new Long(userDetails.getId()));
		claims.put(CLAIM_USER_NAME, new String(userDetails.getUsername()));
		return claims;
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		//return !isTokenExpired(token)  && isTokenActive(token);
	}

//	private boolean isTokenActive(String token) {
//		int tokenStatus = sessionService.getTokenStatusByToken(token);
//		return tokenStatus == RowStatus.ACTIVE.getValue();
//	}
	
//	public synchronized String refreshToken(String expiredToken,LoraxUser user) {
//		try {
//			sessionService.expireToken(expiredToken);
//			String refreshedToken = generateToken(user);
//			sessionService.create(user, refreshedToken);
//			return refreshedToken;
//		}catch(NotFoundException nfe) {
//			// swallow this
//			throw nfe;
//		}
//	}
	
	
	
}
