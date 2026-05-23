# IMPORT_MAP.md — Forge 1.20.1 → NeoForge 1.21.1 完整映射表

> 基于实际扫描 610 个 .java 文件、1568 个唯一 import、132 个 Forge import
> 适用版本：Forge 47.x → NeoForge 21.1.228

---

## 一、规模总览

| 顶级包 | 出现次数 | 主要工作 |
|---|---|---|
| `net.minecraft.world` | 426 | 大量 1.20.1→1.21.1 签名变化 |
| `nonamecrackers2.witherstormmod.common` | 309 | 项目内部，**包名不变** |
| `nonamecrackers2.witherstormmod.client` | 185 | 项目内部，**包名不变** |
| `net.minecraft.client` | 143 | 渲染 API 部分变化 |
| **`net.minecraftforge.event`** | 35 | → `net.neoforged.neoforge.event.*` |
| **`net.minecraftforge.common`** | 29 | → `net.neoforged.neoforge.common.*` |
| `net.minecraft.network` | 29 | **packet 系统完全重写** |
| **`net.minecraftforge.client`** | 28 | → `net.neoforged.neoforge.client.*` |
| `org.spongepowered.asm` | 17 | **包名不变**（Mixin 框架） |
| **`net.minecraftforge.fml`** | 11 | → `net.neoforged.fml.*`（注意是 `fml` 不是 `neoforge`） |

---

## 二、Forge → NeoForge 包名映射（机械替换，可自动化）

### 2.1 基础规则

```
net.minecraftforge.fml              → net.neoforged.fml          # FML 在 NeoForge 仍独立
net.minecraftforge.eventbus         → net.neoforged.bus          # 事件总线
net.minecraftforge.api.distmarker   → net.neoforged.api.distmarker
net.minecraftforge.forgespi         → net.neoforged.neoforgespi
net.minecraftforge.*                → net.neoforged.neoforge.*   # 其他默认规则
```

### 2.2 完整 132 个 Forge import 的逐项处理

#### A. `net.minecraftforge.event.*` (35 处) — 大部分 ✅ 可自动改

| Forge 1.20.1 | NeoForge 1.21.1 | 自动 |
|---|---|---|
| `net.minecraftforge.event.entity.*` (19) | `net.neoforged.neoforge.event.entity.*` | ✅ |
| `net.minecraftforge.event.TickEvent` (5) | **已删除** → 拆分为 `net.neoforged.neoforge.event.tick.{ServerTickEvent,LevelTickEvent,PlayerTickEvent,EntityTickEvent}` | ⚠️ 需手改 |
| `net.minecraftforge.event.level.*` (3) | `net.neoforged.neoforge.event.level.*` | ✅ |
| `net.minecraftforge.event.AttachCapabilitiesEvent` | **已删除** → 改用 `RegisterCapabilitiesEvent` | 🔴 重构 |
| `net.minecraftforge.event.AnvilUpdateEvent` | `net.neoforged.neoforge.event.AnvilUpdateEvent` | ✅ |
| `net.minecraftforge.event.AddReloadListenerEvent` | `net.neoforged.neoforge.event.AddReloadListenerEvent` | ✅ |
| `net.minecraftforge.event.AddPackFindersEvent` | `net.neoforged.neoforge.event.AddPackFindersEvent` | ✅ |
| `net.minecraftforge.event.RegisterCommandsEvent` | `net.neoforged.neoforge.event.RegisterCommandsEvent` | ✅ |
| `net.minecraftforge.event.PlayLevelSoundEvent` | `net.neoforged.neoforge.event.PlayLevelSoundEvent` | ✅ |
| `net.minecraftforge.event.ItemAttributeModifierEvent` | **已删除** → 改用 `ItemAttributeModifiers` data component | 🔴 重构 |
| `net.minecraftforge.event.ForgeEventFactory` | `net.neoforged.neoforge.event.EventHooks` | ⚠️ 类名变 |

#### B. `net.minecraftforge.client.*` (28 处) — ✅ 多数可自动

| Forge | NeoForge | 自动 |
|---|---|---|
| `net.minecraftforge.client.event.*` (16) | `net.neoforged.neoforge.client.event.*` | ✅ |
| `net.minecraftforge.client.model.*` (8) | `net.neoforged.neoforge.client.model.*` | ✅ |
| `net.minecraftforge.client.gui.*` (2) | `net.neoforged.neoforge.client.gui.*` | ✅ |
| `net.minecraftforge.client.ChunkRenderTypeSet` | `net.neoforged.neoforge.client.ChunkRenderTypeSet` | ✅ |
| `net.minecraftforge.client.extensions.*` | `net.neoforged.neoforge.client.extensions.*` | ✅ |

#### C. `net.minecraftforge.common.*` (29 处) — ⚠️ 半自动

