# 凋零风暴 Mod 1.21.1 迁移 · 进度记录

> 实时更新 · AI 主导分析阶段
> 启动时间：2026-05-23 05:55 (UTC+08:00)

---

## 当前状态：✅ 分析层完成 + 第一轮自动迁移完成（AI 独立完成）

**完成时间**：2026-05-23 ~06:30 UTC+08:00

**👉 直接看 [PLAN.md](./PLAN.md) 了解下一步**

### 已确认事实

- ✅ **GitHub 仓库**：https://github.com/tiantiankaixin-dev/wither.git
  - 最新 commit：`7fe6e0d Add clean 1.20.1 source project` (tian, 7 小时前)
  - **队友实际迁移进度：0%** —— 只是上传了干净的 1.20.1 源码
- ✅ 本地 `wither-main/` 与 GitHub 一致（无 git 历史，是裸文件副本）
- ✅ 本地另有 `wither-github-latest/` 带 git 的克隆，作为分析的 canonical 源
- ✅ 工作区已有完整反编译产物（`decompiled/witherstormmod/`）
- ✅ NeoForge 1.21.1 MDK 已配置（`mdk/`），但只含默认 examplemod 模板

### 源码规模

| 项 | 数量 |
|---|---|
| `witherstormmod` Java 文件 | 610 |
| Mixin 文件（位于 `nonamecrackers2/witherstormmod/mixin/`） | 60 |
| Packet（待扫描确认） | ~34 |
| Capability（待扫描确认） | ~7 |
| 资源文件（贴图/模型/声音/lang） | ~1300 |
| 依赖库 `crackerslib` Java 文件 | ~70 |

### 目标版本矩阵

| 项 | 源（已有） | 目标 |
|---|---|---|
| Minecraft | 1.20.1 | 1.21.1 |
| Loader | Forge 47.1.x | NeoForge 21.1.228 |
| Java | 17 | 21 |
| Mod 版本号 | 4.2.1 | 1.21.1-4.2.1-port.0.0.1 |

---

## 文档导航

| 文档 | 用途 | 状态 |
|---|---|---|
| `PROGRESS.md` (本文档) | 实时进度记录 | ✅ 完成 |
| `PLAN.md` | 总路线图 + 可勾选 checklist | ✅ 完成 ⭐ **醒来先看这个** |
| `IMPORT_MAP.md` | Forge → NeoForge import 映射表 | ✅ 完成 |
| `MIXIN_AUDIT.md` | 60 个 Mixin 的逐个迁移评估 | ✅ 完成 |
| `PACKET_AUDIT.md` | 网络包重写方案 | ✅ 完成 |
| `CAPABILITY_AUDIT.md` | Capability 重构方案 | ✅ 完成 |
| `auto_migrate.ps1` | 自动改写工具脚本 | ✅ 已跑过一轮 |
| `BLOCKERS.md` | 遇到无法处理的问题 | ⚫ 未生成（无阻塞） |

---

## 时间线（AI 自动推进）

| 时间 (UTC+8) | 动作 | 产出 |
|---|---|---|
| 05:55 | 启动分析，clone GitHub 仓库 | `wither-github-latest/` |
| 06:00 | 扫描 610 个 .java 的 1568 个 import | `raw_imports.txt`, `IMPORT_MAP.md` |
| 06:08 | 扫描 60 个 Mixin | `mixin_raw.txt`, `MIXIN_AUDIT.md` |
| 06:13 | 扫描 34 个 Packet | `packet_raw.txt`, `PACKET_AUDIT.md` |
| 06:18 | 扫描 7 个 Capability | `CAPABILITY_AUDIT.md` |
| 06:22 | 第一版 `auto_migrate.ps1` 报错（PowerShell 哈希表 bug） | 修正 |
| 06:25 | 第二版用 robocopy + in-place 修改成功 | 610 文件落 mdk |
| 06:28 | 验证 PlayerMotionMessage.java 已被转换 | TODO_MIG 标记可见 |
| 06:30 | 总 PLAN.md 完成 | 本文档 |

