package tk.pubfinderapp.model;

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
