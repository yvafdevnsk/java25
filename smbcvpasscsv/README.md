# Java25(LTS): 三井住友カード会員向けインターネットサービス『Vpass』の利用明細のCSVファイルから「利用店名」ごとの「利用金額」の小計および累計を出力する

## 環境
- Windows 11 Pro, 25H2, 26200.8037
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
    |                       +-smbcvpasscsv
    |                             |-CSV2Subtotal.java
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
Windows バージョン: 10.0.26200.8037
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

## 2. プロジェクトの作成
プロジェクトのディレクトリ構造を作成する。
```
mkdir -p /home/mizuki/workspace/java25/smbcvpasscsv
```

空のテキストファイルを作成する。
```
cd /home/mizuki/workspace/java25/smbcvpasscsv
touch CSV2Subtotal.java
```

ubuntu上でvscodeを起動する。
```
cd /home/mizuki/workspace/java25/smbcvpasscsv
code .
```

## 3. プログラムの実装
```
//「利用店名」ごとの「利用金額」の小計を計算する。
Map<String, Integer> subtotals = transactions.stream()
        .collect(Collectors.groupingBy(CreditCardTransaction::storeName,
                                       Collectors.summingInt(CreditCardTransaction::amount)));

//「利用金額」の降順、「利用店名」の昇順でソートする。
List<Map.Entry<String, Integer>> sortedSubtotals = subtotals.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                .thenComparing(Map.Entry.comparingByKey()))
        .collect(Collectors.toList());

//各行までの累計金額を追加して出力する。
List<String> outputLines = new ArrayList<>();
int runningTotal = 0;
for (Map.Entry<String, Integer> entry : sortedSubtotals) {
    runningTotal += entry.getValue();
    outputLines.add(String.join(",", csvQuote(entry.getKey()), csvQuote(String.valueOf(entry.getValue())), csvQuote(String.valueOf(runningTotal))));
}
```

参考情報。
- [Common I/O Tasks in Modern Java - Dev.java](https://dev.java/learn/modernio/)
- [Using Records to Model Immutable Data - Dev.java](https://dev.java/learn/records/)
- [Files (Java Platform SE 25 & JDK 25)](https://docs.oracle.com/javase/jp/25/docs/api/java.base/java/nio/file/Files.html)
- [Path (Java Platform SE 25 & JDK 25)](https://docs.oracle.com/javase/jp/25/docs/api/java.base/java/nio/file/Path.html)
- [Record (Java Platform SE 25 & JDK 25)](https://docs.oracle.com/javase/jp/25/docs/api/java.base/java/lang/Record.html)
- [Collectors (Java Platform SE 25 & JDK 25)](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/stream/Collectors.html)

## 4. プログラムの実行
第1引数にCSVファイルのフルパスを指定して実行する。
```
cd /home/mizuki/workspace/java25/smbcvpasscsv
java25 CSV2Subtotal.java /home/mizuki/downloads/202603.csv
```

指定したCSVファイルと同じディレクトリに小計ファイルが作成される。
```
/home/mizuki/downloads/202603_subtotal.csv
```
