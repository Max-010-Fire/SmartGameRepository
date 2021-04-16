import java.io.Serializable;
import java.util.Random;

public class GeneticCode implements Selectable<GeneticCode>, Serializable {
    Chromosome[] chromosomes;
    int power;
    Random random = new Random();

    public GeneticCode(int chromosomesCount) {
        chromosomes = new Chromosome[chromosomesCount];
    }

    public GeneticCode(int chromosomesCount, int countOfGenes, Gene defaultGene) {
        chromosomes = new Chromosome[chromosomesCount];
        for(int i = 0; i < chromosomes.length; i++) {
            chromosomes[i] = new Chromosome(countOfGenes, defaultGene);
        }
    }

    public Chromosome[] getChromosomes() {
        return chromosomes;
    }

    @Override
    public void mutation(MutationType mutationType, int powerOfMutation) {
        int currentMutation = powerOfMutation;
        while (currentMutation > 0) {
            int currentPowerOfMutation = 1 + random.nextInt(currentMutation);
            chromosomes[random.nextInt(chromosomes.length)].mutation(mutationType, currentPowerOfMutation);
            currentMutation -= currentPowerOfMutation;
        }
    }

    @Override
    public GeneticCode crossing(GeneticCode secondGeneticCode) {
        GeneticCode newGeneticCode = new GeneticCode(chromosomes.length);
        for(int i = 0; i < chromosomes.length; i++) {
            newGeneticCode.chromosomes[i] = chromosomes[i].crossing(secondGeneticCode.chromosomes[i]);
        }
        return newGeneticCode;
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public void setPower() {
        power = 0;
    }

    double sigmoid(double x) {
        return (1 / (1 + Math.exp(-x)));
    }

    double computeOneNeuron(Chromosome prevLayer, Chromosome prevLayerWeight, boolean biasNeuron, int numberOfNeuron) {
        double result = 0;
        Gene[] prevGenes = prevLayer.getGenes();
        Gene[] prevWeight = prevLayerWeight.getGenes();
        for (int i = 0; i < prevGenes.length; i++) {
            result += prevGenes[i].getValue() * prevWeight[i].getValue();
        }
        if (biasNeuron) {
            result += prevWeight[prevWeight.length - 1].getValue();
        }
        return Math.max(0, result);
    }

    static int chooseBestNeuron(Chromosome outputLayer) {
        return Gene.getMaximumGene(outputLayer.getGenes());
    }

    public Chromosome think(Chromosome inputLayer) {
        Chromosome hiddenLayer = new Chromosome(9);
        Chromosome outputLayer = new Chromosome(9);

        for (int i = 0; i < hiddenLayer.getGenes().length; i++) {
            hiddenLayer.setGene(i, computeOneNeuron(inputLayer, chromosomes[i], true, i));
        }

        for (int i = 0; i < outputLayer.getGenes().length; i++) {
            outputLayer.setGene(i, computeOneNeuron(hiddenLayer, chromosomes[9 + i], true, i));
        }

        return outputLayer;
    }

    @Override
    public int fight(GeneticCode enemy) {
        int result = 0;

        int firstScore = 0;
        int secondScore = 0;

        TicTacToe ticTacToe = new TicTacToe();
        firstScore = ticTacToe.playTicTacToe(this, enemy, false, 36, 12);

        ticTacToe = new TicTacToe();
        secondScore = ticTacToe.playTicTacToe(enemy, this, false, 36, 12);

        return firstScore-secondScore;
    }

    @Override
    public String toString() {
        String chromosomesState = "";
        for (int i = 0; i < chromosomes.length; i++) {
            chromosomesState += (chromosomes[i].toString() + "\n");
        }
        chromosomesState += ("Power: " + getPower());
        return chromosomesState;
    }

    @Override
    public GeneticCode clone() {
        GeneticCode geneticCode = new GeneticCode(chromosomes.length);
        for( int i = 0; i < chromosomes.length; i++) {
            geneticCode.chromosomes[i] = chromosomes[i].clone();
        }
        return  geneticCode;
    }
}
