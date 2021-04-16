import java.util.Arrays;
import java.util.Comparator;

enum SelectionCriteria
{
    MaximumPower,
    MinimumPower,
    Tournament
}

enum CompletionType
{
    NumberOfGenerations,
    TargetValue,
    GenesConstancy
}

enum TournamentType
{
    FullCircle
}

public class NaturalSelection {
    private Selectable[] geneticCodes;
    private Selectable[] transitionState;
    private int generationNumber;
    private int[] bestPower;

    NaturalSelection(int countOfChromosomes, int countOfGenes, Gene defaultGene) {
        geneticCodes = new Chromosome[countOfChromosomes];
        generationNumber = 0;

        for (int i = 0; i < countOfChromosomes; i++)
        {
            geneticCodes[i] = new Chromosome(countOfGenes, defaultGene);
        }
        transitionState = new Chromosome[geneticCodes.length * geneticCodes.length];
    }

    NaturalSelection(int countOfGeneticCodes, int countOfChromosomes, int countOfGenes, Gene defaultGene) {
        geneticCodes = new GeneticCode[countOfGeneticCodes];
        generationNumber = 0;

        for (int i = 0; i < countOfGeneticCodes; i++)
        {
            geneticCodes[i] = new GeneticCode(countOfChromosomes, countOfGenes, defaultGene);
        }
        transitionState = new GeneticCode[geneticCodes.length * geneticCodes.length];
    }

    double getBestPower()
    {
        return geneticCodes[0].getPower();
    }

    Selectable getBestChromosome() {
        return  geneticCodes[0];
    }

    void setGeneticCodes(Selectable geneticCode) {
        for (int i = 0; i < geneticCodes.length; i++) {
            geneticCodes[i] = geneticCode;
        }
    }

    void setGeneticCodes(Selectable[] geneticCodes) {
        int i = 0;
        while(i * geneticCodes.length < this.geneticCodes.length) {
            for (int j = 0; j < geneticCodes.length && i * geneticCodes.length + j < this.geneticCodes.length; j++) {
                this.geneticCodes[i* geneticCodes.length + j] = geneticCodes[j];
            }
            i++;
        }
    }

    /*
    NumberOfGeneration:
    0. Count of generations
    TargetValue:
    0. Maximum count of generations
    1. Target value
     */
    void startNaturalSelection(SelectionCriteria selectionCriteria, MutationType mutationType, int mutationPower, CompletionType completionType, int... completionArguments) {
        switch (completionType) {
            case NumberOfGenerations:
                while(generationNumber < completionArguments[0]) {
                    newGeneration(selectionCriteria, mutationType, mutationPower);
                }
                break;
            case TargetValue:
                while(generationNumber < completionArguments[0] && newGeneration(selectionCriteria, mutationType, mutationPower) != completionArguments[1]) { }
                break;
            /*case GenesConstancy:
                bestPower = new int[completionArguments[0]];
                while(generationNumber < Math.min(completionArguments[0], completionArguments[1])) {
                    bestPower[generationNumber] = newGeneration(selectionCriteria, mutationType, mutationPower);
                }
                bestPower[generationNumber] = getBestPower();
                while(generationNumber < completionArguments[0] && newGeneration(selectionCriteria, mutationType, mutationPower) != bestPower[generationNumber - completionArguments[1]]) {
                    bestPower[generationNumber] = getBestPower();
                }
                break;*/
        }
    }

    void crossing() {
        for(int i = 0; i < geneticCodes.length; i++) {
            transitionState[i] = (Selectable) geneticCodes[i].clone();
        }
        int k = geneticCodes.length;
        for (int i = 0; i < geneticCodes.length - 1; i++) {
            for (int j = i; j < geneticCodes.length; j++) {
                Selectable selectable = (Selectable) geneticCodes[i].crossing(geneticCodes[j]);
                transitionState[k] = selectable;
                k++;
            }
        }
    }

    void mutation(MutationType mutationType, int powerOfMutation) {
        int k = geneticCodes.length + transitionState.length / 2;
        for (int i = k; i < transitionState.length; i++) {
            transitionState[i] = (Selectable) transitionState[i - transitionState.length / 2].clone();
            transitionState[i].mutation(mutationType, powerOfMutation);
        }
    }

    void setTransitionStatePowers() {
        for(int i = 0; i < transitionState.length; i++) {
            transitionState[i].setPower();
        }
    }

    void runTournament(TournamentType tournamentType) {
        switch (tournamentType) {
            case FullCircle:
                for(int i = 0; i < transitionState.length - 1; i++) {
                    for(int j = i + 1; j < transitionState.length; j++) {
                        transitionState[i].fight(transitionState[j]);
                    }
                }
                break;
        }
    }

    void selection(SelectionCriteria selectionCriteria) {
        switch (selectionCriteria) {
            case MaximumPower:
                setTransitionStatePowers();
                Arrays.sort(transitionState, new ChromosomeCompareMax());
                break;
            case MinimumPower:
                setTransitionStatePowers();
                Arrays.sort(transitionState, new ChromosomeCompareMin());
                break;
            case Tournament:
                runTournament(TournamentType.FullCircle);
                Arrays.sort(transitionState, new ChromosomeCompareMax());
                break;
        }

        for (int i = 0; i < geneticCodes.length; i++) {
            geneticCodes[i] = transitionState[i];
        }
    }

    double newGeneration(SelectionCriteria selectionCriteria, MutationType mutationType, int powerOfMutation) {
        generationNumber++;
        crossing();
        mutation(mutationType, powerOfMutation);
        selection(selectionCriteria);
        showGenerationState(false);
        return getBestPower();
    }

    String getTransitionState() {
        String generationTransitionState = "Transition state generation: " + generationNumber + "\n";
        for (int i = 0; i < transitionState.length; i++) {
            generationTransitionState += (transitionState[i].toString() + "\n");
        }

        return generationTransitionState;
    }

    void showTransitionState() {
        System.out.print(getTransitionState());
    }

    String getGenerationState() {
        String generationState = "Generation: " + generationNumber + "\n";
        for (int i = geneticCodes.length - 1; i >= 0; i--) {
            generationState += (geneticCodes[i].toString() + "\n");
        }

        return generationState;
    }

    void showGenerationState(boolean isTransition) {
        if(isTransition) {
            showTransitionState();
        }
        System.out.print(getGenerationState());
    }
}

class ChromosomeCompareMax implements Comparator<Selectable> {
    public int compare(Selectable firstChromosome, Selectable secondChromosome) {
        return (int)(secondChromosome.getPower() - firstChromosome.getPower());
    }
}

class ChromosomeCompareMin implements Comparator<Selectable> {
    public int compare(Selectable firstChromosome, Selectable secondChromosome) {
        return (int)(firstChromosome.getPower() - secondChromosome.getPower());
    }
}
