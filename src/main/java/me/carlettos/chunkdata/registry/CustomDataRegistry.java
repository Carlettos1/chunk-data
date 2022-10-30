package me.carlettos.chunkdata.registry;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

import net.minecraft.util.Identifier;

/**
 * An small class to maintain the registry of the {@link DataType} 
 * instances and automatize processes related to serialization.
 */
public class CustomDataRegistry {
    private CustomDataRegistry() {}
    
    /**
     * All the registered {@link DataType} instances.
     */
    public static final LinkedHashMap<Identifier, DataType<?>> REGISTRY = new LinkedHashMap<>();
    
    /**
     * Register a {@link DataType} instance with an id.
     * 
     * @param id id of the instance
     * @param dataType {@link DataType} instance to register
     */
    public static final void register(Identifier id, DataType<?> dataType) {
        REGISTRY.put(id, dataType);
    }
    
    /**
     * ForEach shortcut.
     */
    public static final void forEach(BiConsumer<Identifier, DataType<?>> consumer) {
        REGISTRY.forEach(consumer);
    }
}
