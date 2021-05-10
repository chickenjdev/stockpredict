package predict.stock.msg;

import lombok.Getter;
import lombok.Setter;
import predict.stock.base.SPMsg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TelegramTrackerMsg extends SPMsg {
    private String title = "";
    private Date time = new Date();
    private List<String> keys = new ArrayList<>();
    private List<String> values = new ArrayList<>();

    public void addContent(String key, String value) {
        keys.add(key);
        values.add(value);
    }
}
