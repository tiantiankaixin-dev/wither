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

