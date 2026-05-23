<#
.SYNOPSIS
    Phase-3 mechanical fix for VertexConsumer chain API in 1.21.

.DESCRIPTION
    1.21 renamed the entire VertexConsumer fluent chain:
        .vertex(Matrix4f, x, y, z)  -> .addVertex(Matrix4f, x, y, z)
        .color(...)                 -> .setColor(...)
        .uv(u, v)                   -> .setUv(u, v)
        .overlayCoords(int)         -> .setOverlay(int)
        .uv2(int)                   -> .setLight(int)
        .normal(Matrix3f, x, y, z)  -> .setNormal(x, y, z)   (drops Matrix3f - mild correctness loss)
        .normal(x, y, z)            -> .setNormal(x, y, z)
        .endVertex()                -> (removed entirely)

    Scope: only files that contain ".vertex(" so we don't accidentally rewrite
    unrelated .color/.uv calls elsewhere.

.PARAMETER TargetDir
.PARAMETER DryRun
#>

param(
    [string]$TargetDir = "c:\Users\ROG\Desktop\Projects\crackers-wither-storm-modupdate\mdk\src\main\java\nonamecrackers2",
    [switch]$DryRun
)

# Order matters: process specific patterns before generic ones
$Rules = @(
    # 1. Rename core method
    @('\.vertex\(',                                  '.addVertex('),

    # 2. Drop endVertex() call (and the leading dot)
    @('\.endVertex\(\)',                             ''),

    # 3. Normal with Matrix3f variant (matrix3f / normal / matrix3F variable names — drop the matrix arg)
    @('\.normal\(\s*matrix3f\s*,\s*',                '.setNormal('),
    @('\.normal\(\s*matrix3F\s*,\s*',                '.setNormal('),
    @('\.normal\(\s*normal3f\s*,\s*',                '.setNormal('),
    @('\.normal\(\s*normalMatrix\s*,\s*',            '.setNormal('),
    # Generic identifier-followed-by-comma (catches other Matrix3f variable names, but only if first arg is a single identifier)
    @('\.normal\(\s*([A-Za-z_]\w*)\s*,\s*([\-\d])',  '.setNormal($2'),

    # 4. Normal without matrix (catch-all)
    @('\.normal\(',                                  '.setNormal('),

    # 5. Other chain methods (only safe in scope of files containing .vertex chains)
    @('\.uv2\(',                                     '.setLight('),
    @('\.overlayCoords\(',                           '.setOverlay('),
    @('\.uv\(',                                      '.setUv('),
    @('\.color\(',                                   '.setColor(')
)

$ErrorActionPreference = 'Stop'
$utf8NoBom = New-Object System.Text.UTF8Encoding($false)
$stats = @{ Files = 0; Scoped = 0; Modified = 0; Replaces = 0 }

$javaFiles = Get-ChildItem -Recurse -Filter *.java $TargetDir
Write-Host "Scanning $($javaFiles.Count) .java files..." -ForegroundColor Cyan
if ($DryRun) { Write-Host "DRY RUN MODE" -ForegroundColor Yellow }

foreach ($file in $javaFiles) {
    $stats.Files++
    $content = [System.IO.File]::ReadAllText($file.FullName)

    # Scope: only files containing .vertex( OR .endVertex() OR .overlayCoords(
    if ($content -notmatch '\.vertex\(' -and $content -notmatch '\.endVertex\(\)' -and $content -notmatch '\.overlayCoords\(') {
        continue
    }
    $stats.Scoped++

    $orig = $content
    $local = 0
    foreach ($rule in $Rules) {
        $pattern = $rule[0]; $replacement = $rule[1]
        if ($content -match $pattern) {
            $count = ([regex]::Matches($content, $pattern)).Count
            $content = [regex]::Replace($content, $pattern, $replacement)
            $local += $count
        }
    }

    if ($content -ne $orig) {
        $stats.Modified++
        $stats.Replaces += $local
        if (-not $DryRun) {
            [System.IO.File]::WriteAllText($file.FullName, $content, $utf8NoBom)
        }
    }
}

Write-Host "`n=== STATS ===" -ForegroundColor Cyan
Write-Host ("Files scanned:   {0}" -f $stats.Files)
Write-Host ("Files in scope:  {0}" -f $stats.Scoped)
Write-Host ("Files modified:  {0}" -f $stats.Modified)
Write-Host ("Total replaces:  {0}" -f $stats.Replaces)