| Forge | NeoForge | 自动 |
|---|---|---|
| `net.minecraftforge.common.capabilities.*` (7) | **重构** → `net.neoforged.neoforge.capabilities.*`，**API 改写** | 🔴 |
| `net.minecraftforge.common.ForgeConfigSpec` (3) | `net.neoforged.neoforge.common.ModConfigSpec` | ⚠️ 类名变 |
| `net.minecraftforge.common.util.*` (3) | `net.neoforged.neoforge.common.util.*`（其中 `LazyOptional` 已删除！） | 🔴 部分 |
| `net.minecraftforge.common.brewing.*` (3) | `net.neoforged.neoforge.common.brewing.*` | ✅ |
| `net.minecraftforge.common.command.*` (2) | `net.neoforged.neoforge.common.commands.*` | ⚠️ 命名 |
| `net.minecraftforge.common.data.*` (2) | `net.neoforged.neoforge.common.data.*` | ✅ |
| `net.minecraftforge.common.MinecraftForge` | `net.neoforged.neoforge.common.NeoForge` | ⚠️ 重命名 |
| `net.minecraftforge.common.ForgeMod` | `net.neoforged.neoforge.common.NeoForgeMod` | ⚠️ 重命名 |
| `net.minecraftforge.common.ForgeSpawnEggItem` | `net.neoforged.neoforge.common.DeferredSpawnEggItem` | ⚠️ 重命名 |
| `net.minecraftforge.common.IForgeShearable` | `net.neoforged.neoforge.common.IShearable` | ⚠️ 重命名 |
| `net.minecraftforge.common.ToolAction(s)` | `net.neoforged.neoforge.common.ItemAbility(/ies)` | 🔴 1.21 重命名 + API 改 |
| `net.minecraftforge.common.Tags` | `net.neoforged.neoforge.common.Tags` | ✅ |
| `net.minecraftforge.common.extensions.*` | `net.neoforged.neoforge.common.extensions.*` | ✅ |
| `net.minecraftforge.common.BasicItemListing` | `net.neoforged.neoforge.common.BasicItemListing` | ✅ |

#### D. `net.minecraftforge.fml.*` (11 处) — ⚠️ 注意是 `net.neoforged.fml` 不是 neoforge

| Forge | NeoForge | 自动 |
|---|---|---|
| `net.minecraftforge.fml.event.*` (3) | `net.neoforged.fml.event.*` | ✅ |
| `net.minecraftforge.fml.ModLoader` | `net.neoforged.fml.ModLoader` | ✅ |
| `net.minecraftforge.fml.ModLoadingContext` | `net.neoforged.fml.ModLoadingContext` | ✅ |
| `net.minecraftforge.fml.ModList` | `net.neoforged.fml.ModList` | ✅ |
| `net.minecraftforge.fml.DistExecutor` | **已删除** → 改用 `if (FMLEnvironment.dist == Dist.CLIENT)` | 🔴 |
| `net.minecraftforge.fml.config.*` | `net.neoforged.fml.config.*` | ✅ |
| `net.minecraftforge.fml.loading.*` | `net.neoforged.fml.loading.*` | ✅ |
| `net.minecraftforge.fml.javafmlmod.*` | `net.neoforged.fml.javafmlmod.*` | ✅ |
| `net.minecraftforge.fml.common` | `net.neoforged.fml.common` | ✅ |

#### E. `net.minecraftforge.network.*` (9 处) — 🔴 **完全重写**

`@Mod` 网络系统从 1.20.5 起彻底改了 (`CustomPayload` + `StreamCodec`)。

| Forge | NeoForge | 自动 |
|---|---|---|
| `net.minecraftforge.network.simple.SimpleChannel` | 删除，改用 `IPayloadRegistrar` | 🔴 重写 |
| `net.minecraftforge.network.NetworkRegistry` | 删除 | 🔴 |
| `net.minecraftforge.network.NetworkHooks` | `net.neoforged.neoforge.network.PacketDistributor` | 🔴 |
| `net.minecraftforge.network.NetworkEvent` (2) | 删除，改用 `IPayloadContext` | 🔴 |
| `net.minecraftforge.network.PacketDistributor` (3) | `net.neoforged.neoforge.network.PacketDistributor`（API 改） | ⚠️ |
| `net.minecraftforge.network.PlayMessages` | 删除 | 🔴 |

详见 `PACKET_AUDIT.md`。

#### F. `net.minecraftforge.registries.*` (7 处) — ⚠️ 半自动

| Forge | NeoForge | 自动 |
|---|---|---|
| `DeferredRegister` | `net.neoforged.neoforge.registries.DeferredRegister` | ✅ |
| `ForgeRegistries` | `net.neoforged.neoforge.registries.NeoForgeRegistries` 或直接用 `BuiltInRegistries` | ⚠️ |
| `RegistryObject` | `net.neoforged.neoforge.registries.DeferredHolder` | ⚠️ 重命名 |
| `IForgeRegistry` | **大多数情况已不需要** → 直接用 `Registry<T>` | 🔴 |
| `NewRegistryEvent` | `net.neoforged.neoforge.registries.NewRegistryEvent` | ✅ |
| `RegistryBuilder` | `net.neoforged.neoforge.registries.RegistryBuilder` | ✅ |

#### G. 其他散件 (~13 处)

