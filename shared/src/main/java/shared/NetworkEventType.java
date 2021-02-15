package shared;

import java.io.Serializable;

public enum NetworkEventType implements Serializable {
    WHO_IS_OUT_THERE,
    NOTIFY_PRESENCE,
    RESPOND_PRESENCE,
    NOTIFY_ABSENCE
}
