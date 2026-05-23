# PLAN.md — 凋零风暴 Mod 1.21.1 迁移总路线图

> v1.0 · AI 完成分析层 + 第一轮自动迁移 · 2026-05-23

---

## 当前状态（AI 已完成的事）

| 项 | 状态 |
|---|---|
| GitHub 仓库 clone 到 `wither-github-latest/` | ✅ |
| 全量分析文档（IMPORT_MAP / MIXIN / PACKET / CAPABILITY） | ✅ |
| `auto_migrate.ps1` 自动改写脚本 | ✅ |
| **第一轮自动迁移到 `mdk/src/main/java/nonamecrackers2/`** | ✅ |
| 待人工处理的标记 | **140 处 `TODO_MIG`，散落在 67 个文件** |

---

## 剩余工作量预估

| 阶段 | 工作 | 预期工时（AI + 人工） |
|---|---|---|
| A | crackerslib 移植 | 8-15h |
| B | 处理 140 个 `TODO_MIG` | 10-20h |
| C | 重写 Packet 基类 + 33 个子类 | 11-21h |
| D | Capability → Data Attachment 重构（7 个） | 15-25h |
| E | Mixin 适配（60 个） | 35-55h |
| F | Vanilla API 1.20.1→1.21.1 迁移（散落各文件） | 30-50h |
| G | 第一次成功 `runClient` 调试 | 10-20h |
| H | 跑通"召唤凋零风暴 + 一阶段战斗" | 10-30h |
| I | 兼容 Create / Aeronautics | 20-40h |
| **合计** | | **~150-275 小时** |

按"AI 主力 + 人工审核 1-2 周专注"算，乐观 6 周可达里程碑 H。

---

## 阶段 A：移植 crackerslib（依赖前置）

`witherstormmod` 依赖 `crackerslib`。**必须先把 crackerslib 移植到 1.21.1**，否则 witherstormmod 无法编译。

### 步骤

1. 创建子模块 `mdk-crackerslib/`（或同一 mdk 项目下增加 source set）
2. 把 `decompiled/crackerslib/` 的源码（70 个文件）复制进去
3. 用同一 `auto_migrate.ps1`（改 SourceDir）跑一遍
4. 处理 crackerslib 自己的 TODO_MIG
5. 把 `Packet` 抽象基类按 PACKET_AUDIT.md 第三节的目标架构重写
6. 构建并产出 `crackerslib-1.21.1-x.x.jar`
7. 在 witherstormmod 的 `build.gradle` 添加这个 jar 为依赖

### 关键文件清单

```
decompiled/crackerslib/
├── nonamecrackers2/crackerslib/common/packet/Packet.java    ← 必改
├── nonamecrackers2/crackerslib/common/packet/PacketHandler.java
├── nonamecrackers2/crackerslib/common/event/...
└── ...（其他工具类）
```

---

## 阶段 B：处理 140 个 TODO_MIG

`auto_migrate.ps1` 在以下情况插入了 TODO_MIG 标记：

| TODO 来源 | 推荐处理 |
|---|---|
| `DistExecutor` (~10 处) | 替换为 `if (FMLEnvironment.dist == Dist.CLIENT) { ... }` |
| `AttachCapabilitiesEvent` (~3 处) | 见 CAPABILITY_AUDIT，改用 RegisterCapabilitiesEvent + Data Attachment |
| `TickEvent` (~5 处) | 按用途拆分到 ServerTickEvent / LevelTickEvent / PlayerTickEvent / EntityTickEvent |
| `NetworkRegistry/Hooks/Event` (~9 处) | 见 PACKET_AUDIT，删除并改用 IPayloadRegistrar + IPayloadContext |
| `LazyOptional` (~20+ 处) | 删除，新 Capability 直接返回 T 或 null |
| `ItemAttributeModifierEvent` (~1 处) | 改用 `DataComponents.ATTRIBUTE_MODIFIERS` |
| `server.timings` (~1 处) | 删除 |

### 操作建议

```bash
# 在 IntelliJ 中：
1. 按 Ctrl+Shift+F 全局搜 "TODO_MIG"
2. 按出现顺序逐个处理
3. 每改一处就 Ctrl+S，让编译器告诉你下一个错
```

---

## 阶段 C：重写 Packet 系统

详见 `PACKET_AUDIT.md`。

**关键产出**：
- `crackerslib.common.packet.Packet`（基类，实现 `CustomPacketPayload`）
- 33 个 Message 子类（AI 模板化批量生成）
- `WitherStormModMessageHandlerServer`（注册中心）

---

## 阶段 D：Capability → Data Attachment

详见 `CAPABILITY_AUDIT.md`。

**7 个文件**：
1. `EntityCapability` (基类) — 重写为 Data Attachment 工具
2. `PlayerWitherStormData` — AttachmentType + copyOnDeath
3. `WitherSicknessTracker` — AttachmentType
4. `WitherStormAutoSpawner` — SavedData
5. `WitherStormBowelsManager` — AttachmentType（在凋零风暴实体上）
6. `ChunkLoadingBlockEntities` — Level 级 AttachmentType
7. `WitherStormModChunkLoader` — SavedData

---

## 阶段 E：Mixin 适配

详见 `MIXIN_AUDIT.md`。

