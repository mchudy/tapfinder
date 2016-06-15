package tk.tapfinderapp.model.user;

import java.util.List;

import lombok.Value;
import tk.tapfinderapp.model.BadgeDto;
import tk.tapfinderapp.model.RankDto;

@Value
public class UserDto {
    String userName;
    String imagePath;
    RankDto rank;
    List<BadgeDto> badges;
    int experience;
}
