# MIXIN_AUDIT.md — 60 个 Mixin 逐项迁移评估

> 扫描结果：60 个 Mixin 文件，靶向 ~50 个 vanilla 类
> Mixin 框架包名（`org.spongepowered.asm.*`）**1.21.1 不变**，主要工作是 Vanilla 目标类的方法签名变更适配

---

## 一、总览

| 类型 | 数量 | 迁移难度 |
|---|---|---|
| **Accessor 接口**（`I` 前缀，只读字段访问） | 18 | ⭐ 极简，仅需校验字段名 |
| **`@Inject` 注入**（修改方法行为） | 24 | ⭐⭐⭐ 需校验 target 方法签名 |
| **`@Redirect` 重定向** | 7 | ⭐⭐⭐⭐ 需重新核对调用点 |
| **`@ModifyArg` / `@ModifyConstant` / `@ModifyVariable`** | 3 | ⭐⭐⭐⭐⭐ 最脆弱 |
| 其他纯 Mixin（仅 `@Shadow`） | ~8 | ⭐⭐ |

---

## 二、Accessor-Only Mixin（18 个）—— 1-2 小时可全部完成

这类 Mixin 只是给 vanilla 字段开个公共访问口。NeoForge 1.21.1 中**许多字段已经通过 AT (Access Transformer) 暴露**，部分 Accessor 可以直接删除改用 AT。

| Mixin | Target Class | 1.21.1 状态 | 行动 |
|---|---|---|---|
| `IMixinBlockModelRenderer` | `ModelBlockRenderer` | 类名不变 | 校验字段名 |
| `IMixinBrain` | `Brain` (5 accessor) | 类名不变 | 校验 5 字段名 |
| `IMixinCreeper` | `Creeper` (2 accessor) | 类名不变 | 校验 `swell`、`oldSwell` |
| `IMixinCubeDefinition` | `CubeDefinition` (6 accessor) | 类名不变 | 校验 |
| `IMixinEntityModelSet` | `EntityModelSet` | 类名不变 | 校验 `roots` 字段 |
| `IMixinGameRenderer` | `GameRenderer` | 类名不变 | 校验 |
| `IMixinJigsawPlacement` | `JigsawPlacement` | 类名不变，但 1.21 内部重构过 | ⚠️ 重点核对 |
| `IMixinLayerDefinition` | `LayerDefinition` | 类名不变 | 校验 `mesh` |
| `IMixinLightTexture` | `LightTexture` | 类名不变 | 校验 |
| `IMixinModelPart` | `ModelPart` (2 accessor) | 类名不变 | 校验 |
| `IMixinOverlayTexture` | `OverlayTexture` | 类名不变 | 校验 |
| `IMixinPartDefinition` | `PartDefinition` (2 accessor) | 类名不变 | 校验 |
| `IMixinPhantom` | `Phantom` (4 accessor) | 类名不变 | 校验 |
| `IMixinPostChain` | `PostChain` | **1.21 大改** | 🔴 需重新审视 |
| `IMixinShulkerBullet` | `ShulkerBullet` (2 accessor) | 类名不变 | 校验 |
| `IMixinStructurePiecesBuilder` | `StructurePiecesBuilder` | 类名不变 | 校验 |
| `IMixinSynchedEntityData` | `SynchedEntityData` | **1.20.5 大改** | 🔴 需重写 |
| `IMixinZombieVillager` | `ZombieVillager` (2 accessor) | 类名不变 | 校验 |

**预期工时**：每个 5-15 分钟 = 总 2-4 小时

---

## 三、`@Inject` Mixin（24 个）—— 主要工作量

这类需要：
1. 验证 target class 还存在
2. 验证 target method 的 SRG/Mojmap 签名
3. 验证 `@At` 注入点偏移仍然合法
4. 如有 `LocalCapture` 还要核对局部变量

