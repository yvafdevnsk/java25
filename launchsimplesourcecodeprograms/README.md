# Java25(LTS): シンプルなソースコードプログラムの起動

## 環境
- Windows 11 Pro, 25H2, 26200.7840
- Windows Subsystem for Linux (WSL2), Ubuntu 24.04.4 (LTS), 6.6.87.2-microsoft-standard-WSL2

## 全体図
```
/
    +-home
    |     +-mizuki
    |           +-download
    |           |     |-OpenJDK25U-jdk_x64_linux_hotspot_25.0.1_8.tar.gz
    |           |
    |           +-workspace
    |                 +-java25
    |                       +-launchsimplesourcecodeprograms
    |                             |-HelloWorld.java
    |                             |-HelloJava.java
    |                             |-MultipleClassesInSameFile.java
    |                             |-MultiFileSourceCodePrograms.java
    |                             |-ScannerExample.java
    |                             |-ReferenceNonJDKClass.java
    |                             |-ShebangHelloJava
    |                             |
    |                             +-lib
    |                             |     |-commons-lang3-3.20.0-bin.tar.gz
    |                             |     +-commons-lang3-3.20.0
    |                             |                 |-commons-lang3-3.20.0.jar
    |                             |
    |                             +-model
    |                             |     |-Person.java
    |                             |
    |                             +-service
    |                                   |-PersonService.java
    |
    +-opt
    |     +-java
    |           +-jdk-25.0.1+8
    |                 +-bin
    |                       |-java
    |
    +-usr
          +-local
                +-bin
                      |-java25 -> /opt/java/jdk-25.0.1+8/bin/java
```

## 1. WSLの環境を最新に更新する

WSLバージョンを最新バージョンに更新する。PowerShell上で以下のコマンドを実行する。UACのダイアログが出る。実行中のUbuntuのタブは先に閉じておく。
```
wsl --update
```
実行結果。
```
更新プログラムを確認しています。
Linux 用 Windows サブシステムをバージョンに更新しています: 2.6.3。
```

WSLとそのコンポーネントに関するバージョン情報を確認する。PowerShell上で以下のコマンドを実行する。
```
wsl --version
```
実行結果。
```
WSL バージョン: 2.6.3.0
カーネル バージョン: 6.6.87.2-1
WSLg バージョン: 1.0.71
MSRDC バージョン: 1.2.6353
Direct3D バージョン: 1.611.1-81528511
DXCore バージョン: 10.0.26100.1-240331-1435.ge-release
Windows バージョン: 10.0.26200.7623
```

ディストリビューションのパッケージの更新とアップグレードを実施する。Ubuntu上で以下のコマンドを実行する。
```
sudo apt update && sudo apt upgrade
```

現在の環境を確認する。
```
lsb_release -a
```
実行結果。
```
No LSB modules are available.
Distributor ID: Ubuntu
Description:    Ubuntu 24.04.4 LTS
Release:        24.04
Codename:       noble
```

現在の環境を確認する。
```
cat /etc/os-release
```
実行結果。
```
PRETTY_NAME="Ubuntu 24.04.4 LTS"
NAME="Ubuntu"
VERSION_ID="24.04"
VERSION="24.04.4 LTS (Noble Numbat)"
VERSION_CODENAME=noble
ID=ubuntu
ID_LIKE=debian
HOME_URL="https://www.ubuntu.com/"
SUPPORT_URL="https://help.ubuntu.com/"
BUG_REPORT_URL="https://bugs.launchpad.net/ubuntu/"
PRIVACY_POLICY_URL="https://www.ubuntu.com/legal/terms-and-policies/privacy-policy"
UBUNTU_CODENAME=noble
LOGO=ubuntu-logo
```

現在の環境を確認する。
```
uname -a
```
実行結果。
```
Linux SilentMasterPro 6.6.87.2-microsoft-standard-WSL2 #1 SMP PREEMPT_DYNAMIC Thu Jun  5 18:30:46 UTC 2025 x86_64 x86_64 x86_64 GNU/Linux
```

現在の環境を確認する。
```
java25 --version
```
実行結果。
```
openjdk 25.0.1 2025-10-21 LTS
OpenJDK Runtime Environment Temurin-25.0.1+8 (build 25.0.1+8-LTS)
OpenJDK 64-Bit Server VM Temurin-25.0.1+8 (build 25.0.1+8-LTS, mixed mode, sharing)
```

参考情報。
- [Update WSL | Basic commands for WSL | Microsoft Learn](https://learn.microsoft.com/en-us/windows/wsl/basic-commands#update-wsl)
- [Check WSL version | Basic commands for WSL | Microsoft Learn](https://learn.microsoft.com/en-us/windows/wsl/basic-commands#check-wsl-version)
- [Update and upgrade packages | Set up a WSL development environment | Microsoft Learn](https://learn.microsoft.com/en-us/windows/wsl/setup/environment#update-and-upgrade-packages)
