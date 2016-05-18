package unnamed.mini.pw.edu.pl.unnamedapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegisterExternalDto {
    String userName;
    String provider;
    String externalAccessToken;
    String email;
}
