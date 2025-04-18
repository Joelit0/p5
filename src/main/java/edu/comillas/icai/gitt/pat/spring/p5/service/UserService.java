package edu.comillas.icai.gitt.pat.spring.p5.service;

import edu.comillas.icai.gitt.pat.spring.p5.entity.AppUser;
import edu.comillas.icai.gitt.pat.spring.p5.entity.Token;
import edu.comillas.icai.gitt.pat.spring.p5.model.ProfileRequest;
import edu.comillas.icai.gitt.pat.spring.p5.model.ProfileResponse;
import edu.comillas.icai.gitt.pat.spring.p5.model.RegisterRequest;
import edu.comillas.icai.gitt.pat.spring.p5.repository.TokenRepository;
import edu.comillas.icai.gitt.pat.spring.p5.repository.AppUserRepository;
import edu.comillas.icai.gitt.pat.spring.p5.util.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * TODO#6
 * Completa los métodos del servicio para que cumplan con el contrato
 * especificado en el interface UserServiceInterface, utilizando
 * los repositorios y entidades creados anteriormente
 */

@Service
public class UserService implements UserServiceInterface {
    private final AppUserRepository appUserRepository;
    private final TokenRepository tokenRepository;
    private final Hashing hashing;

    public UserService(AppUserRepository appUserRepository, TokenRepository tokenRepository) {
        this.appUserRepository = appUserRepository;
        this.tokenRepository = tokenRepository;
        this.hashing = new Hashing();
    }

    public Token login(String email, String password) {
        Optional<AppUser> appUser = this.appUserRepository.findByEmail(email);

        if (appUser.isEmpty()) return null;

        AppUser user = appUser.get();

        if (hashing.compare(user.getPassword(), password)) {
            Optional<Token> token = tokenRepository.findByAppUserId(user.getId());

            if (token.isPresent()) return token.get();

            Token newToken = new Token();
            newToken.setAppUser(user);

            return tokenRepository.save(newToken);
        } else {
            return null;
        }
    }

    public AppUser authentication(String tokenId) {
        Optional<Token> token = tokenRepository.findById(tokenId);

        if (token.isEmpty()) { return null; }

        return token.get().getAppUser();
    }

    public ProfileResponse profile(AppUser appUser) {
        return new ProfileResponse(
            appUser.getName(),
            appUser.getEmail(),
            appUser.getRole()
        );
    }

    public ProfileResponse profile(AppUser appUser, ProfileRequest profile) {
        appUser.setName(profile.name());
        appUser.setPassword(profile.password());
        appUser.setRole(profile.role());

        appUser = appUserRepository.save(appUser);

        return new ProfileResponse(
            appUser.getName(),
            appUser.getEmail(),
            appUser.getRole()
        );
    }

    public ProfileResponse profile(RegisterRequest register) {
        AppUser newUser = new AppUser();

        newUser.setName(register.name());
        newUser.setEmail(register.email());
        newUser.setRole(register.role());
        newUser.setPassword(hashing.hash(register.password()));

        AppUser savedUser = appUserRepository.save(newUser);

        return new ProfileResponse(
            savedUser.getName(),
            savedUser.getEmail(),
            savedUser.getRole()
        );
    }

    public void logout(String tokenId) {
        Optional<Token> token = tokenRepository.findById(tokenId);

        if (token.isPresent()) {
            tokenRepository.delete(token.get());
        }
    }

    public void delete(AppUser appUser) {
        Optional<AppUser> user = appUserRepository.findById(appUser.getId());

        if (user.isPresent()) {
            Optional<Token> token = tokenRepository.findByAppUserId(appUser.getId());

            if (token.isPresent()) {
                tokenRepository.delete(token.get());
            }

            appUserRepository.delete(appUser);
        }
    }

}
