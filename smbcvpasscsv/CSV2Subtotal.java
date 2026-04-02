import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 三井住友カード会員向けインターネットサービス『Vpass』の利用明細のCSVファイルから
 * 「利用店名」ごとの「利用金額」の小計および累計を出力する。
 * 
 * CSVファイルの1行は以下のような形式の11項目であると仮定する。
 * 利用日(YYYY/M/D),利用店名,カード使用者,支払い区分,分割回数,支払い予定月('YY/MM),利用金額,支払い総額(現地通貨額),支払い総額(通貨略称),内手数料(換算レート),内手数料(換算日)
 * 
 * CSVファイルにヘッダー行は存在しない。
 * CSVファイルの1行の実データは13項目あるが最後の2項目は内容が不明なため無視する。
 * このプログラムで使用するのは「利用店名」と「利用金額」の2項目のみである。
 */
public class CSV2Subtotal {
    /**
     * CSVデータを解析して「利用店名」ごとの「利用金額」の小計および累計を計算して別ファイルに出力する。
     * 
     * 以下に入力データと出力データの例を示す。
     * 入力データ
     *     ファイルのパス
     *         /home/mizuki/downloads/202603.csv
     *     ファイルの内容
     *         2026/2/27,まいばすけっと,ご本人,1回払い,,'26/03,591,591,,,,,
     *         2026/2/26,まいばすけっと,ご本人,1回払い,,'26/03,670,670,,,,,
     *         2026/2/25,まいばすけっと,ご本人,1回払い,,'26/03,3150,3150,,,,,
     *         2026/2/24,バーガーキング,ご本人,1回払い,,'26/03,870,870,,,,,
     *         2026/2/23,バーガーキング,ご本人,1回払い,,'26/03,870,870,,,,,
     *         2026/2/5,スマートＥＸ（ＪＲ東海）,ご本人,1回払い,,'26/03,13990,13990,,,,,
     * 出力データ
     *     ファイルのパス
     *         /home/mizuki/downloads/202603_subtotal.csv
     *     ファイルの内容
     *         "利用店名","利用金額小計","利用金額累計"
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
        List<CreditCardTransaction> transactions = new ArrayList<>();
        try {
            Files.readAllLines(inputCsvPath).stream()
                    .forEach(line -> transactions.add(CreditCardTransaction.fromCsvLine(line)));
        } catch (IOException e) {
            System.err.println("CSVファイルの読み込みに失敗しました: " + e.getMessage());
            System.exit(1);
        }

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

        //ヘッダーを追加する。
        outputLines.add(0, String.join(",", csvQuote("利用店名"), csvQuote("利用金額小計"), csvQuote("利用金額累計")));

        //ファイルに出力する。
        PathComponents pathComponents = splitPath(inputCsvPath);
        Path outputCsvPath = Path.of(pathComponents.directory(), pathComponents.fileNameWithoutExtension() + "_subtotal.csv");
        try {
            Files.write(outputCsvPath, outputLines);
            System.out.println("小計を出力しました: " + outputCsvPath);
        } catch (IOException e) {
            System.err.println("CSVファイルの書き込みに失敗しました: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * CSVの行から「利用店名」と「利用金額」を格納する。
     */
    private record CreditCardTransaction(String storeName, int amount) {
        public static CreditCardTransaction fromCsvLine(String csvLine) {
            String[] parts = csvLine.split(",");
            return new CreditCardTransaction(parts[1], Integer.parseInt(parts[6]));
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
