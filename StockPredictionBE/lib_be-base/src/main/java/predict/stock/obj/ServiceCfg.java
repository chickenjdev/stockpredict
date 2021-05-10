package predict.stock.obj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.j2objc.annotations.Property;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceCfg {
    public int ID;
    @Property("copy, nonatomic") @JsonProperty("CODE")
    public String code = "";
    @Property("copy, nonatomic") @JsonProperty("NAME")
    public String name = "";
    @Property("assign, nonatomic") @JsonProperty("PARENTID")
    public int parentId;
    @Property("copy, nonatomic") @JsonProperty("ICONURL")
    public String iconUrl = "";
    @Property("assign, nonatomic") @JsonProperty("STATUS")
    public int status;
    @Property("copy, nonatomic") @JsonProperty("NOTE")
    public String note = "";
    @Property("assign, nonatomic") @JsonProperty("SORTORDER")
    public int sortOrder;
    @Property("copy, nonatomic") @JsonProperty("JSONINIT")
    public String jsonInit = "";
    @Property("copy, nonatomic") @JsonProperty("JSONDATA")
    public String jsonData = "";
    @Property("assign, nonatomic") @JsonProperty("LASTUPDATE")
    public double lastUpdate;
    @Property("assign, nonatomic") @JsonProperty("ICONWIDTH")
    public int iconWidth;
    @Property("assign, nonatomic") @JsonProperty("ICONHEIGHT")
    public int iconHeight;
    @Property("copy, nonatomic") @JsonProperty("REDIRECTTO")
    public String redirectTo = "";
    @Property("copy, nonatomic") @JsonProperty("DEFAULTPARAM")
    public String defaultParam = "";
    @Property("copy, nonatomic") @JsonProperty("DEFAULTICON")
    public String defaultIcon = "";
    @Property("assign, nonatomic") @JsonProperty("BILLTYPE")
    public int billType;
    @Property("assign, nonatomic") @JsonProperty("TOTALFORM")
    public int totalForm;
    @Property("copy, nonatomic") @JsonProperty("HOST")
    public String host = "";
    @Property("assign, nonatomic") @JsonProperty("PORT")
    public int port;
    @Property("copy, nonatomic") @JsonProperty("URI")
    public String uri = "";
    @Property("copy, nonatomic") @JsonProperty("PARTNERAGENT")
    public String partnerAgent = "";
    @Property("assign, nonatomic") @JsonProperty("ISBUYCARD")
    public int isBuyCard;
    @Property("copy, nonatomic") @JsonProperty("EXTRA1")
    public String extra1 = "";
    @Property("copy, nonatomic") @JsonProperty("EXTRA2")
    public String extra2 = "";
    @Property("copy, nonatomic") @JsonProperty("HOMEPOSITION")
    public String homePosition = "";
    @Property("copy, nonatomic") @JsonProperty("CREATEDATE")
    public Date createDate;
    @Property("copy, nonatomic") @JsonProperty("CREATEBY")
    public String createBy = "";
    @Property("copy, nonatomic") @JsonProperty("UPDATEBY")
    public String updateBy = "";
    @Property("copy, nonatomic") @JsonProperty("IS2STEP")
    public Boolean is2Step = false;
    @Property("copy, nonatomic") @JsonProperty("HASMOCKING")
    public Boolean hasMocking = false;
    @Property("copy, nonatomic") @JsonProperty("CONFIGDATA")
    public String configData = "";
    @Property("assign, nonatomic") @JsonProperty("STATUSBACKEND")
    public int statusBackEnd;
    @Property("assign, nonatomic") @JsonProperty("LASTUPDATESTATUS")
    public double lastUpdateStatus;

}
