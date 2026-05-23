<#
.SYNOPSIS
    Combined phase-1 + phase-2 auto-migration that runs IN PLACE on an arbitrary target directory.
    Use this for crackerslib (already copied into mdk source tree) or any subset of files.

.PARAMETER TargetDir
    The directory to scan (recursive). Defaults to the crackerslib subtree.

.PARAMETER DryRun
    Preview changes without writing.

.EXAMPLE
    .\auto_migrate_inplace.ps1
    .\auto_migrate_inplace.ps1 -TargetDir "...\mdk\src\main\java\nonamecrackers2\crackerslib" -DryRun
#>

param(
    [string]$TargetDir = "c:\Users\ROG\Desktop\Projects\crackers-wither-storm-modupdate\mdk\src\main\java\nonamecrackers2\crackerslib",
    [switch]$DryRun
)

# === Phase-1 rules: Forge -> NeoForge import / class renames ===
$Phase1Rules = @(
    # Removed APIs (insert TODO markers)
    @('net\.minecraftforge\.fml\.DistExecutor',                  '// TODO_MIG: DistExecutor removed, use FMLEnvironment.dist == Dist.CLIENT'),
    @('net\.minecraftforge\.event\.AttachCapabilitiesEvent',     '// TODO_MIG: AttachCapabilitiesEvent removed, use RegisterCapabilitiesEvent'),
    @('net\.minecraftforge\.event\.ItemAttributeModifierEvent',  '// TODO_MIG: ItemAttributeModifierEvent removed, use DataComponents.ATTRIBUTE_MODIFIERS'),
    @('net\.minecraftforge\.event\.TickEvent',                   '// TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent'),
    @('net\.minecraftforge\.network\.NetworkRegistry',           '// TODO_MIG: NetworkRegistry removed, use IPayloadRegistrar'),
    @('net\.minecraftforge\.network\.NetworkHooks',              '// TODO_MIG: NetworkHooks removed, use PacketDistributor'),
    @('net\.minecraftforge\.network\.NetworkEvent',              '// TODO_MIG: NetworkEvent removed, use IPayloadContext'),
    @('net\.minecraftforge\.network\.simple\.SimpleChannel',     '// TODO_MIG: SimpleChannel removed, use IPayloadRegistrar'),
    @('net\.minecraftforge\.network\.PlayMessages',              '// TODO_MIG: PlayMessages removed'),
    @('net\.minecraftforge\.server\.timings',                    '// TODO_MIG: server.timings removed'),
    @('net\.minecraftforge\.common\.util\.LazyOptional',         '// TODO_MIG: LazyOptional removed, new Capability API returns T or null'),

    # Class renames
    @('net\.minecraftforge\.common\.MinecraftForge',             'net.neoforged.neoforge.common.NeoForge'),
    @('net\.minecraftforge\.common\.ForgeMod',                   'net.neoforged.neoforge.common.NeoForgeMod'),
    @('net\.minecraftforge\.common\.ForgeSpawnEggItem',          'net.neoforged.neoforge.common.DeferredSpawnEggItem'),
    @('net\.minecraftforge\.common\.IForgeShearable',            'net.neoforged.neoforge.common.IShearable'),
    @('net\.minecraftforge\.common\.ForgeConfigSpec',            'net.neoforged.neoforge.common.ModConfigSpec'),
    @('net\.minecraftforge\.entity\.IEntityAdditionalSpawnData', 'net.neoforged.neoforge.entity.IEntityWithComplexSpawn'),
    @('net\.minecraftforge\.event\.ForgeEventFactory',           'net.neoforged.neoforge.event.EventHooks'),
    @('net\.minecraftforge\.registries\.ForgeRegistries',        'net.neoforged.neoforge.registries.NeoForgeRegistries'),
    @('net\.minecraftforge\.registries\.RegistryObject',         'net.neoforged.neoforge.registries.DeferredHolder'),
    @('net\.minecraftforge\.common\.ToolActions',                'net.neoforged.neoforge.common.ItemAbilities'),
    @('net\.minecraftforge\.common\.ToolAction',                 'net.neoforged.neoforge.common.ItemAbility'),

    # Package prefix replacements (most specific first)
    @('net\.minecraftforge\.fml\.',                              'net.neoforged.fml.'),
    @('net\.minecraftforge\.eventbus\.',                         'net.neoforged.bus.'),
    @('net\.minecraftforge\.api\.distmarker\.',                  'net.neoforged.api.distmarker.'),
    @('net\.minecraftforge\.forgespi\.',                         'net.neoforged.neoforgespi.'),
    @('net\.minecraftforge\.common\.command\.',                  'net.neoforged.neoforge.common.commands.'),
    @('net\.minecraftforge\.',                                   'net.neoforged.neoforge.'),

    # In-file refs
    @('MinecraftForge\.EVENT_BUS',                               'NeoForge.EVENT_BUS')
)

