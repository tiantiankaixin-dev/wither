<#
.SYNOPSIS
    Phase-2 mechanical fix: high-frequency 1.21.1 vanilla API patterns.

.DESCRIPTION
    Targets the top 10 most-frequent compile errors:
    1. ResourceLocation("ns", "path")          -> ResourceLocation.fromNamespaceAndPath("ns", "path")
    2. ForgeRegistries.X.get(rl)               -> BuiltInRegistries.X.get(rl)         (best-effort)
    3. ForgeRegistries.X                       -> BuiltInRegistries.X                  (best-effort)
    4. import Crackiness                       -> import IronGolem.Crackiness        
    5. Cancelable annotation                   -> implements ICancellableEvent
    6. Tick events: import TickEvent           -> wired to new packages
    7. ChunkStatus                             -> ChunkStatus stays but moved
    8. SpawnPlacements.Type                    -> SpawnPlacementType
    9. import LevelTickEvent etc               -> proper import paths
    10. crackerslib stub generation (so non-crackerslib errors can be seen)

.PARAMETER TargetDir
    The mdk source root.

.PARAMETER DryRun
    Preview changes without writing.

.EXAMPLE
    .\auto_migrate_phase2.ps1 -DryRun
    .\auto_migrate_phase2.ps1
#>

param(
    [string]$TargetDir = "c:\Users\ROG\Desktop\Projects\crackers-wither-storm-modupdate\mdk\src\main\java\nonamecrackers2",
    [switch]$DryRun
)

# Each rule = [pattern, replacement, optional-comment]
$Rules = @(
    # === ResourceLocation private constructor ===
    # Match "new ResourceLocation(<arg>, <arg>)" -> "ResourceLocation.fromNamespaceAndPath(...)"
    @('new\s+ResourceLocation\s*\(\s*("(?:[^"\\]|\\.)*"|[\w\.]+)\s*,\s*("(?:[^"\\]|\\.)*"|[\w\.]+)\s*\)', 'ResourceLocation.fromNamespaceAndPath($1, $2)'),
    # Single-arg variant "new ResourceLocation("foo:bar")"
    @('new\s+ResourceLocation\s*\(\s*("(?:[^"\\]|\\.)*")\s*\)', 'ResourceLocation.parse($1)'),

    # === ForgeRegistries -> BuiltInRegistries (vanilla equivalent) ===
    @('ForgeRegistries\.ITEMS\b',                    'BuiltInRegistries.ITEM'),
    @('ForgeRegistries\.BLOCKS\b',                   'BuiltInRegistries.BLOCK'),
    @('ForgeRegistries\.ENTITY_TYPES\b',             'BuiltInRegistries.ENTITY_TYPE'),
    @('ForgeRegistries\.MOB_EFFECTS\b',              'BuiltInRegistries.MOB_EFFECT'),
    @('ForgeRegistries\.SOUND_EVENTS\b',             'BuiltInRegistries.SOUND_EVENT'),
    @('ForgeRegistries\.PARTICLE_TYPES\b',           'BuiltInRegistries.PARTICLE_TYPE'),
    @('ForgeRegistries\.BLOCK_ENTITY_TYPES\b',       'BuiltInRegistries.BLOCK_ENTITY_TYPE'),
    @('ForgeRegistries\.ENCHANTMENTS\b',             'BuiltInRegistries.ENCHANTMENT'),
    @('ForgeRegistries\.POTIONS\b',                  'BuiltInRegistries.POTION'),
    @('ForgeRegistries\.MENU_TYPES\b',               'BuiltInRegistries.MENU'),
    @('ForgeRegistries\.RECIPE_SERIALIZERS\b',       'BuiltInRegistries.RECIPE_SERIALIZER'),
    @('ForgeRegistries\.RECIPE_TYPES\b',             'BuiltInRegistries.RECIPE_TYPE'),
    @('ForgeRegistries\.VILLAGER_PROFESSIONS\b',     'BuiltInRegistries.VILLAGER_PROFESSION'),
    @('ForgeRegistries\.POI_TYPES\b',                'BuiltInRegistries.POINT_OF_INTEREST_TYPE'),
    @('NeoForgeRegistries\.',                        'BuiltInRegistries.'),

    # IForgeRegistry<T> -> Registry<T>
    @('\bIForgeRegistry\b',                          'Registry'),

    # === SpawnPlacements.Type -> SpawnPlacementType ===
    @('SpawnPlacements\.Type\b',                     'SpawnPlacementTypes'),

    # === IronGolem.Crackiness -> IronGolem.Crackiness (the import path is what changed) ===
    # This stays the same; what's broken is the import. Handle via import-line replacement below.

    # === Cancelable annotation ===
    @('@Cancelable\s*\r?\n',                         "// TODO_MIG[CANCELABLE]: implement ICancellableEvent on this class instead`r`n"),
    @('import\s+net\.neoforged\.bus\.api\.Cancelable\s*;',  '// TODO_MIG[CANCELABLE]: removed; class must implement ICancellableEvent'),

    # === TickEvent imports (the classes moved) ===
    @('import\s+net\.neoforged\.neoforge\.event\.TickEvent\.LevelTickEvent\s*;', 'import net.neoforged.neoforge.event.tick.LevelTickEvent;'),
    @('import\s+net\.neoforged\.neoforge\.event\.TickEvent\.ClientTickEvent\s*;', 'import net.neoforged.neoforge.client.event.ClientTickEvent;'),
    @('import\s+net\.neoforged\.neoforge\.event\.TickEvent\.PlayerTickEvent\s*;', 'import net.neoforged.neoforge.event.tick.PlayerTickEvent;'),
    @('import\s+net\.neoforged\.neoforge\.event\.TickEvent\.ServerTickEvent\s*;', 'import net.neoforged.neoforge.event.tick.ServerTickEvent;'),
    @('import\s+net\.neoforged\.neoforge\.event\.TickEvent\.WorldTickEvent\s*;', 'import net.neoforged.neoforge.event.tick.LevelTickEvent;'),
    @('import\s+net\.neoforged\.neoforge\.event\.TickEvent\b[^;]*;',             '// TODO_MIG[TICK_EVENT]: TickEvent removed; use *TickEvent.Pre / *TickEvent.Post'),

    # TickEvent.Phase -> implicit (Pre/Post is now class-level)
    @('TickEvent\.Phase\.START',                     '/* TODO_MIG[TICK_PHASE]: use *TickEvent.Pre */'),
    @('TickEvent\.Phase\.END',                       '/* TODO_MIG[TICK_PHASE]: use *TickEvent.Post */'),

    # === ForgeCapabilities removed ===
    @('import\s+net\.neoforged\.neoforge\.common\.capabilities\.ForgeCapabilities\s*;', '// TODO_MIG[FORGECAP]: ForgeCapabilities removed; use Capabilities.ItemHandler.* etc'),
    @('ForgeCapabilities\.',                         '/* TODO_MIG[FORGECAP] */ Capabilities.'),

    # === MobType removal ===
    # Just comment out @Override getMobType methods (handled below via separate scan)
    @('import\s+net\.minecraft\.world\.entity\.MobType\s*;',  '// TODO_MIG[MOBTYPE]: MobType removed in 1.21; getMobType() is gone — use entity tags'),

    # === ProtectionEnchantment ===
    @('import\s+net\.minecraft\.world\.item\.enchantment\.ProtectionEnchantment\s*;', '// TODO_MIG[PROTECTION_ENCH]: ProtectionEnchantment removed; use EnchantmentHelper.getDamageProtection'),

    # === Minecraft.getPartialTick removed ===
    @('Minecraft\.getInstance\(\)\.getPartialTick\(\)',  'Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false)'),
    @('mc\.getPartialTick\(\)',                       'mc.getDeltaTracker().getGameTimeDeltaPartialTick(false)'),

    # === ChunkStatus moved (still in net.minecraft.world.level.chunk but as ChunkStatus class — usage same; the issue is that 1.21.1 changed it to be a Holder. For now leave it but flag) ===
    # Not auto-fixable safely; leave as-is and note in audit.

    # === IronGolem.Crackiness import (the inner enum stayed but the import path changed) ===
    @('import\s+net\.minecraft\.world\.entity\.animal\.IronGolem\.Crackiness\s*;', "import net.minecraft.world.entity.animal.IronGolem;`r`nimport net.minecraft.world.entity.animal.IronGolem.Crackiness;"),

    # === BuiltInRegistries import ensure ===
    # Will be added to top of file when BuiltInRegistries appears but isn't imported (best-effort below)
    @('import\s+net\.neoforged\.neoforge\.registries\.NeoForgeRegistries\s*;', 'import net.minecraft.core.registries.BuiltInRegistries;')
)

