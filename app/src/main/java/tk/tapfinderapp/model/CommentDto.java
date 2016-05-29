package tk.tapfinderapp.model;

import java.util.Date;

import lombok.Data;

@Data
public class CommentDto {
    int id;
    String text;
    String userName;
    Date date;
    String placeId;
}
