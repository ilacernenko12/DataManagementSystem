package by.chernenko.datamanagementsystem.service;

import by.chernenko.datamanagementsystem.model.Role;
import by.chernenko.datamanagementsystem.model.User;
import by.chernenko.datamanagementsystem.dto.AuthenticationRequest;
import by.chernenko.datamanagementsystem.dto.AuthenticationResponse;
import by.chernenko.datamanagementsystem.dto.RegisterRequest;
import by.chernenko.datamanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<String> register(RegisterRequest request) {
        try {
            // Проверка на занятость email
            if (repository.existsByEmail(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Email is already taken.");
            }

            // Проверка на занятость username
            if (repository.existsByUsername(request.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Username is already taken.");
            }

            // Создание и сохранение нового пользователя
            var user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .company(request.getCompany())
                    .role(Role.USER)
                    .build();

            repository.save(user);

            // Успешный ответ
            return ResponseEntity.ok("Registration successful.");
        } catch (Exception ex) {
            // Логирование ошибки (при необходимости)
            ex.printStackTrace();

            // Ответ в случае ошибки сервера
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during registration.");
        }
    }


    public ResponseEntity<?> authenticate(AuthenticationRequest request) {
        try {
            // Проверка, что terminalId не равен null или пустой строке
            if (request.getTerminalId() == null || request.getTerminalId().isBlank()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Terminal ID cannot be null or empty.");
            }

            // Поиск пользователя в базе данных
            var user = repository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Аутентификация пользователя
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Обновление terminalId
            user.setTerminalId(request.getTerminalId());
            repository.save(user);

            // Генерация JWT токена
            var jwtToken = jwtService.generateToken(user);

            // Возврат успешного ответа
            return ResponseEntity.ok(AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build());
        } catch (UsernameNotFoundException ex) {
            // Обработка ситуации, когда пользователь не найден
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invalid username or password.");
        } catch (BadCredentialsException ex) {
            // Обработка ситуации неверных учетных данных
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Incorrect username or password.");
        } catch (Exception ex) {
            // Обработка прочих ошибок
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during authentication.");
        }
    }
}
