# CAPABILITY_AUDIT.md — Capability 系统迁移方案

> Forge 1.20.1 的 `Capability<T>` + `LazyOptional<T>` + `ICapabilityProvider` 三件套已被 NeoForge 1.21.1 **完全重构**为 `BlockCapability` / `EntityCapability` / `ItemCapability` + `IAttachmentHolder` 系统。

---

## 一、扫描结果

`nonamecrackers2/witherstormmod/common/capability/` 含 7 个文件：

| 文件 | 用途 | 复杂度 |
|---|---|---|
| `EntityCapability.java` | **抽象基类**——包装 Forge Capability API | ⭐⭐⭐ |
| `ChunkLoadingBlockEntities.java` | 持久化"哪些 BlockEntity 在加载 chunk" | ⭐⭐ |
| `PlayerWitherStormData.java` | 玩家与凋零风暴的关系状态 | ⭐⭐⭐ |
| `WitherSicknessTracker.java` | 凋零感染追踪 | ⭐⭐ |
| `WitherStormAutoSpawner.java` | 凋零风暴自动生成器（世界级数据） | ⭐⭐⭐ |
| `WitherStormBowelsManager.java` | 凋零风暴体内空间管理（containment） | ⭐⭐⭐⭐ |
| `WitherStormModChunkLoader.java` | mod 级 chunk 加载管理 | ⭐⭐⭐ |

---

## 二、Forge → NeoForge Capability API 对照

### 2.1 旧 API（1.20.1 Forge）

```java
// 注册 Capability
public static Capability<MyCap> MY_CAP = CapabilityManager.get(new CapabilityToken<>() {});

// 给实体附加 Capability（AttachCapabilitiesEvent）
@SubscribeEvent
public static void attach(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof Player player) {
        LazyOptional<MyCap> opt = LazyOptional.of(() -> new MyCap(player));
        event.addCapability(new ResourceLocation("witherstormmod", "my_cap"), 
            new ICapabilitySerializable<Tag>() { ... });
    }
}

// 访问
entity.getCapability(MY_CAP).ifPresent(cap -> cap.doSomething());
```

### 2.2 新 API（1.21.1 NeoForge）

NeoForge 1.21.1 提供了**两套**系统，对应不同场景：

#### 方案 A：**Data Attachments**（推荐用于持久数据）

```java
// 注册 attachment type
private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = 
    DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, "witherstormmod");

public static final Supplier<AttachmentType<MyData>> MY_DATA = ATTACHMENTS.register(
    "my_data",
    () -> AttachmentType.builder(holder -> new MyData())
        .serialize(MyData.CODEC)  // 自动持久化
        .copyOnDeath()
        .build()
);

// 访问
MyData data = entity.getData(MY_DATA);
entity.setData(MY_DATA, newData);
```

#### 方案 B：**Capabilities 2.0**（适用于交互接口如 IItemHandler、IFluidHandler）

```java
public static final EntityCapability<IItemHandler, Direction> ITEM_HANDLER = 
    EntityCapability.createSided(
        ResourceLocation.fromNamespaceAndPath("witherstormmod", "item_handler"),
        IItemHandler.class
    );

// 注册
@SubscribeEvent
public static void registerCaps(RegisterCapabilitiesEvent event) {
    event.registerEntity(ITEM_HANDLER, EntityType.PLAYER, 
        (player, side) -> player.getInventory().itemHandler);
}

// 访问
IItemHandler handler = entity.getCapability(ITEM_HANDLER, side);
if (handler != null) { ... }
```

---

## 三、逐文件迁移策略

### 3.1 `EntityCapability.java` —— 完全重写为 **Data Attachment**

```java
// 新版
public abstract class EntityCapability<E extends EntityCapability<E, T>, T extends Entity> {
    protected final T entity;
    
    protected EntityCapability(T entity) { this.entity = entity; }
    
    public abstract void tick();
    public abstract CompoundTag write(HolderLookup.Provider lookup);
    public abstract void read(CompoundTag tag, HolderLookup.Provider lookup);
    public abstract void copyFrom(E other);
}
```

**关键变化**：
- 删除 `Serializable` 内部类（不再需要包装 ICapabilitySerializable）
- 删除 `LazyOptional` 引用
- `write/read` 方法增加 `HolderLookup.Provider` 参数（1.21 NBT 系统改）
- 改用 AttachmentType 注册

### 3.2 `PlayerWitherStormData.java` —— Data Attachment

每个玩家自己的状态，用 `AttachmentType` + `copyOnDeath()`。

### 3.3 `WitherSicknessTracker.java` —— Data Attachment

同上。

### 3.4 `WitherStormAutoSpawner.java` —— 改用 `SavedData`

世界级数据（不是实体级），用 vanilla 的 `SavedData` 系统：

```java
public class WitherStormAutoSpawner extends SavedData {
    private static final SavedData.Factory<WitherStormAutoSpawner> FACTORY = 
        new SavedData.Factory<>(WitherStormAutoSpawner::new, WitherStormAutoSpawner::load);
    
    public static WitherStormAutoSpawner get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(FACTORY, "witherstorm_autospawner");
    }
    
    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider lookup) { ... }
}
```

### 3.5 `WitherStormBowelsManager.java` —— 实体 Data Attachment

凋零风暴实体自己的数据。

### 3.6 `ChunkLoadingBlockEntities.java` —— Data Attachment 挂在 Level 上

或者改用 `Level` 级 `AttachmentType`（NeoForge 也支持给 Level 挂 attachment）。

### 3.7 `WitherStormModChunkLoader.java` —— `SavedData`

世界级 chunk loader 注册表。

---

## 四、迁移工作量

| 文件 | 改动行数估算 | 工时 |
|---|---|---|
| `EntityCapability` (基类) | ~30 行 | 1-2h |
| `PlayerWitherStormData` | ~60 行 | 2-3h |
| `WitherSicknessTracker` | ~40 行 | 1-2h |
| `WitherStormAutoSpawner` | ~80 行 | 2-3h |
| `WitherStormBowelsManager` | ~100 行 | 3-5h |
| `ChunkLoadingBlockEntities` | ~50 行 | 1-2h |
| `WitherStormModChunkLoader` | ~80 行 | 2-3h |
| **测试与调试** | — | 3-5h |
| **合计** | | **15-25h** |

---

## 五、风险点

1. **`copyOnDeath` 行为**：Data Attachment 默认死亡后不复制，需显式设置
2. **`syncToClient`**：如果客户端也需要这些数据，需要额外的 sync packet
3. **JSON/SNBT 持久化兼容性**：玩家死亡/世界存档兼容
4. **Bowels Manager 跨实体引用**：凋零风暴体内的实体引用，跨 chunk 时数据持久化要小心

---

## 六、建议迁移顺序

1. 先做 `EntityCapability` 基类（其他文件的前置依赖）
2. 然后做最简单的两个：`WitherSicknessTracker` + `PlayerWitherStormData`
3. 再做 `WitherStormAutoSpawner`（独立的 SavedData，无依赖）
4. 最后做 `WitherStormBowelsManager` + `ChunkLoadingBlockEntities`（互相依赖）
5. `WitherStormModChunkLoader` 视情况

---

## 七、注意事项

- **NeoForge 1.21.1 已经稳定支持 Data Attachment**，是首选方案
- 避免使用废弃的 Forge Capability API（即使有兼容层也不要依赖）
- 所有 NBT 操作必须传 `HolderLookup.Provider`（1.21 强制）
