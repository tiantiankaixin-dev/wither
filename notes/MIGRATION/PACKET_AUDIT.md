# PACKET_AUDIT.md — 34 个 Packet 迁移方案

> **重大发现**：所有 packet 继承自 `crackerslib.common.packet.Packet` 抽象基类
> **战略意义**：迁移 crackerslib 的 Packet 基类 → 33 个子类几乎零修改

---

## 一、Packet 列表（33 个 Message + 1 个 Handler）

### 服务端 → 客户端（约 28 个，渲染/特效同步）

| Packet | 用途 |
|---|---|
| `BlindScreenMessage` | 屏幕致盲特效 |
| `CreateDebrisMessage` | 碎片生成 |
| `CreateLoopingSoundMessage` | 循环音效 |
| `DistantRendererMessage` | 远距离渲染 |
| `EntitySyncableDataMessage` | 实体数据同步 |
| `FormidibombExplosionMessage` | Formidi 炸弹爆炸 |
| `GlobalSoundMessage` | 全局音效 |
| `InjureHeadMessage` | 头部受伤 |
| `NotifyHeadInjuryMessage` | 头部受伤通知 |
| `OnHeadAttackedMessage` | 头部被攻击 |
| `PlayAdditionalLoopingSoundMessage` | 附加循环音效 |
| `PlayerMotionMessage` | 玩家运动（拉拽光束） |
| `RemoveAdditionalLoopingSoundMessage` | 移除附加音效 |
| `RemoveDistantSuperBeaconMessage` | 移除远距 SuperBeacon |
| `RemoveSoundLoopMessage` | 移除音效循环 |
| `RemoveStormFromDistantRendererMessage` | 从远距渲染器移除风暴 |
| `ShakeScreenMessage` | 屏幕震动 |
| `StormAttributesMessage` | 风暴属性同步 |
| `StormMetadataMessage` | 风暴元数据 |
| `StormSoundPositionMessage` | 风暴音效位置 |
| `StormTeleportMessage` | 风暴传送 |
| `SuperBeaconSetEffectMessage` | 设置 SuperBeacon 效果 |
| `SuperBeaconValidEffectsMessage` | SuperBeacon 有效效果 |
| `UpdateDamagingProjectileMessage` | 投射物更新 |
| `UpdateDistantSuperBeaconMessage` | 远距 SuperBeacon 更新 |
| `UpdateEffectInstanceMessage` | 效果实例更新 |
| `UpdatePlayDeadManagerMessage` | 装死管理器更新 |
| `UpdateStormHeadLookMessage` | 风暴头部朝向更新 |
| `UpdateStormPositionMessage` | 风暴位置更新 |
| `UpdateStormVelocityMessage` | 风暴速度更新 |
| `UpdateWitherSicknessTrackerMessage` | 凋零感染追踪器更新 |
| `WitherStormToDistantRendererMessage` | 风暴投递到远距渲染器 |

### 客户端 → 服务端（约 1 个）

| Packet | 用途 |
|---|---|
| `SuperBeaconToggleAreaMessage` | 玩家切换 SuperBeacon 区域 |

### 处理器

| 文件 | 用途 |
|---|---|
| `WitherStormModMessageHandlerServer` | 服务端消息分发入口 |

---

## 二、当前架构（1.20.1 Forge）

```java
// crackerslib 提供的抽象基类
public abstract class Packet {
    protected Packet(boolean isOutgoing) { ... }
    public abstract void encode(FriendlyByteBuf buffer);
    public abstract void decode(FriendlyByteBuf buffer);
    public abstract Runnable getProcessor(NetworkEvent.Context context);
}

// 每个子类
public class PlayerMotionMessage extends Packet {
    public void encode(FriendlyByteBuf buffer) { ... }
    public void decode(FriendlyByteBuf buffer) { ... }
    public Runnable getProcessor(Context context) {
        return () -> DistExecutor.unsafeRunWhenOn(
            Dist.CLIENT, 
            () -> () -> WitherStormModMessageHandlerClient.processPlayerMotionMessage(this)
        );
    }
}
```

---

## 三、目标架构（1.21.1 NeoForge）

NeoForge 1.21.1 使用 `CustomPacketPayload` + `StreamCodec` 模式：