$Phase1CallSiteRules = @(
    @('LazyOptional\.empty\(\)',     'null /* MIG_LAZYOPT */'),
    @('ForgeRegistries\.',           'NeoForgeRegistries.'),
    @('RegistryObject<',             'DeferredHolder<'),
    # Class-name renames (in-file references, not just imports)
    @('\bForgeConfigSpec\b',         'ModConfigSpec'),
    @('\bForgeMod\b',                'NeoForgeMod'),
    @('\bMinecraftForge\b',          'NeoForge'),
    @('\bForgeEventFactory\b',       'EventHooks'),
    @('\bForgeSpawnEggItem\b',       'DeferredSpawnEggItem'),
    @('\bIForgeShearable\b',         'IShearable'),
    @('\bIEntityAdditionalSpawnData\b','IEntityWithComplexSpawn'),
    @('\bToolActions\b',             'ItemAbilities'),
    @('\bToolAction\b',              'ItemAbility')
)

# === Phase-2 rules: vanilla API changes ===
$Phase2Rules = @(
    # ResourceLocation private ctor
    @('new\s+ResourceLocation\s*\(\s*("(?:[^"\\]|\\.)*"|[\w\.]+)\s*,\s*("(?:[^"\\]|\\.)*"|[\w\.]+)\s*\)', 'ResourceLocation.fromNamespaceAndPath($1, $2)'),
    @('new\s+ResourceLocation\s*\(\s*("(?:[^"\\]|\\.)*")\s*\)', 'ResourceLocation.parse($1)'),

    # ForgeRegistries.X -> BuiltInRegistries.Y
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
    @('\bIForgeRegistry\b',                          'Registry'),

    # SpawnPlacements.Type
    @('SpawnPlacements\.Type\b',                     'SpawnPlacementTypes'),

    # Cancelable
    @('@Cancelable\s*\r?\n',                         "// TODO_MIG[CANCELABLE]: implement ICancellableEvent on this class instead`r`n"),
    @('import\s+net\.neoforged\.bus\.api\.Cancelable\s*;',  '// TODO_MIG[CANCELABLE]: removed; class must implement ICancellableEvent'),

    # TickEvent imports
    @('import\s+net\.neoforged\.neoforge\.event\.TickEvent\.LevelTickEvent\s*;', 'import net.neoforged.neoforge.event.tick.LevelTickEvent;'),
    @('import\s+net\.neoforged\.neoforge\.event\.TickEvent\.ClientTickEvent\s*;', 'import net.neoforged.neoforge.client.event.ClientTickEvent;'),
    @('import\s+net\.neoforged\.neoforge\.event\.TickEvent\.PlayerTickEvent\s*;', 'import net.neoforged.neoforge.event.tick.PlayerTickEvent;'),
    @('import\s+net\.neoforged\.neoforge\.event\.TickEvent\.ServerTickEvent\s*;', 'import net.neoforged.neoforge.event.tick.ServerTickEvent;'),
    @('import\s+net\.neoforged\.neoforge\.event\.TickEvent\.WorldTickEvent\s*;', 'import net.neoforged.neoforge.event.tick.LevelTickEvent;'),
    @('import\s+net\.neoforged\.neoforge\.event\.TickEvent\b[^;]*;',             '// TODO_MIG[TICK_EVENT]: TickEvent removed; use *TickEvent.Pre / *TickEvent.Post'),
    @('TickEvent\.Phase\.START',                     '/* TODO_MIG[TICK_PHASE]: use *TickEvent.Pre */'),
    @('TickEvent\.Phase\.END',                       '/* TODO_MIG[TICK_PHASE]: use *TickEvent.Post */'),

    # ForgeCapabilities
    @('import\s+net\.neoforged\.neoforge\.common\.capabilities\.ForgeCapabilities\s*;', '// TODO_MIG[FORGECAP]: ForgeCapabilities removed; use Capabilities.ItemHandler.* etc'),
    @('ForgeCapabilities\.',                         '/* TODO_MIG[FORGECAP] */ Capabilities.'),

    # MobType
    @('import\s+net\.minecraft\.world\.entity\.MobType\s*;',  '// TODO_MIG[MOBTYPE]: MobType removed in 1.21; getMobType() is gone - use entity tags'),

    # ProtectionEnchantment
    @('import\s+net\.minecraft\.world\.item\.enchantment\.ProtectionEnchantment\s*;', '// TODO_MIG[PROTECTION_ENCH]: ProtectionEnchantment removed; use EnchantmentHelper.getDamageProtection'),

    # getPartialTick
    @('Minecraft\.getInstance\(\)\.getPartialTick\(\)',  'Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false)'),
    @('mc\.getPartialTick\(\)',                       'mc.getDeltaTracker().getGameTimeDeltaPartialTick(false)'),

    # IronGolem.Crackiness inner enum (use real newline via backtick-n)
    @('import\s+net\.minecraft\.world\.entity\.animal\.IronGolem\.Crackiness\s*;', "import net.minecraft.world.entity.animal.IronGolem;`r`nimport net.minecraft.world.entity.animal.IronGolem.Crackiness;"),

    # BuiltInRegistries import normalization
    @('import\s+net\.neoforged\.neoforge\.registries\.NeoForgeRegistries\s*;', 'import net.minecraft.core.registries.BuiltInRegistries;')
)

