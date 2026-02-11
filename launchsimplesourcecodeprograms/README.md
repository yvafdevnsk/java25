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

## 2. 最初の単一ファイルソースコードプログラムの実行

プロジェクトのディレクトリ構造を作成する。
```
mkdir -p /home/mizuki/workspace/java25/launchsimplesourcecodeprograms
mkdir -p /home/mizuki/workspace/java25/launchsimplesourcecodeprograms/model
mkdir -p /home/mizuki/workspace/java25/launchsimplesourcecodeprograms/service
mkdir -p /home/mizuki/workspace/java25/launchsimplesourcecodeprograms/lib
```

空のテキストファイルを作成する。
```
cd /home/mizuki/workspace/java25/launchsimplesourcecodeprograms
touch HelloWorld.java
touch HelloJava.java
touch MultipleClassesInSameFile.java
touch MultiFileSourceCodePrograms.java
touch ScannerExample.java
touch ReferenceNonJDKClass.java
touch ShebangHelloJava
cd /home/mizuki/workspace/java25/launchsimplesourcecodeprograms/model
touch Person.java
cd /home/mizuki/workspace/java25/launchsimplesourcecodeprograms/service
touch PersonService.java
```

ubuntu上でvscodeを起動する。
```
cd /home/mizuki/workspace/java25/launchsimplesourcecodeprograms
code .
```

HelloWorld.javaファイルに以下のコードを記述する。
```
public class HelloWorld {
    public static void main(String[] args) {
        IO.println("Hello World!");
    }
}
```
Javaプログラムを実行する。
```
java25 HelloWorld.java
```
実行結果。
```
Hello World!
```

Javaプログラムに引数を渡す。HelloJava.javaファイルに以下のコードを記述する。
```
public class HelloJava {
    public static void main(String[] args) {
        IO.println("Hello " + args[0]);
    }
}
```
Javaプログラムを実行する。
```
java25 HelloJava.java Mizuki!
```
実行結果。
```
Hello Mizuki!
```

同じファイル内に複数のクラスを定義する。MultipleClassesInSameFile.javaファイルに以下のコードを記述する。
```
public class MultipleClassesInSameFile {
    public static void main(String[] args) {
        IO.println(GenerateMessage.generateMessage());
        IO.println(AnotherMessage.generateAnotherMessage());
    }
}

class GenerateMessage {
    static String generateMessage() {
        return "Here is one message";
    }
}

class AnotherMessage {
    static String generateAnotherMessage() {
        return "Here is another message";
    }
}
```
Javaプログラムを実行する。
```
java25 MultipleClassesInSameFile.java
```
実行結果。
```
Here is one message
Here is another message
```

複数ファイルのソースコードで構成されるプログラムを起動する。ソースコードのパッケージ階層とファイルの場所を一致させる。MultiFileSourceCodePrograms.javaファイルに以下のコードを記述する。
```
import model.Person;
import service.PersonService;

public class MultiFileSourceCodePrograms {
    public static void main(String[] args) {
        PersonService service = new PersonService();
        Person person = service.createNewPerson();
        IO.println(person.printName() + " has been created!");
    }
}
```
model/Person.javaファイルに以下のコードを記述する。
```
package model;

public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String printName() {
        return this.name;
    }
}
```
service/PersonService.javaファイルに以下のコードを記述する。
```
package service;

import model.Person;

public class PersonService {
    public Person createNewPerson() {
        return new Person("Mizuki");
    }
}
```
Javaプログラムを実行する。
```
java25 MultiFileSourceCodePrograms.java
```
実行結果。
```
Mizuki has been created!
```

コアJDKに含まれるクラスは実行するためにクラスパスに追加する必要はない。ScannerExample.javaファイルに以下のコードを記述する。
```
import java.util.Scanner;
import java.util.regex.MatchResult;

public class ScannerExample {
    public static void main(String... args) {
        String wordsAndNumbers = """
            Longing rusted furnace
            daybreak 17 benign
            9 homecoming 1
            freight car
        """;

        try (Scanner scanner = new Scanner(wordsAndNumbers)) {
            scanner.findAll("benign").map(MatchResult::group).forEach(IO::println);
        }
    }
}
```
Javaプログラムを実行する。
```
java25 ScannerExample.java
```
実行結果。
```
benign
```