## 最终统计

- **源文件**：610 个 .java
- **已落地到 mdk**：610 个（100%）
- **已自动修改**：185 个（30%）
- **import 改名**：563 处
- **call-site 改名**：420 处
- **TODO_MIG 标记**：140 处（散落在 67 个文件）

## 已知问题 / 注意

1. PowerShell 脚本之前因哈希表语法问题报错，最终改用 robocopy + in-place 方式，工作正常
2. `mdk/build/libs/` 不会立刻有产出——还有大量编译错误待修
3. `mods.toml` / `neoforge.mods.toml` 未修改（仍是 examplemod 模板）——需要手写
4. `resources/` 未复制——见 PLAN.md 动作 4
5. crackerslib 未迁移（前置依赖）——见 PLAN.md 阶段 A

---

## Phase-2 进度（2026-05-24 ~03:30 UTC+8）

### 已完成

| 动作 | 结果 |
|---|---|
| 修复 BOM 编码 bug | 185 个文件去 BOM，避免 javac 拒绝 |
| 复制资源文件 | 1356 个 .png/.json/.ogg 已落地 mdk |
| 配置 `neoforge.mods.toml` | 启用 mixin + AT，写入 mod 元数据 |
| 修复 `import // TODO_MIG;` 语法错误 | 123 行注释化（合法 Java） |
| 跑首次 `gradlew compileJava` | **5234 个真实错误，散落在 329 个文件** |
| Phase-2 自动迁移 | -490 错误，-37 个出错文件 |
| 推送到 GitHub | commit `bc02d13` 已上 port-1.21.1 |

### 当前编译状态：**4744 错误 / 292 文件**

### 剩余错误模式（按优先级）

| 模式 | 数量 | 处理策略 |
|---|---|---|
| `Capabilities.*` 路径不对 | 598 | 手动校正，或单独写一轮 |
| `VertexConsumer.vertex(Matrix4f)` | 320 | 改为 `addVertex(Matrix4f,x,y,z)` 可自动 |
| `BuiltInRegistries.*` 用法 | 266 | 部分调用要 `.getValue()` 或 `.get().value()` |
| `crackerslib` 不存在 | 212 | **需要先迁移 crackerslib 库** |
| `MobType` 删除 | 199 | 手动删除 `getMobType()` 重写（1.21 用 entity tags 替代） |
| `PacketDistributor.PacketTarget` | 94 | 手动改写为 `PacketDistributor.SERVER`/`PLAYER` 等 |
| `Crackiness` 内部类导入 | 78 | 部分修了，剩余手动 |
| `TickEvent` 拆分 | 78 | 手动加 `.Pre` / `.Post` 后缀 |
| `renderToBuffer` 签名变化 | 58 | 模型类需删除 `int color` 参数 |
| `ModelPart.render` 签名变化 | 52 | 同上 |

### 工时复盘

| 阶段 | 实际工时 |
|---|---|
| 0-30 min: 分析 + 第一轮自动迁移 | 30 min |
| 30-90 min: 修 bug（BOM、PowerShell hashtable）+ 资源复制 | 60 min |
| 90-150 min: Phase-2 自动迁移 + 编译验证 | 60 min |
| **合计 AI 工时** | **2.5 小时** |

### 下一步建议

**短期（明天）**：
1. 迁移 crackerslib（70 文件，预计 4-6h AI 工时）— 是 witherstormmod 编译的硬前置
2. 写 phase-3 自动脚本处理 `VertexConsumer.vertex(Matrix4f)`（320 错误一波清）

**中期**：
1. 手动处理 `MobType` 删除（199 错误）— 涉及游戏逻辑判断
2. 重写 Packet 系统（参见 PACKET_AUDIT.md）— 解决 `PacketDistributor` 94 错误
3. 重构 Capability → Data Attachment（参见 CAPABILITY_AUDIT.md）— 解决 598 + 264 错误