$ErrorActionPreference = 'Stop'
$utf8NoBom = New-Object System.Text.UTF8Encoding($false)
$stats = @{ Files = 0; Modified = 0; Phase1Replaces = 0; Phase2Replaces = 0; CallSiteReplaces = 0 }

if (-not (Test-Path $TargetDir)) {
    Write-Error "TargetDir not found: $TargetDir"
    exit 1
}

$javaFiles = Get-ChildItem -Recurse -Filter *.java $TargetDir
Write-Host "Found $($javaFiles.Count) .java files in $TargetDir" -ForegroundColor Cyan
if ($DryRun) { Write-Host "DRY RUN MODE - no files written" -ForegroundColor Yellow }

foreach ($file in $javaFiles) {
    $stats.Files++
    # Read tolerantly (BOM-safe via UTF8 default); will re-write without BOM
    $content = [System.IO.File]::ReadAllText($file.FullName)
    $orig = $content
    $p1 = 0; $p2 = 0; $cs = 0

    foreach ($rule in $Phase1Rules) {
        $pattern = $rule[0]; $replacement = $rule[1]
        if ($content -match $pattern) {
            $p1 += ([regex]::Matches($content, $pattern)).Count
            $content = [regex]::Replace($content, $pattern, $replacement)
        }
    }
    foreach ($rule in $Phase1CallSiteRules) {
        $pattern = $rule[0]; $replacement = $rule[1]
        if ($content -match $pattern) {
            $cs += ([regex]::Matches($content, $pattern)).Count
            $content = [regex]::Replace($content, $pattern, $replacement)
        }
    }
    foreach ($rule in $Phase2Rules) {
        $pattern = $rule[0]; $replacement = $rule[1]
        if ($content -match $pattern) {
            $p2 += ([regex]::Matches($content, $pattern)).Count
            $content = [regex]::Replace($content, $pattern, $replacement)
        }
    }

    # Auto-add BuiltInRegistries import if used but not present
    if ($content -match '\bBuiltInRegistries\.' -and $content -notmatch 'import\s+net\.minecraft\.core\.registries\.BuiltInRegistries\s*;') {
        $content = $content -replace '(package\s+[\w\.]+\s*;\s*\r?\n)', "`$1`r`nimport net.minecraft.core.registries.BuiltInRegistries;`r`n"
        $p2++
    }

    # Comment out broken `import // TODO_MIG: ...;` lines (turn into harmless comments)
    if ($content -match 'import\s+//\s*TODO_MIG[^\r\n]*;') {
        $content = [regex]::Replace($content, '^\s*import\s+(//\s*TODO_MIG[^\r\n]*;.*)$', '// TODO_MIG[REMOVED_IMPORT]: $1', 'Multiline')
    }

    if ($content -ne $orig) {
        $stats.Modified++
        $stats.Phase1Replaces += $p1
        $stats.Phase2Replaces += $p2
        $stats.CallSiteReplaces += $cs
        if (-not $DryRun) {
            [System.IO.File]::WriteAllText($file.FullName, $content, $utf8NoBom)
        }
    } else {
        # Even unchanged files: strip BOM if present
        if (-not $DryRun -and $content.Length -gt 0 -and $content[0] -eq [char]0xFEFF) {
            [System.IO.File]::WriteAllText($file.FullName, $content.Substring(1), $utf8NoBom)
        }
    }
}

Write-Host "`n=== STATS ===" -ForegroundColor Cyan
Write-Host ("Files scanned:       {0}" -f $stats.Files)
Write-Host ("Files modified:      {0}" -f $stats.Modified)
Write-Host ("Phase-1 replaces:    {0}" -f $stats.Phase1Replaces)
Write-Host ("Phase-1 call-sites:  {0}" -f $stats.CallSiteReplaces)
Write-Host ("Phase-2 replaces:    {0}" -f $stats.Phase2Replaces)
