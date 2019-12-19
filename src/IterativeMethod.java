import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class IterativeMethod {
    private double[][] matrix;
    private double[][] symmetricMatrix;
    private double[] residual;
    private double[] eigenvector;
    private double[] prevEigenvector;
    private double eigenvalue;
    private double prevEigenvalue;
    private int iterationAmount;
    private int length;

    public IterativeMethod() {
        this(5);
    }

    public IterativeMethod(int length) {
        this.length = length;
        this.matrix = new double[][]{{0.4974, 0.0000, -0.1299, 0.0914, 0.1523},
                {-0.0305, 0.3248, 0.0000, -0.0619, 0.0203},
                {0.0102, -0.0914, 0.5887, 0.0112, 0.0355},
                {0.0305, 0.0000, -0.0741, 0.5887, 0.0000},
                {0.0203, -0.0305, 0.1472, -0.0122, 0.4263}};
        this.symmetricMatrix = new double[this.length][this.length];
        this.residual = new double[this.length];
        this.eigenvector = new double[this.length];
        this.prevEigenvector = new double[this.length];
        this.eigenvalue = 0;
        this.prevEigenvalue = 0;
        this.iterationAmount = 0;
    }

    public IterativeMethod(double[][] matrix) {
        this.length = matrix.length;
        this.matrix = new double[this.length][this.length];
        this.symmetricMatrix = new double[this.length][this.length];
        this.residual = new double[this.length];
        this.eigenvector = new double[this.length];
        this.prevEigenvector = new double[this.length];
        this.eigenvalue = 0;
        this.prevEigenvalue = 0;
        this.iterationAmount = 0;
    }

    public void inputMatrix(String path) throws IOException, ArrayIndexOutOfBoundsException {
        symmetricMatrix = Matrix.matrixCompositionMatrix(Matrix.transposeMatrix(matrix), matrix);
    }

    public void outputResidual() throws ArrayIndexOutOfBoundsException {
        System.out.print("(");
        for (int i = 0; i < length - 1; i++) {
            System.out.printf("%3.1e; ", residual[i]);
        }
        System.out.printf("%3.1e", residual[length - 1]);
        System.out.println(")");
    }

    private void findResidual() throws ArrayIndexOutOfBoundsException {
        residual = Vector.matrixCompositionVector(symmetricMatrix, eigenvector);
        residual = Vector.vectorMinusVector(residual, Vector.vectorCompositionNumber(eigenvector, eigenvalue));
    }

    private void initialValueForEigenvector() throws ArrayIndexOutOfBoundsException {
        for (int i = 0; i < eigenvector.length; i++) {
            eigenvector[i] = 1;
        }
    }

    private void findEigenvector() throws ArrayIndexOutOfBoundsException {
        prevEigenvector = Vector.copyVector(eigenvector);
        eigenvector = Vector.matrixCompositionVector(symmetricMatrix, eigenvector);
    }

    private void findEigenvalue() throws ArrayIndexOutOfBoundsException {
        prevEigenvalue = eigenvalue;
        eigenvalue = eigenvector[0] / prevEigenvector[0];
    }

    public void findSolution(double accuracy) throws ArrayIndexOutOfBoundsException {
        this.initialValueForEigenvector();
        do {
            this.findEigenvector();
            Vector.outputVector(eigenvector);
            this.findEigenvalue();
            System.out.println(eigenvalue);
            iterationAmount++;
        } while (Math.abs(eigenvalue - prevEigenvalue) >= accuracy);
        eigenvector = Vector.orthonormalize(eigenvector);
        this.findResidual();
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public double[][] getSymmetricMatrix() {
        return symmetricMatrix;
    }

    public double[] getResidual() {
        return residual;
    }

    public double[] getEigenvector() {
        return eigenvector;
    }

    public double getEigenvalue() {
        return eigenvalue;
    }

    public int getLength() {
        return length;
    }

    public int getIterationAmount() {
        return iterationAmount;
    }

}