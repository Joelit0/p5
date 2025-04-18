package edu.comillas.icai.gitt.pat.spring.p5.repository;

import edu.comillas.icai.gitt.pat.spring.p5.entity.AppUser;
import edu.comillas.icai.gitt.pat.spring.p5.entity.Token;
import edu.comillas.icai.gitt.pat.spring.p5.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class RepositoryIntegrationTest {
    @Autowired TokenRepository tokenRepository;
    @Autowired AppUserRepository appUserRepository;

    /**
     * TODO#9
     * Completa este test de integración para que verifique
     * que los repositorios TokenRepository y AppUserRepository guardan
     * los datos correctamente, y las consultas por AppToken y por email
     * definidas respectivamente en ellos retornan el token y usuario guardados.
     */
    @Test void saveTest() {
        // Given ...
        AppUser user = new AppUser();

        user.setName("UserName");
        user.setEmail("user@example.com");
        user.setPassword("contrasenA123");
        user.setRole(Role.USER);

        AppUser savedUser = appUserRepository.save(user);

        Token token = new Token();
        token.setAppUser(savedUser);

        Token savedToken = tokenRepository.save(token);

        // When ...

        Optional<AppUser> userFounded = appUserRepository.findByEmail("user@example.com");
        Optional<Token> tokenFounded = tokenRepository.findByAppUserId(savedUser.getId());

        // Then ...
        assertTrue(userFounded.isPresent());
        assertEquals("UserName", userFounded.get().getName());
        assertEquals("user@example.com", userFounded.get().getEmail());

        assertTrue(tokenFounded.isPresent());
        assertEquals(savedUser.getId(), tokenFounded.get().getAppUser().getId());
    }

    /**
     * TODO#10
     * Completa este test de integración para que verifique que
     * cuando se borra un usuario, automáticamente se borran sus tokens asociados.
     */
    @Test void deleteCascadeTest() {
        // Given ...
        AppUser user = new AppUser();

        user.setName("UserName");
        user.setEmail("user@example.com");
        user.setPassword("contrasenA123");
        user.setRole(Role.USER);

        AppUser savedUser = appUserRepository.save(user);

        Token token = new Token();
        token.setAppUser(savedUser);

        tokenRepository.save(token);

        assertEquals(1, appUserRepository.count());
        assertEquals(1, tokenRepository.count());

        // When ...

        appUserRepository.delete(savedUser);

        // Then ...
        assertEquals(0, appUserRepository.count());
        assertEquals(0, tokenRepository.count());
    }
}