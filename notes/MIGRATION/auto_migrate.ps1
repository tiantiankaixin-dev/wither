<#
.SYNOPSIS
    Wither Storm Mod 1.20.1 Forge -> 1.21.1 NeoForge Auto-Migration Script (Phase 1: imports + simple renames)

.DESCRIPTION
    Mechanical replacements (~70% of import work). For each .java file:
    1. Apply Forge -> NeoForge package mapping
    2. Replace known class renames (MinecraftForge -> NeoForge etc.)
    3. Mark spots needing human review (insert TODO comments)

.PARAMETER SourceDir
.PARAMETER TargetDir
.PARAMETER DryRun

.EXAMPLE
    .\auto_migrate.ps1 -DryRun
    .\auto_migrate.ps1
#>

param(
    [string]$SourceDir = "c:\Users\ROG\Desktop\Projects\crackers-wither-storm-modupdate\wither-github-latest\src\main\java\nonamecrackers2",
    [string]$TargetDir = "c:\Users\ROG\Desktop\Projects\crackers-wither-storm-modupdate\mdk\src\main\java\nonamecrackers2",
    [switch]$DryRun
)

# Each rule = [pattern, replacement]; processed in order
$Rules = @(
    # === Removed APIs (insert TODO markers) ===
    @('net\.minecraftforge\.fml\.DistExecutor',                       '// TODO_MIG: DistExecutor removed, use FMLEnvironment.dist == Dist.CLIENT'),
    @('net\.minecraftforge\.event\.AttachCapabilitiesEvent',          '// TODO_MIG: AttachCapabilitiesEvent removed, use RegisterCapabilitiesEvent (see CAPABILITY_AUDIT.md)'),
    @('net\.minecraftforge\.event\.ItemAttributeModifierEvent',       '// TODO_MIG: ItemAttributeModifierEvent removed, use DataComponents.ATTRIBUTE_MODIFIERS'),
    @('net\.minecraftforge\.event\.TickEvent',                        '// TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent'),
    @('net\.minecraftforge\.network\.NetworkRegistry',                '// TODO_MIG: NetworkRegistry removed, use IPayloadRegistrar (see PACKET_AUDIT.md)'),
    @('net\.minecraftforge\.network\.NetworkHooks',                   '// TODO_MIG: NetworkHooks removed, use PacketDistributor'),
    @('net\.minecraftforge\.network\.NetworkEvent',                   '// TODO_MIG: NetworkEvent removed, use IPayloadContext'),
    @('net\.minecraftforge\.network\.simple\.SimpleChannel',          '// TODO_MIG: SimpleChannel removed, use IPayloadRegistrar'),
    @('net\.minecraftforge\.network\.PlayMessages',                   '// TODO_MIG: PlayMessages removed'),
    @('net\.minecraftforge\.server\.timings',                         '// TODO_MIG: server.timings removed'),
    @('net\.minecraftforge\.common\.util\.LazyOptional',              '// TODO_MIG: LazyOptional removed, new Capability API returns T or null'),

    # === Class renames ===
    @('net\.minecraftforge\.common\.MinecraftForge',                  'net.neoforged.neoforge.common.NeoForge'),
    @('net\.minecraftforge\.common\.ForgeMod',                        'net.neoforged.neoforge.common.NeoForgeMod'),
    @('net\.minecraftforge\.common\.ForgeSpawnEggItem',               'net.neoforged.neoforge.common.DeferredSpawnEggItem'),
    @('net\.minecraftforge\.common\.IForgeShearable',                 'net.neoforged.neoforge.common.IShearable'),
    @('net\.minecraftforge\.common\.ForgeConfigSpec',                 'net.neoforged.neoforge.common.ModConfigSpec'),
    @('net\.minecraftforge\.entity\.IEntityAdditionalSpawnData',      'net.neoforged.neoforge.entity.IEntityWithComplexSpawn'),
    @('net\.minecraftforge\.event\.ForgeEventFactory',                'net.neoforged.neoforge.event.EventHooks'),
    @('net\.minecraftforge\.registries\.ForgeRegistries',             'net.neoforged.neoforge.registries.NeoForgeRegistries'),
    @('net\.minecraftforge\.registries\.RegistryObject',              'net.neoforged.neoforge.registries.DeferredHolder'),
    @('net\.minecraftforge\.common\.ToolActions',                     'net.neoforged.neoforge.common.ItemAbilities'),
    @('net\.minecraftforge\.common\.ToolAction',                      'net.neoforged.neoforge.common.ItemAbility'),

    # === Package prefix replacements (order matters: more specific first) ===
    @('net\.minecraftforge\.fml\.',                                   'net.neoforged.fml.'),
    @('net\.minecraftforge\.eventbus\.',                              'net.neoforged.bus.'),
    @('net\.minecraftforge\.api\.distmarker\.',                       'net.neoforged.api.distmarker.'),
    @('net\.minecraftforge\.forgespi\.',                              'net.neoforged.neoforgespi.'),
    @('net\.minecraftforge\.common\.command\.',                       'net.neoforged.neoforge.common.commands.'),
    @('net\.minecraftforge\.',                                        'net.neoforged.neoforge.'),

    # === In-file references (post-import) ===
    @('MinecraftForge\.EVENT_BUS',                                    'NeoForge.EVENT_BUS')
)

