package bavard.network;

import java.io.Serializable;

public enum NetworkEventType implements Serializable {
    WHO_IS_OUT_THERE,
    CHECK_PSEUDONYM,
    NOTIFY_PRESENCE,
    RESPOND_PRESENCE,
    NOTIFY_ABSENCE
}
