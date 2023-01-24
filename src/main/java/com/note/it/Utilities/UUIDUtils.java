package com.note.it.Utilities;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UUIDUtils {
    public UUID generate() {
        // Todo: Check Thread Safety
        return UUID.randomUUID();
    }

    public UUID toString(final String uuid) {
        return UUID.fromString(uuid);
    }
}
