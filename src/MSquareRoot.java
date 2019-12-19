import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MSquareRoot {
    private double[][] matrix;
    private double[] vectorB;
    private double[] answers;
    private byte rows;
    private byte cols;
    private double determinant;
    private double[] convertedVectorB;
    private double[][] symmetricMatrix;
    private double[][] L;
    private double[][] U;

    public MSquareRoot() {
        rows = 5;
        cols = 5;
        determinant = 1;
        matrix = new double[rows][cols];
        vectorB = new double[rows];
        answers = new double[rows];
        convertedVectorB = new double[rows];
        symmetricMatrix = new double[rows][cols];
        L = new double[rows][cols];
        U = new double[rows][cols];
    }

    public MSquareRoot(double[][] matrix, double[] vectorB) {
        rows = 5;
        cols = 5;
        determinant = 1;
        this.matrix = matrix;
        this.vectorB = vectorB;
        answers = new double[rows];
        convertedVectorB = new double[rows];
        symmetricMatrix = new double[rows][cols];
        L = new double[rows][cols];
        U = new double[rows][cols];
    }

    public MSquareRoot(double[][] sharedMatrix) {
        rows = 5;
        cols = 5;
        determinant = 1;
        matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.matrix[i][j] = sharedMatrix[i][j];
            }
        }
        vectorB = new double[rows];
        for (int i = 0; i < rows; i++) {
            this.vectorB[i] = sharedMatrix[i][sharedMatrix[0].length - 1];
        }
        answers = new double[rows];
        convertedVectorB = new double[rows];
        symmetricMatrix = new double[rows][cols];
        L = new double[rows][cols];
        U = new double[rows][cols];
    }

    public void inputMatrix(String inPath) throws IOException, ArrayIndexOutOfBoundsException {
        Scanner sc = new Scanner(new File(inPath));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = sc.nextDouble();
            }
            vectorB[i] = sc.nextDouble();
        }
        sc.close();
    }

    public void outputMatrix() throws ArrayIndexOutOfBoundsException {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%7.4f ", matrix[i][j]);
            }
            System.out.println();
        }
    }

    public void outputSymmetricMatrix() throws ArrayIndexOutOfBoundsException {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%7.4f ", symmetricMatrix[i][j]);
            }
            System.out.println();
        }
    }

    public void outputVectorB() throws ArrayIndexOutOfBoundsException {
        for (int i = 0; i < vectorB.length; i++) {
            System.out.printf("%7.4f%n", vectorB[i]);
        }
    }

    public void outputConvertedVectorB() throws ArrayIndexOutOfBoundsException {
        for (int i = 0; i < convertedVectorB.length; i++) {
            System.out.printf("%7.4f%n", convertedVectorB[i]);
        }
    }

    public void outputAnswers() throws ArrayIndexOutOfBoundsException {
        for (int i = 0; i < answers.length; i++) {
            System.out.printf("%7.4f%n", answers[i]);
        }
    }

    public void outputL() throws ArrayIndexOutOfBoundsException {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%7.4f ", L[i][j]);
            }
            System.out.println();
        }
    }

    public void outputU() throws ArrayIndexOutOfBoundsException {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%7.4f ", U[i][j]);
            }
            System.out.println();
        }
    }

    public void outputResidualVector() throws ArrayIndexOutOfBoundsException {
        System.out.println("Р’РµРєС‚РѕСЂ РЅРµРІСЏР·РєРё:");
        double[] res = Vector.vectorMinusVector(vectorB, Vector.matrixCompositionVector(matrix, answers));
        double max = Math.abs(res[0]);
        int index = 0;
        for (int i = 0; i < res.length; i++) {
            System.out.println(res[i]);
            if (Math.abs(res[i]) > max) {
                index = i;
                max = Math.abs(res[i]);
            }
        }
        System.out.println("РќРѕСЂРјР° РІРµРєС‚РѕСЂР° РЅРµРІСЏР·РєРё СЂР°РІРЅР°: " + Math.abs(res[index]));
    }

    public void outputDeterminant() throws ArrayIndexOutOfBoundsException {
        System.out.printf("РћРїСЂРµРґРµР»РёС‚РµР»СЊ РјР°С‚СЂРёС†С‹ = %7.4f%n", getDeterminant());
    }

    public void findSymmetric() throws ArrayIndexOutOfBoundsException {
        double[][] transposedMatrix = Matrix.transposeMatrix(matrix);
        symmetricMatrix = Matrix.matrixCompositionMatrix(transposedMatrix, matrix);
        convertedVectorB = Vector.matrixCompositionVector(transposedMatrix, vectorB);
    }

    public void findLU() throws ArrayIndexOutOfBoundsException {
        /*Searching for U*/
        double sum = 0;
        for (int i = 0; i < symmetricMatrix.length; i++) {
            for (int k = 0; k < i; k++) {
                sum += U[k][i] * U[k][i];
            }
            U[i][i] = Math.sqrt(symmetricMatrix[i][i] - sum);
            for (int j = i + 1; j < symmetricMatrix.length; j++) {
                sum = 0;
                for (int k = 0; k < i; k++) {
                    sum += U[k][i] * U[k][j];
                }
                U[i][j] = (symmetricMatrix[i][j] - sum) / U[i][i];
            }
            sum = 0;
        }
        /*Searching for L*/
        L = Matrix.transposeMatrix(U);
        /*Determinant*/
        for (int i = 0; i < L.length; i++) {
            determinant *= L[i][i];
        }
    }

    public void findSolution() throws ArrayIndexOutOfBoundsException {
        /*Preparation to fight*/
        this.findSymmetric();
        this.findLU();
        double[] vectorY = {0, 0, 0, 0, 0};
        /*Straight course. Solving system L*VectorY=convertedVectorB*/
        double sum = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < i; j++) {
                sum += vectorY[j] * L[i][j];
            }
            vectorY[i] = (convertedVectorB[i] - sum) / L[i][i];
            sum = 0;
        }
        /*Reverse course. Solving system U*answers=VectorY*/
        for (int i = rows - 1; i >= 0; i--) {
            for (int j = rows - 1; j >= i + 1; j--) {
                sum += answers[j] * U[i][j];
            }
            answers[i] = (vectorY[i] - sum) / U[i][i];
            sum = 0;
        }
    }

    public byte getRows() {
        return rows;
    }

    public byte getCols() {
        return cols;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public double[] getVectorB() {
        return vectorB;
    }

    public double[] getAnswers() {
        return answers;
    }

    public double[] getConvertedVectorB() {
        return convertedVectorB;
    }

    public double[][] getSymmetricMatrix() {
        return symmetricMatrix;
    }

    public double[][] getL() {
        return L;
    }

    public double[][] getU() {
        return U;
    }

    public double getDeterminant() {
        return determinant;
    }
}