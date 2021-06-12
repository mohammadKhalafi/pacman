package sample.controller;

import sample.models.Pair;

import java.util.ArrayList;
import java.util.Collections;

import static sample.view.print;

public class randBoard {

    static int numRows = 17;
    static int numColumns = 23;


    public static int tempArray[][] = new int[numRows + 1][numColumns + 1];
    public static int counter = 1;
    public static int array[][] = new int[numRows + 1][numColumns + 1];
    public static int column = numColumns;
    public static int row = numRows;


    public static void createArray() {

        for (int i = 0; i < numRows + 1; i++) {
            for (int j = 0; j < numColumns + 1; j++) {
                array[i][j] = 1;
                tempArray[i][j] = 0;
            }
        }

        for (int i = 1; i < numColumns + 1; i++) {
            array[1][i] = -1;
        }

        for (int i = 1; i < numColumns + 1; i++) {
            array[numRows][i] = -1;
        }

        for (int i = 1; i < numRows + 1; i++) {
            array[i][(numColumns + 1) / 2] = -1;
        }

        for (int i = 1; i < numRows + 1; i++) {
            array[i][(numColumns + 1) * 3 / 4] = -1;
        }

        for (int i = 1; i < numRows + 1; i++) {
            array[i][(numColumns + 1) / 4] = -1;
        }

        array[(numColumns + 1) / 2][(numRows + 1) / 2] = 0;

        tempArray[(numColumns + 1) / 2][(numRows + 1) / 2] = counter;
        counter++;

        dfs((numColumns + 1) / 2, (numRows + 1) / 2);

        for (int i = 0; i < numRows + 1; i++) {
            for (int j = 0; j < numColumns + 1; j++) {
                if (array[i][j] == -1) {
                    array[i][j] = 0;
                }
                System.out.print(array[i][j]);
            }
            print("");
        }
        print();
        print("");

        for (int i = 0; i < numRows + 1; i++) {
            for (int j = 0; j < numColumns + 1; j++) {
                System.out.printf("%4d", tempArray[i][j]);
            }
            print("");
        }


        print("end");

    }


    public static void dfs(int i, int j) {

        ArrayList<Integer> nears = getNears(i, j);

        for (int k = 0; k < nears.size(); k += 2) {

            int I = nears.get(k);
            int J = nears.get(k + 1);

            if (array[I][J] == 0) {
                continue;
            }

            if (array[I][J] == -1) {
                array[I][J] = 0;
                tempArray[I][J] = counter++;
                dfs(I, J);
                continue;
            }
            if (array[I][J] == 2) {
                continue;
            }

            // 1
            boolean bool = true;

            ArrayList<Integer> nears1 = getNears(I, J);
            for (int t = 0; t < nears1.size(); t += 2) {

                int ii = nears1.get(t);
                int jj = nears1.get(t + 1);

                if (array[ii][jj] == 0 && !(ii == i && jj == j)) {
                    bool = false;
                } else if (array[ii][jj] == -1) {

                    ArrayList<Integer> nears2 = getNears(ii, jj);
                    for (int l = 0; l < nears2.size(); l += 2) {

                        int iii = nears2.get(l);
                        int jjj = nears2.get(l + 1);

                        if (array[iii][jjj] == 0) {
                            bool = false;
                        }
                    }

                }
            }

            if (bool) {
                array[I][J] = 0;
                tempArray[I][J] = counter++;
                dfs(I, J);
            }
        }
    }


    public static ArrayList<Integer> getNears(int i, int j) {
        ArrayList<Integer> ans = new ArrayList<>();

        boolean shouldAdd1 = true;
        boolean shouldAdd2 = true;
        boolean shouldAdd3 = true;
        boolean shouldAdd4 = true;

        if (i == 1 && j == 1) {
            shouldAdd3 = false;
            shouldAdd4 = false;
        } else if (i == row && j == column) {
            shouldAdd1 = false;
            shouldAdd2 = false;
        } else if (i == 1 && j == column) {
            shouldAdd1 = false;
            shouldAdd3 = false;
        } else if (i == row && j == 1) {
            shouldAdd2 = false;
            shouldAdd4 = false;
        } else if (i == 1) {
            shouldAdd3 = false;
        } else if (i == row) {
            shouldAdd2 = false;
        } else if (j == 1) {
            shouldAdd4 = false;
        } else if (j == column) {
            shouldAdd1 = false;
        }


        ArrayList<Pair> pairs = new ArrayList<>();

        if (shouldAdd1) {
            pairs.add(new Pair(i, j + 1));
        }

        if (shouldAdd2) {
            pairs.add(new Pair(i + 1, j));
        }

        if (shouldAdd3) {
            pairs.add(new Pair(i - 1, j));
        }

        if (shouldAdd4) {
            pairs.add(new Pair(i, j - 1));
        }

        for (Pair pair : (ArrayList<Pair>) pairs.clone()) {
            int i0 = (int) pair.i;
            int j0 = (int) pair.j;
            if (array[i0][j0] == -1) {
                ans.add(i0);
                ans.add(j0);
                pairs.remove(pair);
            }
        }

        Collections.shuffle(pairs);

        for (Pair pair : pairs) {
            int i0 = (int) pair.i;
            int j0 = (int) pair.j;
            ans.add(i0);
            ans.add(j0);
        }

        return ans;
    }

}
