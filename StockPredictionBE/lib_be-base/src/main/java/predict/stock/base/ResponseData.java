package predict.stock.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.j2objc.annotations.Property;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseData implements Serializable {
    @Property("assign, nonatomic")
    private long time = System.currentTimeMillis();
    @Property("assign, nonatomic")
    private int statusCode = 200;
}

