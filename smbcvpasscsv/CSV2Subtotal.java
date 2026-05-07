import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 三井住友カード会員向けインターネットサービス『Vpass』の利用明細のCSVファイルから
 * 「利用店名」ごとの「支払い金額（今回の支払い金額）」の小計および累計を出力する。
 * 
 * 利用明細のCSVファイルは、月の支払い金額の確定前と確定後で項目数が異なっている。
 * このプログラムでは、月の支払い金額の確定後のCSVファイルを想定している。
 * 
 * CSVファイルの明細行は以下のような形式の6項目であると仮定する。
 * 利用日(YYYY/MM/DD),利用店名,利用金額（総支払い金額）,支払い区分（内部情報のため不明）,分割払いにおける今回の回数,支払い金額（今回の支払い金額）
 * 
 * その他の仕様は以下の通りとする。
 * ・文字エンコーディングはShift_JIS、改行コードはCRLFである。
 * ・ヘッダー行は存在しない。
 * ・1行目はクレジットカード情報、最終行は合計金額の行であるため、これらの行は無視する。
 * ・空行も存在する可能性があるため、空行も無視する。
 * ・このプログラムで使用するのは「利用店名」と「支払い金額（今回の支払い金額）」の2項目のみとする。
 */
public class CSV2Subtotal {
    /**
     * CSVデータを解析して「利用店名」ごとの「支払い金額（今回の支払い金額）」の小計および累計を計算して別ファイルに出力する。
     * 
     * 事前の処理として、手作業でCSVファイルの文字エンコーディングをUTF-8に変換する。
     * 入力データ
     *     /home/mizuki/download/credit_card_statement/202603.csv
     * 出力データ
     *     /home/mizuki/download/credit_card_statement/202603_utf8.csv
     * 
     * 以下に入力データと出力データの例を示す。
     * 出力データにはヘッダー行を追加する。
     * 入力データ
     *     ファイルのパス
     *         /home/mizuki/download/credit_card_statement/202603_utf8.csv
     *     ファイルの内容
     *         2026/02/27,まいばすけっと,591,1,1,591
     *         2026/02/26,まいばすけっと,670,1,1,670
     *         2026/02/25,まいばすけっと,3150,1,1,3150
     *         2026/02/24,バーガーキング,870,1,1,870
     *         2026/02/23,バーガーキング,870,1,1,870
     *         2026/02/05,スマートＥＸ（ＪＲ東海）,13990,1,1,13990
     * 出力データ
     *     ファイルのパス
     *         /home/mizuki/download/credit_card_statement/202603_utf8_subtotal.csv
     *     ファイルの内容
     *         "利用店名","支払い金額（今回の支払い金額）小計","支払い金額（今回の支払い金額）累計"
     *         "まいばすけっと","4411","4411"
     *         "バーガーキング","1740","6151"
     *         "スマートＥＸ（ＪＲ東海）","13990","20141"
     * 
     * @param args コマンドライン引数。[0]CSVファイルのフルパス
     */
    public static void main(String[] args) {
        run(args);
    }
    static void run(String[] args) {
        //引数が指定されていない場合はエラーメッセージを表示して終了する。
        if (args.length < 1) {
            System.err.println("CSVファイルのフルパスを指定してください。");
            System.exit(1);
        }

        //引数[0]のCSVファイルのフルパスが存在しない場合はエラーメッセージを表示して終了する。
        Path inputCsvPath = Path.of(args[0]);
        if (!Files.exists(inputCsvPath)) {
            System.err.println("指定されたCSVファイルが存在しません: " + inputCsvPath);
            System.exit(1);
        }

        //CSVファイルを読み込む。
        //空行を除外してから、1行目（クレジットカード情報）と最終行（合計金額）を除外して「利用店名」と「支払い金額（今回の支払い金額）」を格納するCreditCardTransactionのリストを作成する。
        List<CreditCardTransaction> transactions = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(inputCsvPath).stream()
                    .filter(line -> !line.isBlank())
                    .collect(Collectors.toList());
            lines.stream()
                    .skip(1)
                    .limit(Math.max(0, lines.size() - 2L))
                    .forEach(line -> transactions.add(CreditCardTransaction.fromCsvLine(line)));
        } catch (IOException e) {
            System.err.println("CSVファイルの読み込みに失敗しました: " + e.getMessage());
            System.exit(1);
        }

        //「利用店名」ごとの「支払い金額（今回の支払い金額）」の小計を計算する。
        Map<String, Integer> subtotals = transactions.stream()
                .collect(Collectors.groupingBy(CreditCardTransaction::storeName,
                                               Collectors.summingInt(CreditCardTransaction::amount)));

        //「支払い金額（今回の支払い金額）」の降順、「利用店名」の昇順でソートする。
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

        //ヘッダーを追加する。
        outputLines.add(0, String.join(",", csvQuote("利用店名"), csvQuote("支払い金額（今回の支払い金額）小計"), csvQuote("支払い金額（今回の支払い金額）累計")));

        //ファイルに出力する。
        PathComponents pathComponents = splitPath(inputCsvPath);
        Path outputCsvPath = Path.of(pathComponents.directory(), pathComponents.fileNameWithoutExtension() + "_subtotal.csv");
        try {
            Files.write(outputCsvPath, outputLines);
            System.out.println("「利用店名」ごとの「支払い金額（今回の支払い金額）」の小計および累計を出力しました: " + outputCsvPath);
        } catch (IOException e) {
            System.err.println("CSVファイルの書き込みに失敗しました: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * CSVの明細行から「利用店名」と「支払い金額（今回の支払い金額）」を格納する。
     * 
     * CSVの明細行は以下のような形式の6項目であると仮定する。
     * [0]利用日(YYYY/MM/DD)
     * [1]利用店名
     * [2]利用金額（総支払い金額）
     * [3]支払い区分（内部情報のため不明）
     * [4]分割払いにおける今回の回数
     * [5]支払い金額（今回の支払い金額）
     */
    private record CreditCardTransaction(String storeName, int amount) {
        public static CreditCardTransaction fromCsvLine(String csvLine) {
            String[] parts = csvLine.split(",");
            if (parts.length < 6) {
                throw new IllegalArgumentException("CSVの明細行の項目数が不足しています: " + csvLine);
            }
            return new CreditCardTransaction(parts[1], Integer.parseInt(parts[5]));
        }
    }

    /**
     * CSVの値をクオートする。
     * 値にダブルクオートが含まれている場合は、すべてのダブルクオートを削除してからクオートする。
     * @param value CSVの値
     * @return クオートされたCSVの値
     */
    private static String csvQuote(String value) {
        if (value.contains("\"")) {
            return "\"" + value.replace("\"", "") + "\"";
        }
        return "\"" + value + "\"";
    }

    /**
     * ファイルパスをディレクトリ、ファイル名（拡張子なし）、拡張子（ドットなし）に分割する。
     */
    private record PathComponents(String directory, String fileNameWithoutExtension, String extension) {}
    /**
     * ファイルパスをディレクトリ、ファイル名（拡張子なし）、拡張子（ドットなし）に分割する。
     * @param path ファイルパス
     * @return ファイルパスをディレクトリ、ファイル名（拡張子なし）、拡張子（ドットなし）に分割した結果
     */
    private static PathComponents splitPath(Path path) {
        String directory = path.getParent() != null ? path.getParent().toString() : "";
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return new PathComponents(directory, fileName, "");
        }
        return new PathComponents(directory, fileName.substring(0, dotIndex), fileName.substring(dotIndex + 1));
    }
}
