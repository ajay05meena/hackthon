package fb.crawler.fb.model;


import lombok.Data;

import java.util.Date;

@Data
public class FBPageDetail {
    private String id;
    private String name;
    private String likes;
    private Date time = new Date();
}
