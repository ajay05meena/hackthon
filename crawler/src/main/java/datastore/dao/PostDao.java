package datastore.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostDao {
    private String id;
    private String message;
}
