# Chunk Data API

## Setup

For setup instructions please see the [fabric wiki page](https://fabricmc.net/wiki/tutorial:setup) that relates to the IDE that you are using.

## Usage

An example usage, using pollution as a value that needs to be saved in a chunk:

```java
// on the ModInitializer
public class MOD implements ModInitializer {
    public static final Identifier POLLUTION_CHUNK_DATA = new Identifier(MOD_ID, "pollution");
    @Override
    public void onInitialize() {
        // ...
        CustomDataRegistry.register(POLLUTION_CHUNK_DATA, new FloatDataType(() -> 0)); // starts at 0 pollution
        // CustomDataRegistry.register(POLLUTION_CHUNK_DATA, new FloatDataType(() -> 10*(new Random()).nextFloat())); // starts at a random (0-10) pollution
    }
}

// on a block that emits pollution

public class BLOCK extends Block {
    
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        float current = CustomDataManager.getData(world, ChunkPos.fromRegion(pos.getX(), pos.getY()), POLLUTION_CHUNK_DATA); // gets the current data
        CustomDataManager.setData(world, pos, POLLUTION_CHUNK_DATA, current + 1); // sets new data
    }
}
```

Step by step:

1. Create a new Identifier to identify the custom chunk data (use yours mod id to avoid possible errors with other mods).
2. Register a new data type using CustomDataRegistry#register, using its Identifier and a new DataType, in this case, 
we used a FloatDataType which will serialize and deserialize a float number for us, 
the argument in its constructor is a Supplier which will generate the starting data in the chunk.
3. Use CustomDataManager#getData to get the data associated with the given Identifier.
4. Use CustomDataManager#setData to set the new data, and it will be associated with the given Identifier.

Breaking down all the 4 steps:

### Identifier

Its just using the same system that minecraft uses to register its blocks, items, etc.

I suggest also using the mod id to distinguish between mods.

### CustomDataRegistry

The only method that needs to be used is `register` which take an Identifier and a `DataType`.

The `DataType` system its a bit complex to use. If you only need to save one or two values to all chunks, then
use some wrapper like one of the following:
1. `BooleanDataType` which serializes a boolean
2. `ByteDataType` which serializes a byte
3. `DoubleDataType` which serializes a double
4. `FloatDataType` which serializes a float
5. `IntDataType` which serializes an integer
6. `LongDataType` which serializes a long
7. `ShortDataType` which serializes a short
8. `StringDataType` which serializes a String

Every one of this uses a Supplier which will supply a default value to the chunk upon creation of the custom data.
If you need a more flexible `DataType`, you can use `CustomDataType`, which will take a Supplier, as the wrapper DataType.
But it needs a custom serializer to read and write data from the chunk NBT.

Some Builtin serializers are:
1. `BlockSerializer`
2. `ItemSerializer`
Which will serialize a block or an item. Its basic usage is:

```java
public static final Identifier ITEM_DATA = new Identifier(MOD_ID, "item");
// ...
CustomDataRegistry.register(ITEM_DATA, new CustomDataType<>(() -> Registry.ITEM.get((new Random()).nextInt(Registry.ITEM.size())), new ItemSerializer()));
```
Which will register a new DataType with:
1. Id of `ITEM_DATA`
2. A Supplier which will give a random base-item for every chunk (one can be `"minecraft:cake"`, and the next to it can be `"my_mod:some_item"`)
3. An ItemSerializer, which will serialize and deserialize the item for you

With this in mind, you can make your own serializer. Something like this:

```java

public class MagicLevel {
    public float whiteMagic;
    public float blackMagic;
    public float baseMagic;
    public float mana;

    public MagicLevel(float whiteMagic, float blackMagic, float baseMagic, float mana) {
        this.whiteMagic = whiteMagic;
        this.blackMagic = blackMagic;
        this.baseMagic = baseMagic;
        this.mana = mana;
    }
    
    public static MagicLevel getDefault() {
        return new MagicLevel(50, 20, 10, 10);
    }
```

Needs a serializer like this:

```java
public class MagicLevelSerializer implements Serializer<MagicLevel> {
    @Override
    public void serialize(NbtCompound parent, MagicLevel obj) {
        parent.putFloat("white_magic", obj.whiteMagic);
        parent.putFloat("black_magic", obj.blackMagic);
        parent.putFloat("base_magic", obj.baseMagic);
        parent.putFloat("mana", obj.mana);
    }

    @Override
    public MagicLevel deserialize(NbtCompound parent) {
        return new MagicLevel(
                parent.getFloat("white_magic"), 
                parent.getFloat("black_magic"), 
                parent.getFloat("base_magic"), 
                parent.getFloat("mana"));
    }
}
```

And, will be registered like this:

```java
CustomDataRegistry.register(MAGIC_LEVEL_DATA, new CustomDataType<>(MagicLevel::getDefault, new MagicLevelSerializer()));
```

And can be used in something like this:

```java
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        MagicLevel magicLevel = CustomDataManager.getData(world, player.getChunkPos(), ChunkData.MAGIC_LEVEL_DATA);
        if (magicLevel.mana > 1) {
            // do something magic
            magicLevel.mana -= 1;
            CustomDataManager.setData(world, player.getBlockPos(), ChunkData.MAGIC_LEVEL_DATA, magicLevel);
        }
        // ...
    }
```

### CustomDataManager

Its the data manager, which will be used for `setData` and `getData`. It must be notified of any change so it can save it to the chunk NBT.
You can notify the `CustomDataManager` using the `setData` (`setData will notify in every case`) or `notify` method. 
Or notify the chunk using `chuk.setNeedsSaving(true);` which will also notify the `CustomDataManager`.

Both methods have a lot or overloads, and might have more in the future.