| Forge | NeoForge | 自动 |
|---|---|---|
| `net.minecraftforge.eventbus.api.*` (5) | `net.neoforged.bus.api.*` | ⚠️ 注意改 |
| `net.minecraftforge.entity.PartEntity` | `net.neoforged.neoforge.entity.PartEntity` | ✅ |
| `net.minecraftforge.entity.IEntityAdditionalSpawnData` | `net.neoforged.neoforge.entity.IEntityWithComplexSpawn` | 🔴 重命名 + API 改 |
| `net.minecraftforge.items.IItemHandler` | `net.neoforged.neoforge.items.IItemHandler` | ✅ |
| `net.minecraftforge.server.command.*` | `net.neoforged.neoforge.server.command.*` | ✅ |
| `net.minecraftforge.server.timings` | **已删除** | 🔴 |
| `net.minecraftforge.data.event.*` | `net.neoforged.neoforge.data.event.*` | ✅ |
| `net.minecraftforge.api.distmarker.*` | `net.neoforged.api.distmarker.*` | ⚠️ |
| `net.minecraftforge.forgespi.locating.*` | `net.neoforged.neoforgespi.locating.*` | ⚠️ |

---

## 三、Vanilla MC 1.20.1 → 1.21.1 重大变化（影响 569 个 vanilla import）

> 这些不会被 import 替换脚本自动处理，需要 **AI 辅助逐文件审查 + 编译错误驱动**。

### 3.1 删除 / 重命名的类

| 1.20.1 | 1.21.1 | 影响范围 |
|---|---|---|
| `EntityDataSerializers` 中部分类型 | 部分新增/调整 | 实体同步 |
| `net.minecraft.world.entity.LivingEntity#getAttribute(Attribute)` | 改为 `Holder<Attribute>` | 大量属性代码 |
| `net.minecraft.network.chat.MutableComponent` | 基本不变 | — |
| `net.minecraft.network.protocol.Packet` 体系 | **完全重写** → `CustomPacketPayload` | 34 个 packet 全部 |
| `net.minecraft.world.item.crafting.Recipe<C>` | `C` 参数改为 `RecipeInput` | 配方代码 |
| `net.minecraft.world.item.ItemStack` 的 NBT API | **改用 DataComponents** | 物品序列化 |
| `net.minecraft.world.damagesource.DamageSource` | 改为 `Holder<DamageType>` | 伤害源 |

### 3.2 关键 API 范式变化

| 旧（1.20.1） | 新（1.21.1） | 影响 |
|---|---|---|
| `ItemStack.getTag()` | `ItemStack.get(DataComponents.CUSTOM_DATA)` | 物品 NBT 大改 |
| `ItemStack.getOrCreateTag()` | 同上 + `CustomData` | 同上 |
| `EntityType.create(level, ..., spawnReason)` 签名 | 新增 `consumer` 参数 | 实体生成 |
| `RegistryAccess.registryOrThrow(key)` | `RegistryAccess.lookupOrThrow(key)` | 注册表访问 |
| `BlockEntity#load(CompoundTag)` | `BlockEntity#loadAdditional(CompoundTag, HolderLookup.Provider)` | 方块实体 |
| `BlockEntity#saveAdditional(CompoundTag)` | `BlockEntity#saveAdditional(CompoundTag, HolderLookup.Provider)` | 同上 |

---

## 四、第三方库依赖

### 4.1 `nonamecrackers2.crackerslib.*` (17 处)

**必须先移植 crackerslib 到 1.21.1**。约 70 个 Java 文件。

详见独立任务（在 PLAN.md 列为阶段 A）。

### 4.2 `mezz.jei.api.*` (17 处)

JEI 1.21.1 版本：
- `mezz.jei.api.recipe.*` 部分 API 微调
- `IRecipeCategory` 接口签名小改

工作量低，~2-4 小时。

### 4.3 `com.mojang.blaze3d.*` (17 处)

`BufferBuilder` API 在 1.21 重写。需要替换为新的 `MeshData` 模式。

工作量中等，~4-8 小时。

### 4.4 Mixin (`org.spongepowered.asm.*`, 17 处)

**包名不变**，但 mixin target 类的 SRG/Mojmap 方法签名可能变化，需要逐个验证。

详见 `MIXIN_AUDIT.md`。

---

## 五、自动化率评估

| 类别 | 数量 | 可全自动 | 半自动 | 需手改 |
|---|---|---|---|---|
| Forge import 改名 | 132 | ~85 (64%) | ~30 (23%) | ~17 (13%) |
| Vanilla import | ~570 | ~400 (70%) | ~150 (26%) | ~20 (4%) |
| **整体** | **702** | **~485 (69%)** | **~180 (26%)** | **~37 (5%)** |

但**改完 import 后还有 API 调用层面的修改**，那才是真正吃时间的。Import 修改只是第一刀。

## 六、下一步

1. 写 `auto_migrate.ps1` 自动处理 69% 的 import 改名
2. 写 `MIXIN_AUDIT.md` 逐个 mixin 评估
3. 写 `PACKET_AUDIT.md` 网络层重构方案
4. 写 `CAPABILITY_AUDIT.md` Capability 重构方案
5. 整合到 `PLAN.md` 总路线
