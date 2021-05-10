package predict.stock.base;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import predict.stock.utils.Utils;

public class SPBusCodec implements MessageCodec<SPData, SPData> {



    public SPBusCodec() {
    }

    @Override
    public void encodeToWire(Buffer buffer, SPData data) {
        buffer.appendBuffer(Utils.toBuffer(data));
    }

    @Override
    public SPData decodeFromWire(int i, Buffer buffer) {
        return Utils.fromBytes(buffer.getBytes(i, buffer.length() - 1));
    }

    @Override
    public SPData transform(SPData data) {
        return data;
    }

    @Override
    public String name() {
        return "spBusCodec";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
