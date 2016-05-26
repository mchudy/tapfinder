package unnamed.mini.pw.edu.pl.unnamedapp.model;

import java.util.Date;

import lombok.Data;

@Data
public class CommentDto {
    String text;
    String userName;
    Date date;
}
