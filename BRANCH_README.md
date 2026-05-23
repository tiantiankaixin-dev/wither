# port-1.21.1 分支说明

> WIP（Work In Progress）—— 1.20.1 Forge → 1.21.1 NeoForge 移植
> 创建时间：2026-05-23

## 目录结构

```
.
├── src/                    # 旧 1.20.1 Forge 源码（保留作为对照参考，不要在此修改）
├── mdk/                    # 新 1.21.1 NeoForge 工程（迁移后的目标）
│   ├── src/main/java/      #   ← 移植后的 .java 源码，含 140 个 TODO_MIG 标记
│   ├── src/main/resources/ #   ← 尚未复制，需要从 ../src/main/resources/ 同步
│   ├── build.gradle        #   NeoForge 21.1.228 模板，未配置 crackerslib 依赖
│   └── ...
└── notes/MIGRATION/        # 迁移过程的全套分析文档
    ├── PLAN.md             #   ⭐ 总路线图，先看这个
    ├── PROGRESS.md         #   进度记录
    ├── IMPORT_MAP.md       #   Forge→NeoForge import 映射
    ├── MIXIN_AUDIT.md      #   60 个 Mixin 评估
    ├── PACKET_AUDIT.md     #   34 个 Packet 重构方案
    ├── CAPABILITY_AUDIT.md #   Capability 重构方案
    └── auto_migrate.ps1    #   自动迁移脚本
```

## 当前状态

| 项 | 数值 |
|---|---|
| 源代码文件 | 610 个 .java 已落地 |
| 已自动修改 | 185 个文件 |
| import 改名 | 563 处 |
| call-site 改名 | 420 处 |
| 待人工处理标记 | 140 个 `TODO_MIG`（散落在 67 个文件） |
| 编译状态 | **尚未尝试 compileJava**，预计大量错误 |

## 下一步（按优先级）

1. 移植 `crackerslib` 到 1.21.1（前置依赖库，70 文件）
2. 处理 140 个 `TODO_MIG` 标记
3. 重写 Packet 系统（33 个 Message + 基类）
4. 重构 Capability 为 Data Attachment
5. 适配 60 个 Mixin 到 1.21.1 vanilla 类
6. 修复 vanilla 1.20.1→1.21.1 的 API 散落变更
7. 首次 `runClient` 调试

详见 `notes/MIGRATION/PLAN.md`。

## 注意事项

- `mdk/src/main/resources/` 还是 NeoForge examplemod 模板，**未复制游戏资源**
- `mdk/build.gradle` 仍是 NeoForge 默认配置，**未声明 crackerslib 依赖**
- 现在直接 `gradlew compileJava` 会报很多错（这是预期的，用错误清单驱动后续工作）