| Mixin | Target | 注入数 | 1.21 兼容性 |
|---|---|---|---|
| `MixinAbstractSkeleton` | `AbstractSkeleton` | 1 | ✅ 基本不变 |
| `MixinBee` | `Bee` | 1 | ✅ 基本不变 |
| `MixinBeePollinateGoal` | `Bee$BeePollinateGoal` (内部类) | 1 | ⚠️ 内部类需重新核对 |
| `MixinBellBlock` | `BellBlock` | 1 | ✅ |
| `MixinClientLevel` | `ClientLevel` | 2 inj + 2 red | ⚠️ 1.21 有 chunk 改动 |
| `MixinCrossbowItem` | `CrossbowItem` | 2 | ⚠️ 1.21 弩重写过 |
| `MixinDragonFireball` | `DragonFireball` | 2 | ✅ |
| `MixinEntityRenderDispatcher` | `EntityRenderDispatcher` | 1 | ⚠️ render 系统局部变化 |
| `MixinGameRenderer` | `GameRenderer` | 3 | ⚠️ 1.21 改动较多 |
| `MixinGui` | `Gui` | 2 inj + 1 mod | 🔴 1.21 Gui 大改 |
| `MixinItemInHandRenderer` | `ItemInHandRenderer` | 1 | ✅ |
| `MixinLevelRenderer` | `LevelRenderer` | 1 | 🔴 1.21 大改 |
| `MixinLivingEntity` | `LivingEntity` | 6 | ⚠️ 属性系统 Holder 化 |
| `MixinLivingEntityRenderer` | `LivingEntityRenderer` | 1 | ✅ |
| `MixinMob` | `Mob` | 2 | ✅ |
| `MixinMusicManager` | `MusicManager` | 1 | ✅ |
| `MixinPigRenderer` | `PigRenderer` | 1 | ⚠️ 模型类微改 |
| `MixinPlayer` | `Player` | 1 inj + 1 red | ⚠️ |
| `MixinPointedDripstoneBlock` | `PointedDripstoneBlock` | (无注入但是 Mixin) | ✅ |
| `MixinSnowball` | `Snowball` | 1 | ✅ |
| `MixinSplashManager` | `SplashManager` | 1 | ✅ |
| `MixinSwellGoal` | `SwellGoal` | 2 | ✅ |
| `MixinTextureAtlas` | `TextureAtlas` | 1 | ⚠️ |
| `MixinVillager` | `Villager` | 1 | ✅ |

**预期工时**：平均 30-60 分钟/个 = **12-24 小时**

---

## 四、`@Redirect` Mixin（7 个）—— 最脆弱

`@Redirect` 依赖具体的方法调用 INVOKE 指令，1.21 任何方法签名变化都会失效。

| Mixin | Target | 重定向 |
|---|---|---|
| `MixinClientLevel` | `ClientLevel` | 2 处 |
| `MixinFishingHook` | `FishingHook` | 1 处 |
| `MixinItemEntity` | `ItemEntity` | 1 处 |
| `MixinMobEffect` | `MobEffect` | 2 处 - 🔴 1.21 效果系统改 Holder |
| `MixinPlayer` | `Player` | 1 处 |
| `MixinPlayerItemInHandLayer` | `PlayerItemInHandLayer` | 1 处 |
| `MixinTitleScreen` | `TitleScreen` | 1 处 |

**预期工时**：每个 60-120 分钟 = **7-14 小时**

---

## 五、`@Modify*` Mixin（3 个）

| Mixin | Target | 类型 |
|---|---|---|
| `MixinGui` | `Gui` | ModifyConstant |
| `MixinPanoramaRenderer` | `PanoramaRenderer` | ModifyArg |
| `MixinPrimedTnt` | `PrimedTnt` | ModifyConstant |

**预期工时**：每个 30-90 分钟 = **2-5 小时**

---

## 六、🔴 高风险 Mixin（需特别关注）

| Mixin | 原因 |
|---|---|
| `IMixinPostChain` | 1.21 后处理着色器系统重写 |
| `IMixinSynchedEntityData` | 1.20.5+ 实体数据同步重构 |
| `MixinGui` | 1.21 Gui/HUD 大量重构 |
| `MixinLevelRenderer` | 1.21 渲染管线变化 |
| `MixinLivingEntity` | 1.21 属性系统 Holder 化 |
| `MixinMobEffect` | 1.21 状态效果系统 Holder 化 |

这 6 个 Mixin 可能需要**重写**而非简单适配。

---

## 七、可能可以删除的 Mixin

`MixinPigRenderer`、`MixinTitleScreen` 等渲染层 mixin，NeoForge 通常提供事件 hook 替代。S1 移植期可以先**保留**，跑通后再考虑改用事件。

---

## 八、迁移策略

1. **先做 Accessor**（18 个，~3 小时）：风险低、产出高，建立信心
2. **再做简单 `@Inject`**（如 Bee/Snowball/Mob，~6 个，~3 小时）
3. **接着做中等 `@Inject`**（约 15 个，~10 小时）
4. **最后攻坚 6 个高风险**（~10-15 小时）
5. **最后处理 `@Redirect` / `@Modify`**（~10 小时）

**预期总工时**：35-55 小时（AI 主力 + 人工审核）

---

## 九、行动建议

- **暂时禁用** `MixinPostChain` 和 `MixinSynchedEntityData`，先让其他部分编译过
- 用 `MixinExtras` 取代部分 `@Redirect`（更稳定，1.21.1 普遍使用）
- 编译错误驱动：先全部复制到 mdk，跑 `gradle compileJava`，按错误清单逐个解决
