package predict.stock.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlobalCfg {
    @JsonProperty("ID")
    private int id;
    @JsonProperty("TYPE")
    private String type;
    @JsonProperty("MAINKEY")
    private String mainKey;
    @JsonProperty("SUBKEY")
    private String subKey;
    @JsonProperty("CONTENT")
    private String content;
    @JsonProperty("EXTCONTENT")
    private String extContent;
    @JsonProperty("NOTE")
    private String note;
    @JsonProperty("LASTTIME")
    private long lastTime;
}
