package tk.tapfinderapp.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class LikeDto {
    int likeableItemId;
    boolean liked;
}
