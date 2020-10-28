package dboard.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="doodlefeed.posts")
@Data
public class DoodlePostProperty {
    private int feedSize = 5;   //Default value of 5 - will be updated based on configuration file
}
