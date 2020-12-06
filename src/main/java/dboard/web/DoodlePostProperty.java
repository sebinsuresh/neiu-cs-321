package dboard.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="doodlefeed.posts")
@Data
public class DoodlePostProperty {
    private int feedSize = 5;   // Value will be overriden in configuration
}
