import java.util.Scanner;

public class GameSticks {
    NaturalSelection naturalSelection;

    GameSticks(NaturalSelection naturalSelection) {
        this.naturalSelection = naturalSelection;
    }

    int playSticks() {
        Scanner scanner = new Scanner(System.in);

        Chromosome enemy = (Chromosome) naturalSelection.getBestChromosome();

        int stone = 21;
        int turn = 0;

        while (stone > 0) {
            System.out.println("Stones: " + stone);
            System.out.println("Your turn");
            turn = scanner.nextInt();
            while (turn < 1 || turn > 3 || turn > stone) {
                System.out.println("Wrong turn\nTurn must be between 1 and 3 and not more then count of stones");
                turn = scanner.nextInt();
            }

            stone -= turn;
            if (stone == 0) {
                System.out.println("You win!");
                return 1;
            }

            System.out.println("Stones: " + stone);
            System.out.println("Enemy turn: " + (int)enemy.getGenes()[stone - 1].getValue());
            stone -= enemy.getGenes()[stone - 1].getValue();

            if (stone == 0) {
                System.out.println("You lose!");
                return -1;
            }
        }
        return 1;
    }

    int playSticks(Server server) {

        Chromosome enemy = (Chromosome) naturalSelection.getBestChromosome();

        int stone = 21;
        int turn = 0;

        while (stone > 0) {
            server.run(ConnectionMode.Send, "Stones: " + stone);
            server.run(ConnectionMode.Send, "Your turn");

            server.run(ConnectionMode.Send, "Send");
            turn = Integer.parseInt(server.run(ConnectionMode.Get, ""));

            stone -= turn;
            if (stone <= 0) {
                server.run(ConnectionMode.Send, "You win!");
                return 1;
            }

            server.run(ConnectionMode.Send, "Stones: " + stone);
            server.run(ConnectionMode.Send, "Enemy turn: " + (int)enemy.getGenes()[stone - 1].getValue());
            stone -= enemy.getGenes()[stone - 1].getValue();

            if (stone <= 0) {
                server.run(ConnectionMode.Send, "You lose!");
                return -1;
            }
        }
        return 1;
    }
}
