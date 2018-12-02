package main;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BingoV2 {
    public static final int CONTINUATION = 0;
    private static boolean BINGO = false;
    private static boolean LAST_ONE = false;


    /**
     * bingoゲームを進行する
     * @param args
     */

    public static void main(String[] args) {
        List<List<String>> sheet = createNumbers();
        List<Integer> numbers = getRandomNumbers();
        List<String> winningNumbers = new ArrayList<>();

        //初回出力...初回は真ん中が"w "から始める
        sheet.get(2).set(2, "w ");
        announcement(sheet);

        sheet.get(2).set(2, "  ");
        Scanner scan = new Scanner(System.in);

        int resultCount = 0;
        while (!BINGO) {
            String input = scan.nextLine();
            if (input != null) {

                //当選結果を結合
                winningNumbers.add(numbers.get(resultCount).toString());
                StringJoiner join = new StringJoiner(",");
                winningNumbers.stream().forEach(e -> join.add(e));

                // 現在のターン数、当選番号、当選結果を出力
                System.out.println(resultCount + 1 + "ターン目");
                System.out.println("現在の当選結果は " + numbers.get(resultCount) + " 番です");
                System.out.println("今までの当選結果：" + join.toString());
                System.out.println();


                //当選番号をsheetから除く
                int number = numbers.get(resultCount);
                sheet.stream().forEach(strings -> {
                    if (strings.contains(String.valueOf(number))) {
                        strings.set(strings.indexOf(String.valueOf(number)), "  ");
                    }
                });

                // 途中結果を出力
                announcement(sheet);

                // 判定結果で終了か継続か
                judgment(sheet);

                System.out.println();

                //ビンゴなら終了
                //リーチなら出力
                if(BINGO){
                    System.out.println("ビンゴ！！");
                    break;
                }
                if(LAST_ONE){
                    System.out.println("リーチ！！");
                }
                resultCount++;
            }
        }
        scan.close();
        System.out.println("終わり");
    }

    /**
     * 途中経過を出力
     * @param sheet
     */
    private static void announcement(List<List<String>> sheet) {

        sheet.stream().forEach(l -> {
            System.out.print("|");
            l.stream().forEach(s ->  {
                if(s.length() < 2){
                    s += " ";
                }
                System.out.print(s + "|");
            });
            System.out.println();
        });
    }

    /**
     * bingoか継続もしくはリーチかを判定する
     * @param sheet
     */
    private static void judgment(List<List<String>> sheet) {


        //横判定
        sheet.stream().forEach(l -> {
            int matchCount = (int) l.stream().filter(s -> s.equals("  ")).count();
            if (matchCount == 5) {
                BINGO = true;
            }
            if (matchCount == 4) {
                LAST_ONE = true;
            }
        });

        //縦判定
        IntStream.rangeClosed(0, 4).forEach(i -> {
            int matchCount = ((int) sheet.stream().filter(a -> a.get(i).equals("  ")).count());
            if (matchCount == 5) {
                BINGO = true;
            }
            if (matchCount == 4) {
                LAST_ONE = true;
            }
        });
    }

    /**
     * ランダムな数字のリストを生成(5×5)
     * @return
     */
    private static List<List<String>> createNumbers () {

        List<Integer> numbers = getNumbers();

        List<List<String>> sheet = new ArrayList<>();
        IntStream.rangeClosed(0, 4).forEach(i -> {
            List<String> row = new ArrayList<>();
            sheet.add(row);
            IntStream.rangeClosed(0, 4).forEach(x -> {
                row.add(numbers.get(i * 5 + x).toString());
            });
        });
        return sheet;
    }


    /**
     * 1〜99までのランダムな数字を生成
     * @return
     */
    private static List<Integer> getRandomNumbers () {

        List<Integer> numbers = IntStream.range(1,99).boxed().collect(Collectors.toList());
        Collections.shuffle(numbers);

        return numbers;
    }

    /**
     * シートのサイズ分のランダムな番号を抜粋
     * @return
     */
    public static List<Integer> getNumbers () {

        List<Integer> result = getRandomNumbers().stream().filter(i -> i <= 25).collect(Collectors.toList());
        return result;
    }
}
