[CmdletBinding()]
param(
    [Parameter(Position=0, Mandatory=$true)]
    [ValidateNotNull()]
    [ValidateLength(1,[int]::MaxValue)]
    [string]
    $DownloadUrl,

    [Parameter(Position=1, Mandatory=$true)]
    [string]
    $TargetFile
)

$ErrorActionPreference = "Stop"

function Invoke-MavenWrapperDownload {
    [CmdletBinding()]
    param(
        [Parameter(Position=0, Mandatory=$true)]
        [string]
        $DownloadUrl,

        [Parameter(Position=1, Mandatory=$true)]
        [string]
        $TargetFile
    )

    if (Test-Path "$TargetFile") {
        Write-Verbose "File $TargetFile already exists; skipping download."
        return
    }

    Write-Verbose "Retrieving $DownloadUrl"
    $targetDir = Split-Path $TargetFile
    New-Item -ItemType Directory -Force -ErrorAction SilentlyContinue | Out-Null
    $request = [System.Net.HttpWebRequest]::Create($DownloadUrl)
    $response = $request.GetResponse()
    $responseStream = $response.GetResponseStream()
    $targetStream = New-Object -TypeName System.IO.FileStream -ArgumentList $TargetFile, Create, Write
    $buffer = New-Object byte[] 10240
    while (($bytesRead = $responseStream.Read($buffer, 0, $buffer.Length)) -ne 0) {
        $targetStream.Write($buffer, 0, $bytesRead)
    }
    $targetStream.Dispose()
    $responseStream.Dispose()
}

Invoke-MavenWrapperDownload $DownloadUrl $TargetFile
