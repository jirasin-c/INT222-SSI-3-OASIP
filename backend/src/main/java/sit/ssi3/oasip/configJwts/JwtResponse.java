package sit.ssi3.oasip.configJwts;

import lombok.*;
import sit.ssi3.oasip.dtos.RespondUserDTO;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponse {

    private String message;
    private String accessToken;
    private String refreshToken;

}


