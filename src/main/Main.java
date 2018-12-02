package main;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static final int CONTINUATION = 0;
    private static boolean BINGO = false;
    private static boolean LAST_ONE = false;


    /**
     * bingoゲームを進行する
     * @param args
     */

    public static void main(String[] args) {
        List<List<String>> list = createNumbers();
        List<Integer> num = getRandomNumbers();
        List<String> winningList = new ArrayList<>();

        //初回出力
        firstAnnouncement(list);
        list.get(2).set(2, "  ");
        Scanner scan = new Scanner(System.in);

        int resultCount = 0;
        boolean fin = true;
        while (fin) {
            String input = scan.nextLine();
            if (input != null) {

                //当選結果を結合
                winningList.add(num.get(resultCount).toString());
                StringJoiner join = new StringJoiner(",");
                winningList.stream().forEach(e -> join.add(e));

                // 現在のターン数、当選番号、当選結果を出力
                System.out.println(resultCount + 1 + "ターン目");
                System.out.println("現在の当選結果は " + num.get(resultCount) + " 番です");
                System.out.println("今までの当選結果：" + join.toString());
                System.out.println();

                // 途中結果を出力
                announcement(list, num.get(resultCount));

                // 判定結果で終了か継続か
                judgment(list);

                System.out.println();

                //ビンゴなら終了
                //リーチなら出力
                if(BINGO){
                    System.out.println("ビンゴ！！");
                    fin = false;
                }
                if(LAST_ONE && !BINGO){
                    System.out.println("リーチ！！");
                }
                resultCount++;
            }
        }
        scan.close();
        System.out.println("終わり");
    }

    /**
     * シートの初期状態を出力
     * @param list
     */
    private static void firstAnnouncement(List<List<String>> list) {

        list.get(2).set(2, "W");
        list.stream().forEach(l ->{
            System.out.print("|");
            l.stream().forEach(s -> {
                if(s.length() < 2){
                    s += " ";
                }
                System.out.print(s);
                System.out.print("|");
            });
            System.out.println();
        });
    }

    /**
     * 途中経過を出力
     * @param list
     * @param number
     */
    private static void announcement(List<List<String>> list, int number) {


        list.stream().forEach(strings -> {
            if (strings.contains(String.valueOf(number))) {
                strings.set(strings.indexOf(String.valueOf(number)), "  ");
            }
        });
        list.stream().forEach(l -> {
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
     * @param list
     * @return
     */
    private static void judgment(List<List<String>> list) {


        //横判定
        list.stream().forEach(l->{
           Boolean bingo = l.stream().filter(s -> s.equals("  ")).count() == 5;
           if (bingo){
               BINGO = true;
           }
           Boolean lastOne = l.stream().filter(s -> s.equals("  ")).count() == 4;
           if (lastOne){
               LAST_ONE = true;
           }
        });

        //縦判定
        IntStream.rangeClosed(0, 4).forEach(i -> {

            List<Boolean> bingo = new ArrayList<>();

            list.stream().forEach( a -> {
                if(a.get(i) == "  ") {
                    bingo.add(true);
                }
                if(bingo.size() == 4) {
                    LAST_ONE = true;
                }
                if(bingo.size() == 5) {
                    BINGO = true;
                }
            });
        });
    }

    /**
     * ランダムな数字のリストを生成(5×5)
     * @return
     */
    private static List<List<String>> createNumbers () {

        List<List<String>> sheet = Arrays.asList(new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
        List<Integer> numbers = getNumbers();

            numbers.stream().forEach(integer -> {
                if(numbers.indexOf(integer) < 5){
                    sheet.get(0).add(integer.toString());
                }else if(numbers.indexOf(integer) < 10){
                    sheet.get(1).add(integer.toString());
                }else if(numbers.indexOf(integer) < 15){
                    sheet.get(2).add(integer.toString());
                }else if(numbers.indexOf(integer) < 20){
                    sheet.get(3).add(integer.toString());
                }else {
                    sheet.get(4).add(integer.toString());
                }
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