```java
// NEW: crackerslib 提供的新抽象基类
public abstract class Packet implements CustomPacketPayload {
    @Override
    public abstract Type<? extends CustomPacketPayload> type();
    
    // 子类只需实现 streamCodec() 和 handle()
    public abstract StreamCodec<RegistryFriendlyByteBuf, ? extends Packet> streamCodec();
    public abstract void handle(IPayloadContext context);
}

// NEW: 每个子类
public class PlayerMotionMessage extends Packet {
    public static final Type<PlayerMotionMessage> TYPE = 
        new Type<>(ResourceLocation.fromNamespaceAndPath("witherstormmod", "player_motion"));
    
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerMotionMessage> CODEC = 
        StreamCodec.composite(
            ByteBufCodecs.DOUBLE, m -> m.motion.x,
            ByteBufCodecs.DOUBLE, m -> m.motion.y,
            ByteBufCodecs.DOUBLE, m -> m.motion.z,
            (x, y, z) -> new PlayerMotionMessage(new Vec3(x, y, z))
        );
    
    @Override
    public Type<PlayerMotionMessage> type() { return TYPE; }
    
    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                WitherStormModMessageHandlerClient.processPlayerMotionMessage(this);
            }
        });
    }
}
```

---

## 四、迁移策略：**模板化批处理**

由于 33 个 packet 结构高度一致，AI 可以**模板化批量生成**：

### 步骤

1. **先迁移 crackerslib 的 Packet 基类**（1-2 小时）：
   - 改为 `CustomPacketPayload`
   - 提供 `IPayloadRegistrar` 注册帮助方法

2. **AI 批量重写 33 个子类**（约 6-12 小时）：
   - 每个 packet 约 20-30 分钟（AI 主导）
   - 模式固定：`encode()` → `StreamCodec.composite()`、`decode()` → 同上、`getProcessor()` → `handle()`

3. **重写 `WitherStormModMessageHandlerServer`**（2-3 小时）：
   - 注册所有 packet 到 `PayloadRegistrar`
   - 分发 dispatcher

4. **集成测试**（2-4 小时）：
   - runClient + runServer 联调
   - 验证至少 5 个核心 packet（PlayerMotion / StormPosition / GlobalSound / ShakeScreen / SuperBeaconToggle）

**预期总工时**：**11-21 小时**（含 crackerslib Packet 基类）

---

## 五、复杂度评估

| Packet | 复杂度 | 备注 |
|---|---|---|
| 简单（仅基础类型 int/double/Vec3） | ⭐ 极简 | 约 20 个 |
| 中等（含 ItemStack / List） | ⭐⭐ 中 | 约 8 个 |
| 复杂（含自定义对象/ContraptionData） | ⭐⭐⭐ 复杂 | 约 5 个 |

复杂的几个：
- `StormMetadataMessage`（多种数据混合）
- `EntitySyncableDataMessage`（自定义 syncable data 系统）
- `UpdateEffectInstanceMessage`（MobEffectInstance — 1.21 改 Holder）
- `WitherStormToDistantRendererMessage`（大数据包）
- `DistantRendererMessage`（远距离渲染数据）

---

## 六、ByteBufCodecs 速查表（1.21.1）

| 旧写法 | 新写法 |
|---|---|
| `buf.writeInt(x)` / `readInt()` | `ByteBufCodecs.VAR_INT` |
| `buf.writeDouble(x)` / `readDouble()` | `ByteBufCodecs.DOUBLE` |
| `buf.writeUtf(s)` / `readUtf()` | `ByteBufCodecs.STRING_UTF8` |
| `buf.writeUUID(u)` / `readUUID()` | `UUIDUtil.STREAM_CODEC` |
| `buf.writeItem(stack)` / `readItem()` | `ItemStack.STREAM_CODEC` |
| `buf.writeBlockPos(p)` / `readBlockPos()` | `BlockPos.STREAM_CODEC` |
| `buf.writeResourceLocation(rl)` | `ResourceLocation.STREAM_CODEC` |
| 自定义对象 | 用 `StreamCodec.composite` 组合 |

---

## 七、行动建议

- 先做 crackerslib Packet 基类（其他子类的前置依赖）
- 然后**AI 模板化生成全部 33 个 packet 子类**（一次输出，人工抽查）
- 最后做 Handler 注册和测试
