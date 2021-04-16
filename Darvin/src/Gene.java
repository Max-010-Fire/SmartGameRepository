import java.io.Serializable;

enum MutationType {
    IncreaseMutation,
    DecreaseMutation,
    RandomMutation
}

public class Gene implements Serializable {
    private double value;
    private double minimalValue = Integer.MIN_VALUE;
    private double maximumValue = Integer.MAX_VALUE;

    Gene() {
        value = 0;
    }

    Gene(double defaultValue) {
        value = defaultValue;
    }

    Gene(double defaultValue, double minimalValue, double maximumValue) {
        this.minimalValue = minimalValue;
        this.maximumValue = maximumValue;
        setValue(defaultValue);
    }

    void mutation(MutationType mutationType, int... randomParameters) {
        switch (mutationType) {
            case IncreaseMutation:
                setValue(value + 1);
                break;
            case DecreaseMutation:
                setValue(value - 1);
                break;
            case RandomMutation:
                /*
                setValue((randomParameters[0]==-1)?value*0.9:(randomParameters[0]==1?value*1.1:value));*/
                setValue(value + randomParameters[0]);
                break;
        }
    }

    double getValue() {
        return value;
    }

    void setValue(double newValue) {
        if (newValue <= maximumValue && newValue >= minimalValue) {
            value = newValue;
        } else if (newValue > maximumValue) {
            value = maximumValue;
        } else if (newValue < minimalValue) {
            value = minimalValue;
        }
    }

    static int getMaximumGene(Gene[] genes) {
        int maximumGene = -1;
        double maximum = Double.MIN_VALUE;
        for (int i = 0; i < genes.length; i++) {
            if (genes[i].getValue() > maximum) {
                maximum = genes[i].getValue();
                maximumGene = i;
            }
        }
        return maximumGene;
    }

    @Override
    public String toString() {
        String geneState = "" + value;
        return geneState;
    }

    @Override
    protected Gene clone() {
        Gene gene = new Gene();
        gene.value = value;
        gene.minimalValue = minimalValue;
        gene.maximumValue = maximumValue;
        return gene;
    }
}