**60 个 Mixin** 按优先级处理：
1. 先做 18 个 Accessor 接口（~3h）
2. 再做简单 `@Inject` Mixin（~6 个，~3h）
3. 中等 `@Inject`（~15 个，~10h）
4. 6 个高风险 Mixin（~10-15h）
5. 7 个 `@Redirect` 和 3 个 `@Modify*`（~17h）

**高风险 Mixin（可能需要完全重写）**：
- `MixinPostChain`（1.21 后处理着色器系统大改）
- `MixinSynchedEntityData`（1.20.5+ 数据同步重构）
- `MixinGui` / `MixinLevelRenderer`（1.21 渲染大改）
- `MixinLivingEntity` / `MixinMobEffect`（属性 Holder 化）

---

## 阶段 F：Vanilla API 1.20.1→1.21.1

大量散落变更，主要由编译错误驱动：

1. `ItemStack.getTag()/setTag()` → DataComponents
2. `Recipe<Container>` → `Recipe<RecipeInput>`
3. `BlockEntity#load/saveAdditional` 加 `HolderLookup.Provider` 参数
4. `RegistryAccess.registryOrThrow()` → `lookupOrThrow()`
5. `EntityType.create()` 增加 `consumer` 参数
6. `DamageSource` → `Holder<DamageType>`
7. `BufferBuilder` → `MeshData` 模式
8. Attribute 改为 `Holder<Attribute>`
9. MobEffect 改为 `Holder<MobEffect>`

---

## 阶段 G-I：调试、首次启动、兼容性

略——这些阶段依赖前面成功完成。

---

## 现在能立即开始的 4 个动作

### 动作 1：让 mdk 能 import 解析（节省 IDE 报错噪音）

修改 `mdk/build.gradle`，添加 `crackerslib` 占位：

```gradle
dependencies {
    implementation 'net.neoforged:neoforge:21.1.228'
    // 占位 - 等阶段 A 完成后改成真实 jar
    // implementation files('../mdk-crackerslib/build/libs/crackerslib-1.21.1-0.5.0.jar')
}
```

### 动作 2：grep 第一批 TODO_MIG

```powershell
cd c:\Users\ROG\Desktop\Projects\crackers-wither-storm-modupdate\mdk\src\main\java
Select-String -Recurse -Pattern "TODO_MIG" -Include *.java | Group-Object Filename | Sort-Object Count -Descending | Select-Object Count, Name -First 20
```

### 动作 3：跑第一次 gradle compile 看真实错误

```powershell
cd c:\Users\ROG\Desktop\Projects\crackers-wither-storm-modupdate\mdk
.\gradlew compileJava --warning-mode=none 2>&1 | Out-File first-compile-errors.log
```

**预期**：会有数百个错误。这是好事——给我们一份**真实的待办清单**。

### 动作 4：复制资源文件

```powershell
robocopy `
  c:\Users\ROG\Desktop\Projects\crackers-wither-storm-modupdate\wither-github-latest\src\main\resources `
  c:\Users\ROG\Desktop\Projects\crackers-wither-storm-modupdate\mdk\src\main\resources `
  /E /XO /XF mods.toml accesstransformer.cfg  # 排除可能与 NeoForge 模板冲突的元数据文件
```

注意：`mods.toml` 不复制（NeoForge 1.20.5+ 用 `neoforge.mods.toml`，需要手写）。

---

## 风险 / 已知问题

1. **PowerShell 脚本路径计算曾出 bug**——已修复，改用 robocopy + 就地修改
2. **`PostChain` / `SynchedEntityData` 类 Mixin 可能需要完全重写**——风险高
3. **Sable mod 缺失**（见 ENV.md）——运行时阻塞，需要手动从 Modrinth 下载
4. **crackerslib 反编译可能有信息丢失**——尽量用 GitHub 原始仓库源码（如能找到 nonamecrackers2 的 crackerslib 开源版）

---

## 文件清单

| 路径 | 内容 |
|---|---|
| `notes/MIGRATION/PROGRESS.md` | 实时进度（最新状态） |
| `notes/MIGRATION/PLAN.md` | 本文档（总路线） |
| `notes/MIGRATION/IMPORT_MAP.md` | Forge→NeoForge import 映射表（132 个映射） |
| `notes/MIGRATION/MIXIN_AUDIT.md` | 60 个 Mixin 逐项评估 |
| `notes/MIGRATION/PACKET_AUDIT.md` | 34 个 Packet 重构方案 |
| `notes/MIGRATION/CAPABILITY_AUDIT.md` | 7 个 Capability 重构方案 |
| `notes/MIGRATION/auto_migrate.ps1` | 自动迁移脚本（已成功跑过一次） |
| `notes/MIGRATION/raw_imports.txt` | 1568 个 import 原始扫描 |
| `notes/MIGRATION/mixin_raw.txt` | 60 个 mixin 扫描原始数据 |
| `notes/MIGRATION/packet_raw.txt` | 34 个 packet 扫描原始数据 |
| `mdk/src/main/java/nonamecrackers2/` | **第一轮迁移后的源码（610 个文件，185 个已自动修改）** |

---

## Changelog

- **v1.0 (2026-05-23 ~06:30 UTC+08)** — AI 完成分析层 + 第一轮自动迁移
