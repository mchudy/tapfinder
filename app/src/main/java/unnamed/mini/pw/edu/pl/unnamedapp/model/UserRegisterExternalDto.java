package unnamed.mini.pw.edu.pl.unnamedapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegisterExternalDto {
    private String userName;
    private String provider;
    private String externalAccessToken;
    private String email;
}
