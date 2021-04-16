import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    Random random = new Random();
    int[][] map;

    TicTacToe() {
        map = new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    }

    int playTicTacToe(GeneticCode player, GeneticCode enemy, boolean showGame, int fall, int win) {
        int turns = 0;

        while (true) {
            turns++;
            int turn = GeneticCode.chooseBestNeuron(player.think(mapToChromosome()));
            int i = turn / 3;
            int j = turn % 3;
            if (!checkPlaceToMove(i, j)) {
                enemy.power+=turns;
                player.power-=fall-turns;
                return -1;
            } else {
                move(1, i, j);
            }

            if (showGame) {
                System.out.println(toString());
            }

            if (checkWin()!= 0) {
                player.power+=win + turns;
                enemy.power+=turns;
                return 1;
            }

            if (!checkDraw(map)) {
                turns++;
                turn = GeneticCode.chooseBestNeuron(enemy.think(mapToChromosome()));
                i = turn / 3;
                j = turn % 3;
                if (!checkPlaceToMove(i, j)) {
                    enemy.power-=fall-turns;
                    enemy.power+=turns;
                    return 1;
                } else {
                    move(-1, i, j);
                }
            } else {
                player.power+=turns + win/2;
                enemy.power+=turns + win/2;
                return 0;
            }

            if (checkWin()!= 0) {
                enemy.power+=win+turns;
                player.power+=turns;
                return -1;
            }

            if (showGame) {
                System.out.println(toString());
            }
        }
    }

    int playTicTacToe(GeneticCode enemy) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter i");
            int i = scanner.nextInt();
            System.out.println("Enter j");
            int j = scanner.nextInt();

            while (!checkPlaceToMove(i, j)) {
                System.out.println("Wrong turn");
                System.out.println("Enter i");
                i = scanner.nextInt();
                System.out.println("Enter j");
                j = scanner.nextInt();
            }

            move(1, i, j);

            System.out.println(toString());

            if (checkWin()!= 0) {
                System.out.println("You won!");
                return 1;
            }

            if (!checkDraw(map)) {
                int turn = GeneticCode.chooseBestNeuron(enemy.think(mapToChromosome()));
                i = turn / 3;
                j = turn % 3;
                if (!checkPlaceToMove(i, j)) {
                    int[][] newMap = map;
                    while(!checkPlaceToMove(i, j)) {
                        i = random.nextInt(3);
                        j = random.nextInt(3);
                    }
                    move(-1, i, j);
                    System.out.println(toString());
                    if (checkDraw(newMap)) {
                        System.out.println("You won!");
                        return 1;
                    }
                } else {
                    move(-1, i, j);
                    System.out.println(toString());
                }
            } else {
                System.out.println("Draw");
            }

            if (checkWin()!= 0) {
                System.out.println("Enemy won!");
                return -1;
            }

        }
    }

    boolean checkPlaceToMove(int i, int j) {
        try{
            if (map[i][j] == 0) {
                return true;
            }
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }

    boolean move(int player, int i, int j) {
        if (map[i][j] == 0) {
            map[i][j] = player;
            return true;
        }
        return false;
    }

    boolean checkDraw(int[][] map) {
        int result = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result *= map[i][j];
            }
        }
        if (result == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    int checkWin() {
        int sum;
        for (int i = 0; i < 3; i++) {
            sum = 0;
            for (int j = 0; j < 3; j++) {
                sum+=map[i][j];
            }
            if (sum == 3) {
                return 1;
            } else if (sum == -3) {
                return -1;
            }
        }

        for (int j = 0; j < 3; j++) {
            sum = 0;
            for (int i = 0; i < 3; i++) {
                sum+=map[i][j];
            }
            if (sum == 3) {
                return 1;
            } else if (sum == -3) {
                return -1;
            }
        }

        sum = map[0][0] + map[1][1] + map[2][2];
        if (sum == 3) {
            return 1;
        } else if (sum == -3) {
            return -1;
        }

        sum = map[0][2] + map[1][1] + map[2][0];
        if (sum == 3) {
            return 1;
        } else if (sum == -3) {
            return -1;
        }
        return 0;
    }

    int[][] chromosomeToMap(Chromosome chromosome) {
        int[][] chromosomeMap = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                chromosomeMap[i][j] = (int) chromosome.getGenes()[i * 3 + j].getValue();
            }
        }
        return chromosomeMap;
    }

    Chromosome mapToChromosome() {
        Chromosome mapChromosome = new Chromosome(9);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mapChromosome.setGene(i*3 + j, map[i][j]);
            }
        }
        return mapChromosome;
    }

    Chromosome mapToChromosome(int[][] map) {
        Chromosome mapChromosome = new Chromosome(9);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mapChromosome.setGene(i*3 + j, map[i][j]);
            }
        }
        return mapChromosome;
    }

    Chromosome mapToChromosomeWithSpace(int[][] map, int a, int b) {
        Chromosome mapChromosome = new Chromosome(9);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mapChromosome.setGene(i*3 + j, map[i][j]);
            }
        }
        mapChromosome.setGene(a*3 + b, -1);
        return mapChromosome;
    }

    @Override
    public String toString() {
        String string = "---\n";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                 string += playerToChar(map[i][j]);
            }
            string += "\n";
        }
        string += "---\n";
        return string;
    }

    char playerToChar(int player) {
        switch (player) {
            case 1:
                return 'X';
            case -1:
                return 'O';
            default:
                return ' ';
        }
    }
}
