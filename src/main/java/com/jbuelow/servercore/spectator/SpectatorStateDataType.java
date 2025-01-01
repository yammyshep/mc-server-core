package com.jbuelow.servercore.spectator;

import org.apache.commons.lang.SerializationUtils;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class SpectatorStateDataType implements PersistentDataType<byte[], SpectatorState> {
    @NotNull
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @NotNull
    @Override
    public Class<SpectatorState> getComplexType() {
        return SpectatorState.class;
    }

    @NotNull
    @Override
    public byte[] toPrimitive(@NotNull SpectatorState complex, @NotNull PersistentDataAdapterContext context) {
        return SerializationUtils.serialize(complex);
    }

    @NotNull
    @Override
    public SpectatorState fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
        try {
            InputStream inputStream = new ByteArrayInputStream(primitive);
            ObjectInputStream objectStream = new ObjectInputStream(inputStream);
            return (SpectatorState) objectStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
