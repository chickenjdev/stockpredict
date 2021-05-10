package predict.stock.base;

import com.google.j2objc.annotations.Property;
import com.mservice.proxy.entity.ProxyRequest;
import com.mservice.proxy.entity.ProxyResponse;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class SPData implements Serializable {
    @Property("assign, nonatomic")
    private long time = System.currentTimeMillis();
    @Property("assign, nonatomic")
    private ProxyRequest request = new ProxyRequest();
    @Property("assign, nonatomic")
    private ProxyResponse response = new ProxyResponse();
    private String user = "";
    @Property("copy, nonatomic")
    private String spUser = "";
    @Property("copy, nonatomic")
    private String pass = "";
    @Property("copy, nonatomic")
    private String imei = "";
    @Property("copy, nonatomic")
    private String bankCode = "";
    @Property("copy, nonatomic")
    private String bankName = "";
    @Property("copy, nonatomic")
    private int mapSacomCard = -1;
    @Property("copy, nonatomic")
    private int validWalletConfirm = -1;
    @Property("copy, nonatomic")
    private String name = "";
    @Property("copy, nonatomic")
    private String identify = "";
    @Property("copy, nonatomic")
    private String deviceOs = "";
    @Property("copy, nonatomic")
    private int appVer = 0;
    @Property("copy, nonatomic")
    private String lastSessionKey = "";
    @Property("copy, nonatomic")
    private int agentId = 0;
    @Property("copy, nonatomic")
    private String cmdId = "";
    @Property("copy, nonatomic")
    private String lang = "vi";
    @Property("assign, nonatomic")
    private boolean result = true;
    @Property("assign, nonatomic")
    private int errorCode = 0;
    @Property("copy, nonatomic")
    private String errorDesc = "";
    public SPMsg spMsg;

    private String channel = "APP";//SMS, APP, WEB...
    @Property("retain, nonatomic")
    private ConcurrentHashMap<String, Object> extra = new ConcurrentHashMap<>();

    public boolean getResult(){
        return result;
    }

    public void setResult(boolean result){
        this.result = result;
    }

    public Object getExtra(String key) {
        return extra.get(key);
    }

    public <T> T getExtra(String key, Class<T> type) {
        return  (T) getExtra(key);
    }

    public void putExtra(String key, Object val) {
        if (val != null) {
            if (val instanceof String) {
                extra.put(key, (String) val);
            }
        }
    }

    public Object popExtra(String key) {
        return extra.remove(key);
    }

    public void addExtras(Map<String, String> kvps) {
        for (Map.Entry<String, String> entry : kvps.entrySet()) {
            putExtra(entry.getKey(), entry.getValue());
        }
    }

    public void addFullExtras(Map<String, Object> kvps) {
        for (Map.Entry<String, Object> entry : kvps.entrySet()) {
            putExtra(entry.getKey(), entry.getValue());
        }
    }

    @Nullable
    public SPMsg getSpMsg() {
        return this.spMsg;
    }

    public void setSpMsg(SPMsg spMsg) {
        this.spMsg = spMsg;
    }

    public <T> T getSpMsg(Class<T> type) {
        return (T) spMsg;
    }
}

