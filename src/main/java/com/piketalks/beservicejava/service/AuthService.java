package com.piketalks.beservicejava.service;

import com.piketalks.beservicejava.dto.AuthenticationResponse;
import com.piketalks.beservicejava.dto.LoginRequest;
import com.piketalks.beservicejava.dto.RefreshTokenRequest;
import com.piketalks.beservicejava.dto.RegisterRequest;
import com.piketalks.beservicejava.exceptions.PiketalksException;
import com.piketalks.beservicejava.model.NotificationEmail;
import com.piketalks.beservicejava.model.User;
import com.piketalks.beservicejava.model.VerificationToken;
import com.piketalks.beservicejava.repository.UserRepository;
import com.piketalks.beservicejava.repository.VerificationTokenRepository;
import com.piketalks.beservicejava.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    /* FIXED: used constructor injection instead of direct dependency injection
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
     */

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setUsername(registerRequest.getUsername());
        user.setCreatedDate(Instant.now());
        user.setEnabled(false);

        //save created user into the DB
        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendEmail(new NotificationEmail(user.getEmail(),
                "Please activate your registered account",
                "Dear " + user.getUsername() + " - Use below link to get activate your account:"+
                        "http://localhost:8080/api/auth/accountVerification/"+token
                ));
    }

    @Transactional
    private String generateVerificationToken(User user){
        //random 128-bit value
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        //save created token into the DB
        verificationTokenRepository.save(verificationToken);

        return token;
    }

    @Transactional
    public void verifyAccount(String token){
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new PiketalksException("Invalid Token"));

        String username = verificationToken.get().getUser().getUsername();
        //User user = userRepository.findByUserName(userName).
        //        orElseThrow(() -> new PiketalksException("Username not found " + userName));

        List<User> users = userRepository.findByUsername(username);
                        //orElseThrow(() -> new PiketalksException("Username not found " + userName));

        if (users.isEmpty())
            throw  new PiketalksException("Username not found " + username);
        User user = users.get(0);
        user.setEnabled(true);
        userRepository.save(user);
    }

    public boolean isUserPresent(RegisterRequest registerRequest){
        String username = registerRequest.getUsername();
        List<User> user = userRepository.findByUsername(username);
        if (user.isEmpty())
            return false;
        return true;
    }

    public User getCurrentUser(){
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<User> user = userRepository.findByUsername(userName);
        if (user.isEmpty())
            return null;
        return user.get(0);
    }

    public AuthenticationResponse login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationTimeInMillis()))
                .userName(loginRequest.getUsername())
                .build();

        //return new AuthenticationResponse(token, loginRequest.getUsername());
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());

        //Get username from refresh token, as current authentication context may have already expired
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());

        return AuthenticationResponse.builder()
                .authenticationToken(token) //newly generated token
                .refreshToken(refreshTokenRequest.getRefreshToken()) // return back refreshToken as well
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationTimeInMillis()))
                .userName(refreshTokenRequest.getUsername())
                .build();
    }
}
