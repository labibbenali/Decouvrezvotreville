package com.decouvrezvotreville.apispring.services;

import com.decouvrezvotreville.apispring.entities.ConfirmationToken;
import com.decouvrezvotreville.apispring.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token){
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token){
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now()
        );
    }
    public Optional<ConfirmationToken> findTokenByUser(String email){
        return confirmationTokenRepository.findByUser(email);
    }

    public int updateExpiredAt(String token , LocalDateTime expiredAt){
        return confirmationTokenRepository.updateExpiredAt(token, expiredAt);
    }
}
