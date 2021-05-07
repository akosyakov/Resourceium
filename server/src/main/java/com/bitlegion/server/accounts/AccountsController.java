package com.bitlegion.server.accounts;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/accounts") // This means URLs start with /demo (after Application path)
@CrossOrigin
public class AccountsController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private AccountRepository accountRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TokenChecker tokenChecker;

    @Autowired
    private PasswordValidator passwordValidator;

    @Value("${spring-dev-mode}")
    private Boolean devMode;

    @PostMapping(path = "/register") // Map ONLY POST Requests
    public ResponseEntity<ResponseMessage> addNewUser(@RequestParam String username, @RequestParam String email,
            @RequestParam String password,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateOfBirth,
            @RequestParam String firstName, @RequestParam String lastName, @RequestParam String country) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        String message = "";

        ArrayList<String> errors = passwordValidator.validate(password);
        if (errors.size() > 0) {
            message = JSONArray.toJSONString(errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        if (username.length() == 0) {
            message = "You provided an invalid name";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        if (password.length() == 0) {
            message = "You provided an invalid password";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        if (username.length() == 0) {
            message = "You provided an invalid name";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        if (lastName.length() == 0) {
            message = "You provided an invalid last name";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        if (firstName.length() == 0) {
            message = "You provided an invalid first name";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        if (dateOfBirth.after(new Date())) {
            message = "Invalid date of birth detected";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        try {
            Account newUser = new Account();
            if (accountRepository.findByEmail(email).isPresent()) {
                message = "A user with email " + email + " already exists";
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage(message));
            }
            if (accountRepository.findByUsername(username).isPresent()) {
                message = "A user with name " + username + " already exists";
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage(message));
            }
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setDateOfBirth(dateOfBirth);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setCountry(country);
            accountRepository.save(newUser);
            message = "The user was created successfully!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Token> loginUser(@RequestBody AccountDetails accountDetails) {
        Optional<Account> maybeUser = accountRepository.findByUsername(accountDetails.getUsername());
        if (maybeUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Account user = maybeUser.get();
        if (user.verifyPassword(accountDetails.getPassword())) {
            Optional<Token> maybeToken = tokenRepository.findByAccount(user);
            // we check if the token already exists, if it exists, we return it
            Token token;
            if (maybeToken.isPresent()) {
                token = maybeToken.get();
            } else {
                token = new Token();
                token.setString();
                token.setAccount(user);
                tokenRepository.save(token);
            }
            return ResponseEntity.status(HttpStatus.OK).body(token);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<Boolean> logoutUser(HttpServletRequest request) {
        try {
            Token token = tokenChecker.checkAndReturnTokenOrRaiseException(request);
            tokenRepository.delete(token);
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(false);
        }
    }

    @PutMapping(path = "/update/{userID}")
    public ResponseEntity<ResponseMessage> updateUser(@RequestParam(required = false) String password,
            @RequestParam(required = false) String bio, @PathVariable Integer userID) {
        String message = "";

        Optional<Account> maybeUser = accountRepository.findById(userID);
        if (maybeUser.isEmpty()) {
            message = "No such user found";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
        Account user = maybeUser.get();

        if (password != null) {
            user.setPassword(password);
        }
        if (bio != null) {
            user.setBio(bio);
        }
        accountRepository.save(user);
        message = "The user was updated successfully";
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }

    @GetMapping(path = "/details")
    public ResponseEntity<Account> getUserDetails(HttpServletRequest request) {
        try {
            tokenChecker.checkAndReturnTokenOrRaiseException(request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        Optional<Account> maybeUser = accountRepository.findById(1);
        if (maybeUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(maybeUser.get());
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Account> getAllUsers(HttpServletRequest request) {
        return accountRepository.findAll();
    }
}
