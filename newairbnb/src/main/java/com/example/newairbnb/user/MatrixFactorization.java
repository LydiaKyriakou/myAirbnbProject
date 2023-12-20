package com.example.newairbnb.user;

import com.example.newairbnb.service.RentalService;
import com.example.newairbnb.service.ReviewService;
import com.example.newairbnb.service.UserService;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Random;



public class MatrixFactorization {
//    private  final UserService userService ;
//    private  final RentalService rentalService ;
//    private  final ReviewService reviewService ;

    private Matrix nPQ;

    public static Matrix matrixFactorizationCalc(Matrix R, Matrix P, Matrix Q, int K, int steps, double alpha, double beta) {

        int N = R.rows();
        int M = R.columns(), count_non_zero = 0;
        double error_prev=0, total_error2 = 0,  error = -1;
        for (int step = 0; step < steps; step++) {

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    if (R.get(i, j) > 0) {

                        Vector columnVector = Q.getColumn(j);
                        Vector rowVector = P.getRow(i);

                        double innerProductResult = columnVector.innerProduct(rowVector);
                        double eij = R.get(i, j) - innerProductResult;
                        count_non_zero += 1;

                        for (int k = 0; k < K; k++) {
                            P.set(i, k, P.get(i, k) + alpha * 2 * eij * Q.get(k, j));
                            Q.set(k, j, Q.get(k, j) + alpha * 2 * eij * P.get(i, k));
                            total_error2 += Math.pow(eij, 2);
                        }
                    }
                }
            }

            error_prev = error;
            error = Math.sqrt(total_error2/count_non_zero);
            if(Math.abs(error_prev - error) < 0.001){
                break;
            }

        }



        return P.multiply(Q);
    }


    public MatrixFactorization() {
    }


    public static Matrix run(Matrix R) {
//        double[][] R = {
//                {5, 3, 0, 1},
//                {4, 0, 0, 1},
//                {1, 1, 0, 5},
//                {1, 0, 0, 4},
//                {0, 1, 5, 4}
//        };

        int N = R.rows();
        int M = R.columns();
        int K = 5;

        Random rand = new Random();
        Matrix P =  Basic2DMatrix.random(N, K,rand);
        Matrix Q =  Basic2DMatrix.random(K, M, rand);
//
//        System.out.println("P" + P.toString());
//        System.out.println("Q" + Q.toString());

        Matrix nPQ = matrixFactorizationCalc(R, P, Q, K, 5000, 0.0002, 0.2);
        System.out.println( nPQ.toString());

        return nPQ;
    }



}

