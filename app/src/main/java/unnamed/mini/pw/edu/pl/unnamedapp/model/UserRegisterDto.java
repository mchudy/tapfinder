package unnamed.mini.pw.edu.pl.unnamedapp.model;

import lombok.Value;

@Value
public class UserRegisterDto {
    String username;
    String email;
    String password;
}
