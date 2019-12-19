public class Main {
    public static void main(String[] args) {

        Revolve revolve = new Revolve();
        revolve.findSolution(Math.pow(10, -5));
        System.out.println("Each column is a self vector:");
        Matrix.outputMatrix(revolve.getEigenmatrix());
        Vector.outputVector(revolve.getEigenvalues());
        System.out.println();
        revolve.findResidual();
        System.out.println();
        System.out.println("Number of iterations: " + revolve.getIterationAmount());

    }
}