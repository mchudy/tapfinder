package unnamed.mini.pw.edu.pl.unnamedapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    String userName;
    String email;
    String password;
}
