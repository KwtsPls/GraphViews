package main.java.gr.uoa.di.entities.cypherViews;
import org.apache.jena.base.Sys;

import java.util.ArrayList;
import java.util.List;

public class KnapsackSolver {

    List<KnapsackItem> Result;

    public KnapsackSolver(){
        Result = new ArrayList<KnapsackItem>();
    }

    public List<KnapsackItem> solve(List<KnapsackItem> Items,int b,int n){

        int i,w;
        int[] wt = new int[n];
        double[] val = new double[n];
        int c=0;
        for(KnapsackItem item:Items){
            wt[c] = item.getCost();
            val[c] = item.getBenefit();
            c++;
        }

        double[][] K = new double[n+1][b+1];

        // Build table K[][] in bottom up manner
        for (i = 0; i <= n; i++) {
            for (w = 0; w <= b; w++) {
                if (i == 0 || w == 0)
                    K[i][w] = 0;
                else if (wt[i - 1] <= w)
                    K[i][w] = Math.max(val[i - 1] +
                            K[i - 1][w - wt[i - 1]], K[i - 1][w]);
                else
                    K[i][w] = K[i - 1][w];
            }
        }


        double res = K[n][b];
        System.out.println(res + " " + n);


        List<KnapsackItem> results = new ArrayList<>();
        w = b;
        for (i = n; i > 0 && res > 0 && w>0; i--) {

            // either the result comes from the top
            // (K[i-1][w]) or from (val[i-1] + K[i-1]
            // [w-wt[i-1]]) as in Knapsack table. If
            // it comes from the latter one/ it means
            // the item is included.
            if (res != K[i - 1][w]){

                // This item is included.
                results.add(Items.get(i-1));

                // Since this weight is included its
                // value is deducted
                res = res - val[i-1];
                System.out.println(w+" "+wt[i-1]);
                w = w - wt[i - 1];
            }
        }

        return results;
    }

}