コアJDKに含まれないクラスは実行するためにクラスパスに追加する必要がある。ReferenceNonJDKClass.javaファイルに以下のコードを記述する。
```
import org.apache.commons.lang3.RandomUtils;

public class ReferenceNonJDKClass {
    public static void main(String[] args) {
        IO.println(RandomUtils.nextInt());
    }
}
```
Javaプログラムを実行する。
```
java25 ReferenceNonJDKClass.java
```
実行結果。クラスパスを指定していないのでエラーになる。
```
ReferenceNonJDKClass.java:1: error: package org.apache.commons.lang3 does not exist
import org.apache.commons.lang3.RandomUtils;
                               ^
ReferenceNonJDKClass.java:5: error: cannot find symbol
        IO.println(RandomUtils.nextInt());
                   ^
  symbol:   variable RandomUtils
  location: class ReferenceNonJDKClass
2 errors
error: compilation failed
```
Apache Commons Lang 3.20.0 (Java 8+)をダウンロードする。
```
cd /home/mizuki/workspace/java25/launchsimplesourcecodeprograms/lib
wget https://dlcdn.apache.org//commons/lang/binaries/commons-lang3-3.20.0-bin.tar.gz
```
ダウンロードしたファイルを展開する。
```
tar -xvf commons-lang3-3.20.0-bin.tar.gz
```
クラスパスを指定してJavaプログラムを実行する。
```
cd /home/mizuki/workspace/java25/launchsimplesourcecodeprograms
java25 -cp ./lib/commons-lang3-3.20.0/commons-lang3-3.20.0.jar ReferenceNonJDKClass.java
```
実行結果。非推奨のメソッドを使用している警告が表示される。
```
ReferenceNonJDKClass.java:5: warning: [deprecation] nextInt() in RandomUtils has been deprecated
        IO.println(RandomUtils.nextInt());
                              ^
1 warning
399830253
```
推奨されているメソッド(RandomUtils.secure())を使用して、ReferenceNonJDKClass.javaファイルを更新する。
```
import org.apache.commons.lang3.RandomUtils;

public class ReferenceNonJDKClass {
    public static void main(String[] args) {
        IO.println(RandomUtils.secure().randomInt());
    }
}
```
クラスパスを指定してJavaプログラムを実行する。
```
cd /home/mizuki/workspace/java25/launchsimplesourcecodeprograms
java25 -cp ./lib/commons-lang3-3.20.0/commons-lang3-3.20.0.jar ReferenceNonJDKClass.java
```
実行結果。
```
1752525595
```

ShebangファイルとしてJavaプログラムを実行する。ShebangHelloJavaファイルに以下のコードを記述する。
```
#!/usr/local/bin/java25 --source 25

public class HelloJava {
    public static void main(String[] args) {
        IO.println("Shebang Hello " + args[0]);
    }
}
```
ShebangHelloJavaファイルに実行権限を付与する。
```
chmod +x ShebangHelloJava
```
ShebangHelloJavaファイルを実行する。
```
./ShebangHelloJava World
```
実行結果。
```
Shebang Hello World
```

参考情報。
- [Launching Simple Source-Code Programs | Dev.java](https://dev.java/learn/launch-simple-source-code-programs/)
- [IO (JavaSE 25 & JDK 25)](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/IO.html)
- [Scanner (JavaSE 25 & JDK 25)](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Scanner.html)
- [MatchResult (JavaSE 25 & JDK 25)](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/regex/MatchResult.html)
- [JavaのPatternとは？正規表現のパターンを表すクラス「Pattern」を解説](https://www.bold.ne.jp/engineer-club/java-pattern)
- [Home - Apache Commons Lang](https://commons.apache.org/proper/commons-lang/)
- [org.apache.commons.lang3 > RandomUtils.secure()](https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/RandomUtils.html#secure())
