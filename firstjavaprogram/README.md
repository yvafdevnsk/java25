# Java25(LTS): 最初のJavaプログラムの作成

## 環境
- Windows 11 Pro, 25H2, 26200.7623
- Windows Subsystem for Linux (WSL2), Ubuntu 24.04.3 (LTS), 6.6.87.2-microsoft-standard-WSL2

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
    |                       +-firstjavaprogram
    |                             |-MyFirstJavaApp.java
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
Description:    Ubuntu 24.04.3 LTS
Release:        24.04
Codename:       noble
```

現在の環境を確認する。

```
cat /etc/os-release
```

実行結果。

```
PRETTY_NAME="Ubuntu 24.04.3 LTS"
NAME="Ubuntu"
VERSION_ID="24.04"
VERSION="24.04.3 LTS (Noble Numbat)"
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

## 2. 最初のJavaプログラムの作成

空のテキストファイルを作成する。

```
mkdir -p /home/mizuki/workspace/java25/firstjavaprogram
cd /home/mizuki/workspace/java25/firstjavaprogram
touch MyFirstJavaApp.java
```

ubuntu上でvscodeを起動する。

```
code .
```

MyFirstJavaApp.javaファイルに以下のコードを記述する。

```
void main() {
    IO.println("Hello, World!");
}
```

Javaプログラムを実行する。

```
java25 MyFirstJavaApp.java
```

実行結果。

```
Hello, World!
```

Javaプログラムをインタラクティブにする。

```
void main() {
    IO.println("Hello world!");
    var name = IO.readln("What is your name? ");
    IO.println("Hello " + name);
}
```

Javaプログラムを実行する。

```
java25 MyFirstJavaApp.java
```

実行結果。

```
Hello, World!
What is your name? Mizuki
Hello Mizuki
```

参考情報。
- [Creating a First Java Program | Getting Started with Java | Dev.java](https://dev.java/learn/getting-started/)
- [IO (JavaSE 25 & JDK 25)](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/IO.html)
- [Local-Variable Type Inference | Java Language Changes for Java SE 10 | Language Updates | Language and Libraries | JDK 10 Documentation](https://docs.oracle.com/javase/10/language/toc.htm)