$ErrorActionPreference = 'Stop'
$stats = @{ Files = 0; Modified = 0; TotalReplaces = 0 }
$javaFiles = Get-ChildItem -Recurse -Filter *.java $TargetDir

Write-Host "Found $($javaFiles.Count) .java files in $TargetDir" -ForegroundColor Cyan
if ($DryRun) { Write-Host "DRY RUN MODE - no files written" -ForegroundColor Yellow }

$utf8NoBom = New-Object System.Text.UTF8Encoding($false)

foreach ($file in $javaFiles) {
    $stats.Files++
    $content = [System.IO.File]::ReadAllText($file.FullName, $utf8NoBom)
    $orig = $content
    $localReplaces = 0

    foreach ($rule in $Rules) {
        $pattern = $rule[0]
        $replacement = $rule[1]
        if ($content -match $pattern) {
            $count = ([regex]::Matches($content, $pattern)).Count
            $content = [regex]::Replace($content, $pattern, $replacement)
            $localReplaces += $count
        }
    }

    # Auto-add `import net.minecraft.core.registries.BuiltInRegistries;` if BuiltInRegistries is used but not imported
    if ($content -match '\bBuiltInRegistries\.' -and $content -notmatch 'import\s+net\.minecraft\.core\.registries\.BuiltInRegistries\s*;') {
        $content = $content -replace '(package\s+[\w\.]+\s*;\s*\r?\n)', "`$1`r`nimport net.minecraft.core.registries.BuiltInRegistries;`r`n"
        $localReplaces++
    }

    if ($content -ne $orig) {
        $stats.Modified++
        $stats.TotalReplaces += $localReplaces
        if (-not $DryRun) {
            [System.IO.File]::WriteAllText($file.FullName, $content, $utf8NoBom)
        }
    }
}

Write-Host "`n=== STATS ===" -ForegroundColor Cyan
Write-Host ("Files scanned:    {0}" -f $stats.Files)
Write-Host ("Files modified:   {0}" -f $stats.Modified)
Write-Host ("Total replaces:   {0}" -f $stats.TotalReplaces)