**里程碑预期**：
- 编译错误归零：~30-50 小时 AI + 人工
- runClient 启动：再 ~10-20 小时调试
- "召唤凋零风暴 + 一阶段战斗"：再 ~15-25 小时

---

## Phase-3 进度（2026-05-24 ~04:00 UTC+8）

### 已完成

| 动作 | 结果 |
|---|---|
| 嵌入 crackerslib 60 个反编译文件到 mdk 源码树 | `mdk/src/main/java/nonamecrackers2/crackerslib/` |
| 写 `auto_migrate_inplace.ps1` | 合并 phase-1+2 规则、in-place 模式、类名级重命名 |
| 修非幂等 bug（`LazyOptional.empty()` 嵌套注释） | 4 个文件清理 |
| 全树重跑（670 文件） | 49 文件改了 130 处，主要是 `ForgeConfigSpec`→`ModConfigSpec` 类名引用 |
| 本地 commit | 89 文件变更已 commit（push 因网络暂未成功） |

### 当前编译状态：5286 错误 / 324 文件

| 子项 | 错误数 |
|---|---|
| crackerslib（60 文件） | 1246（每文件 ~9 个 API 改动） |
| witherstormmod（610 文件） | 4040 |

### 净进展（横向对比）

```
phase-2 末（无 crackerslib，610 文件）：4744 错误
phase-3 末（含 crackerslib，670 文件）：5286 错误
新增 60 个 crackerslib 文件贡献：~542 错误
即：把 212 个「找不到包」换成了「具体哪个 API 改了」的细粒度错误
```

### 剩下的硬骨头（必须手写）

| 系统 | 影响范围 | 复杂度 |
|---|---|---|
| Capability API 重构 | crackerslib 40 + witherstormmod 600+ 错误 | ★★★★ 重写 RegisterCapabilitiesEvent + BlockCapability/EntityCapability/ItemCapability |
| Packet API 重构 | crackerslib 2 个核心类 + 33 个子类 | ★★★★ `Packet` → `CustomPacketPayload`，`NetworkEvent.Context` → `IPayloadContext` |
| AttachCapabilitiesEvent 删除 | crackerslib 12 + 多处下游 | ★★★ 必须用 RegisterCapabilitiesEvent 替代 |
| ForgeConfigSpec 方法签名变化 | crackerslib config GUI 子系统 | ★★ 检查 ModConfigSpec 新 API |
| DistExecutor 删除 | 全树多处 | ★★ 改为 `if (FMLEnvironment.dist == Dist.CLIENT)` |
| NonNullSupplier 删除 | crackerslib 14 处 | ★★ 换 `Supplier<@NotNull T>` |

### 自动化能力已耗尽

3 轮自动迁移后，**剩下的错误全部需要语义层面的人工/AI 协作改造**，机械替换无法继续推进。

### 工时复盘（本夜累计）

| 阶段 | 工时 |
|---|---|
| Phase-1 + Phase-2 | 2.5h |
| Phase-3（crackerslib 嵌入 + 类名规则） | 0.5h |
| **本夜总计** | **3h** |

### 醒来后的建议下一步（按优先）

1. **Capability 系统重写**（最大解锁，~600+ 错误一波清）
   - 读 `notes/MIGRATION/CAPABILITY_AUDIT.md`
   - 重写 `nonamecrackers2/crackerslib/common/capability/CapUtil.java`（删除 ICapabilityProvider，改用 NeoForge 1.21 的 `Capabilities.<Kind>.<event>` API）
   - 重写 witherstormmod 中所有 `AttachCapabilitiesEvent` 处理器为 `RegisterCapabilitiesEvent`
2. **Packet 系统重写**（核心 2 文件，下游 33 文件模板化）
   - 读 `notes/MIGRATION/PACKET_AUDIT.md`
   - 把 `Packet` 改造成实现 `CustomPacketPayload` 的抽象类
   - 33 个子类批量加 `StreamCodec<RegistryFriendlyByteBuf, Self>` 静态字段
3. **DistExecutor / NonNullSupplier 清理**（小批量手动）

---