# Call-site rules (only when import was rewritten)
$CallSiteRules = @(
    @('LazyOptional\.empty\(\)',     'null /* TODO_MIG: LazyOptional.empty() -> null */'),
    @('ForgeRegistries\.',           'NeoForgeRegistries.'),
    @('RegistryObject<',             'DeferredHolder<')
)

$ErrorActionPreference = 'Stop'

if (-not (Test-Path $SourceDir)) {
    Write-Error "Source not found: $SourceDir"
    exit 1
}

# Step 1: Copy entire source tree into target using robocopy (skip if dry-run)
if (-not $DryRun) {
    Write-Host "Copying source to target via robocopy..." -ForegroundColor Cyan
    if (Test-Path $TargetDir) { Remove-Item -Recurse -Force $TargetDir }
    New-Item -ItemType Directory -Force -Path $TargetDir | Out-Null
    & robocopy $SourceDir $TargetDir /E /NFL /NDL /NJH /NJS /NC /NS /NP | Out-Null
    if ($LASTEXITCODE -ge 8) { Write-Error "robocopy failed with code $LASTEXITCODE"; exit 1 }
    Write-Host "Source copied. Now applying in-place transformations..." -ForegroundColor Cyan
}

$stats = @{ Files = 0; Modified = 0; ImportChanges = 0; CallSiteChanges = 0; TodoMarkers = 0 }

# Step 2: Walk the (now-copied) target tree and modify in place
$walkRoot = if ($DryRun) { $SourceDir } else { $TargetDir }
$javaFiles = Get-ChildItem -Recurse -Filter *.java $walkRoot

Write-Host "Found $($javaFiles.Count) .java files in $walkRoot" -ForegroundColor Cyan
if ($DryRun) { Write-Host "DRY RUN MODE - no files written" -ForegroundColor Yellow }

foreach ($file in $javaFiles) {
    $stats.Files++
    $content = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)
    $orig = $content
    $localImportChanges = 0
    $localCallChanges = 0
    $localTodos = 0

    foreach ($rule in $Rules) {
        $pattern = $rule[0]
        $replacement = $rule[1]
        if ($content -match $pattern) {
            $count = ([regex]::Matches($content, $pattern)).Count
            $content = [regex]::Replace($content, $pattern, $replacement)
            $localImportChanges += $count
            if ($replacement.StartsWith('// TODO_MIG')) {
                $localTodos += $count
            }
        }
    }

    if ($content -ne $orig) {
        foreach ($rule in $CallSiteRules) {
            $pattern = $rule[0]
            $replacement = $rule[1]
            if ($content -match $pattern) {
                $count = ([regex]::Matches($content, $pattern)).Count
                $content = [regex]::Replace($content, $pattern, $replacement)
                $localCallChanges += $count
                if ($replacement.Contains('TODO_MIG')) { $localTodos += $count }
            }
        }
    }

    if ($content -ne $orig) {
        $stats.Modified++
        $stats.ImportChanges += $localImportChanges
        $stats.CallSiteChanges += $localCallChanges
        $stats.TodoMarkers += $localTodos

        if (-not $DryRun) {
            # IMPORTANT: use UTF8 WITHOUT BOM (javac rejects BOM)
            $utf8NoBom = New-Object System.Text.UTF8Encoding($false)
            [System.IO.File]::WriteAllText($file.FullName, $content, $utf8NoBom)
        }
    }
}

Write-Host "`n=== STATS ===" -ForegroundColor Cyan
Write-Host ("Files scanned:       {0}" -f $stats.Files)
Write-Host ("Files modified:      {0}" -f $stats.Modified)
Write-Host ("Import changes:      {0}" -f $stats.ImportChanges)
Write-Host ("Call-site changes:   {0}" -f $stats.CallSiteChanges)
Write-Host ("TODO markers added:  {0}" -f $stats.TodoMarkers)
Write-Host "`nNext steps:" -ForegroundColor Yellow
Write-Host "  1. grep 'TODO_MIG' in target dir to find all spots needing human review"
Write-Host "  2. Run mdk/gradlew compileJava to see real compile errors"
Write-Host "  3. Fix errors file-by-file (priority: crackerslib -> util -> init -> entity -> mixin)"
