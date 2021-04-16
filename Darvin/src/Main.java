import javax.management.timer.Timer;
import javax.websocket.server.ServerEndpointConfig;
import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] Args) throws IOException, ClassNotFoundException {
        String mainPath = "C:\\Users\\Max\\Haskell\\Darvin\\";

        int countOfGenerations = 100;

        Gene defaultGene = new Gene(1, 1, 3);
        NaturalSelection naturalSelection = new NaturalSelection(8, 21, defaultGene);
        Chromosome geneticCode;

        Server server = new Server(3456);
        String message;
        String saveFileName = "";
        boolean save = false;
        boolean load = false;

        while(true) {
            do {
                message = server.run(ConnectionMode.Get, "");
                if (message.contains("Load ")) {
                    load = true;
                    if (message.split(" ").length > 1) {
                        geneticCode = (Chromosome)loadGeneticCode(mainPath + message.split(" ")[1] + ".gc");
                    }
                    else {
                        geneticCode = (Chromosome)loadGeneticCode(mainPath + "default.gc");
                    }
                    naturalSelection.setGeneticCodes(geneticCode);
                } else if (message.contains("Save ")) {
                    save = true;
                    if (message.split(" ").length > 1) {
                        saveFileName = message.split(" ")[1];
                    }
                    else {
                        saveFileName = "default";
                    }
                }
            }
            while(!message.equals("StartGame") && !load);

            while (countOfGenerations <= 1000) {
                naturalSelection.startNaturalSelection(SelectionCriteria.Tournament, MutationType.RandomMutation, 3, CompletionType.NumberOfGenerations, countOfGenerations);
                GameSticks game = new GameSticks(naturalSelection);

                if (game.playSticks(server) == 1) {
                    countOfGenerations += 100;
                }
            }

            if (save) {
                saveGeneticCode(mainPath + saveFileName + ".gc", naturalSelection.getBestChromosome());
            }

            server.run(ConnectionMode.Send, "StopGame");
        }

        /*
        Gene defaultGene = new Gene(1);
        NaturalSelection naturalSelection = new NaturalSelection(8, 18, 10, defaultGene);*/

        /*
        naturalSelection.setGeneticCodes(
                new GeneticCode[] {loadGeneticCode(mainPath + "11.ser"),
                        loadGeneticCode(mainPath + "12.ser"),
                        loadGeneticCode(mainPath + "13.ser")});*/

        /*
        int numberOfGeneration = 100000;
        while (numberOfGeneration <= 100000) {
            naturalSelection.startNaturalSelection(SelectionCriteria.Tournament, MutationType.RandomMutation, 180, CompletionType.NumberOfGenerations, numberOfGeneration);
            GeneticCode geneticCode = (GeneticCode)naturalSelection.getBestChromosome();
            saveGeneticCode(mainPath + "5" + numberOfGeneration/100000 + ".ser", geneticCode);
            numberOfGeneration += 100000;
        }

        GeneticCode geneticCode = loadGeneticCode(mainPath + "51.ser");
        TicTacToe ticTacToe = new TicTacToe();
        ticTacToe.playTicTacToe(geneticCode, geneticCode, true, 0, 0);*/
        /*GeneticCode geneticCode = loadGeneticCode(mainPath + "save4.ser");

        System.out.println(geneticCode);
        TicTacToe ticTacToe = new TicTacToe();
        ticTacToe.playTicTacToe(geneticCode);*/
/*
        Thread myThready = new Thread(new Runnable()
        {
            public void run() //Этот метод будет выполняться в побочном потоке
            {
                System.out.println("Привет из побочного потока!");
                TicTacToe ticTacToe = new TicTacToe();
                System.out.println(naturalSelection.getBestChromosome());
                ticTacToe.playTicTacToe((GeneticCode)naturalSelection.getBestChromosome());
                ticTacToe = new TicTacToe();
                System.out.println(naturalSelection.getBestChromosome());
                ticTacToe.playTicTacToe((GeneticCode)naturalSelection.getBestChromosome());
                ticTacToe = new TicTacToe();
                System.out.println(naturalSelection.getBestChromosome());
                ticTacToe.playTicTacToe((GeneticCode)naturalSelection.getBestChromosome());
                ticTacToe = new TicTacToe();
                System.out.println(naturalSelection.getBestChromosome());
                ticTacToe.playTicTacToe((GeneticCode)naturalSelection.getBestChromosome());
                ticTacToe = new TicTacToe();
                System.out.println(naturalSelection.getBestChromosome());
                ticTacToe.playTicTacToe((GeneticCode)naturalSelection.getBestChromosome());
                ticTacToe = new TicTacToe();
                System.out.println(naturalSelection.getBestChromosome());
                ticTacToe.playTicTacToe((GeneticCode)naturalSelection.getBestChromosome());
            }
        });
        myThready.start();

        naturalSelection.startNaturalSelection(SelectionCriteria.Tournament, MutationType.RandomMutation, 180, CompletionType.NumberOfGenerations, 10000000);

        System.out.println("Конец обучения");*/


    }

    static boolean saveGeneticCode(String path, Selectable geneticCode) {
        try {
            FileOutputStream outputStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(geneticCode);

            objectOutputStream.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    static Selectable loadGeneticCode(String path) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        Selectable geneticCode = (Selectable)objectInputStream.readObject();
        return geneticCode;
    }
}