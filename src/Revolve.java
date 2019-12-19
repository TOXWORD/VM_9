import java.util.Arrays;

public class Revolve {

    private double[][] enteredMatrix;
    private double[][] matrix;
    private double[][] ortogon;
    private double[][] eigenmatrix;
    private double[] eigenvalues;
    private double[] residual;
    private double cos;
    private double sin;
    private double maxElement;
    private int iMax;
    private int jMax;
    private int length;
    private int iterationAmount;

    public Revolve() {
        this(5);
    }

    public Revolve(int length) {
        this.length = length;
        iterationAmount = 0;
        cos = 0;
        sin = 0;
        maxElement = 0;
        iMax = 0;
        jMax = 0;
        enteredMatrix = new double[][]{{0.4974, 0.0000, -0.1299, 0.0914, 0.1523},
                {-0.0305, 0.3248, 0.0000, -0.0619, 0.0203},
                {0.0102, -0.0914, 0.5887, 0.0112, 0.0355},
                {0.0305, 0.0000, -0.0741, 0.5887, 0.0000},
                {0.0203, -0.0305, 0.1472, -0.0122, 0.4263}};
        matrix = Matrix.matrixCompositionMatrix(Matrix.transposeMatrix(enteredMatrix), enteredMatrix);
        eigenmatrix = Matrix.copyMatrix(Matrix.IDENTITY_MATRIX);
        eigenvalues = new double[this.length];
        residual = new double[this.length];
    }


    private void findMax() {
        maxElement = Math.abs(matrix[0][1]);
        double tmp = 0;
        iMax = 0;
        jMax = 1;
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                tmp = Math.abs(matrix[i][j]);
                if (tmp > maxElement) {
                    maxElement = tmp;
                    iMax = i;
                    jMax = j;
                }
            }
        }
        tmp = 2 * matrix[iMax][jMax] / (matrix[iMax][iMax] - matrix[jMax][jMax]);
        sin = Math.signum(tmp) * Math.sqrt(0.5 * (1 - 1 / Math.sqrt(1 + tmp * tmp)));
        cos = Math.sqrt(0.5 * (1 + 1 / Math.sqrt(1 + tmp * tmp)));
    }

    private void findOrtogon() {
        ortogon = Matrix.copyMatrix(Matrix.IDENTITY_MATRIX);
        ortogon[iMax][iMax] = cos;
        ortogon[jMax][jMax] = cos;
        ortogon[iMax][jMax] = -sin;
        ortogon[jMax][iMax] = sin;
    }

    private void findEigenmatrix() {
        eigenmatrix = Matrix.matrixCompositionMatrix(eigenmatrix, ortogon);
    }

    private void findEigenvalues() {
        for (int i = 0; i < length; i++) {
            eigenvalues[i] = matrix[i][i];
        }
    }


    public void findSolution(double accuracy) {
        do {
            this.findMax();
            this.findOrtogon();
            this.findEigenmatrix();
            matrix = Matrix.matrixCompositionMatrix(Matrix.transposeMatrix(ortogon), matrix);
            matrix = Matrix.matrixCompositionMatrix(matrix, ortogon);
            iterationAmount++;
        } while (maxElement >= accuracy);
        this.findEigenvalues();
    }

    public void findResidual() {

        for (int i = 0; i < length; i++) {

            residual = Vector.matrixCompositionVector(Matrix.matrixCompositionMatrix(Matrix.transposeMatrix(enteredMatrix), enteredMatrix), Matrix.transposeMatrix(eigenmatrix)[i]);
            residual = Vector.vectorMinusVector(residual, Vector.vectorCompositionNumber(Matrix.transposeMatrix(eigenmatrix)[i], eigenvalues[i]));
            System.out.println(Arrays.toString(residual));
        }
    }

    public double[][] getEigenmatrix() {
        return eigenmatrix;
    }

    public double[] getEigenvalues() {
        return eigenvalues;
    }

    public int getIterationAmount() {
        return iterationAmount;
    }
}
