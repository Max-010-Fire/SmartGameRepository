import java.io.Serializable;
import java.util.*;

public class Chromosome implements Cloneable, Selectable<Chromosome>, Serializable {
    private Random random = new Random();
    private Gene[] genes;
    private int power;

    Chromosome(int countOfGenes) {
        genes = new Gene[countOfGenes];
        for(int i = 0; i < genes.length; i++) {
            genes[i] = new Gene();
        }
    }

    Chromosome(int countOfGenes, Gene defaultGene) {
        genes = new Gene[countOfGenes];
        for(int i = 0; i < genes.length; i++) {
            genes[i] = defaultGene.clone();
        }
    }

    public void setGene(int geneNumber, double geneValue) {
        genes[geneNumber].setValue(geneValue);
    }

    public void setGenes(double[] genesValues) {
        genes = new Gene[genesValues.length];
        for(int i = 0; i < genesValues.length; i++) {
            genes[i] = new Gene(genesValues[i]);
        }
    }

    public Gene[] getGenes() {
        return genes;
    }

    static double getChromosomesPower(Chromosome[] genes) {
        return 0;
    }

    public void mutation(MutationType mutationType, int powerOfMutation) {
        int currentPowerOfMutation;
        switch (mutationType) {
            case RandomMutation:
                currentPowerOfMutation = random.nextInt(powerOfMutation + 1);
                for(int i = 0; i < currentPowerOfMutation; i++) {
                    genes[random.nextInt(genes.length)].mutation(mutationType, -1 + random.nextInt(3));
                }
                break;
            default:
                currentPowerOfMutation = random.nextInt(powerOfMutation + 1);
                for(int i = 0; i < currentPowerOfMutation; i++) {
                    genes[random.nextInt(genes.length)].mutation(mutationType);
                }
        }
    }

    public Chromosome crossing(Chromosome secondChromosome) {
        Chromosome newChromosome = new Chromosome(genes.length);
        String binaryCrossingString = Integer.toBinaryString(random.nextInt(2<<genes.length));

        for (int i = 0; i < genes.length; i++) {
            if (genes.length - i - 1 >= binaryCrossingString.length() || binaryCrossingString.charAt(genes.length - i - 1) == '0') {
                newChromosome.genes[i] = this.genes[i];
            }
            else {
                newChromosome.genes[i] = secondChromosome.genes[i];
            }
        }

        return newChromosome;
    }

    void sortChromosome() {
        Arrays.sort(genes);
    }

    public void setPower() {
        power = 0;
    }

    public int getPower() {
        return power;
    }

    public int fight(Chromosome enemy) {
        int result = 0;

        int points = 0;
        int enemyPoints = 0;

        int stone = 21;

        while (stone > 0) {
            stone -= genes[stone - 1].getValue();
            if (stone == 0) {
                points++;
                break;
            }
            if (stone < 0) {
                enemyPoints++;
                break;
            }
            stone -= enemy.genes[stone - 1].getValue();
            if (stone == 0) {
                enemyPoints++;
                break;
            }
            if (stone < 0) {
                points++;
                break;
            }
        }

        if (points > enemyPoints) {
            result = 1;
            power++;
        }

        if (points < enemyPoints) {
            result = -1;
            enemy.power++;
        }

        return result;
    }

    @Override
    public String toString() {
        String genesState = "";
        for (int i = 0; i < genes.length; i++) {
            genesState += (genes[i].toString() + "|");
        }
        genesState += ("|" + getPower());
        return genesState;
    }

    @Override
    public Chromosome clone() {
        Chromosome chromosome = new Chromosome(genes.length);
        for( int i = 0; i < genes.length; i++) {
            chromosome.genes[i] = genes[i].clone();
        }
        return  chromosome;
    }
}
