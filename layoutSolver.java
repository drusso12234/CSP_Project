import java.util.ArrayList;
/**
 * Place for your code.
 */
public class layoutSolver {

    public static void main(String[] argv) {
        String[][] board = [['C', '', ''], ['', '', 'L'], ['', '', '']];
        System.out.println(solve(board));
    }

    ArrayList<ArrayList<String>> variables;
    String[] vals = ['R', 'h', 'H', 'G'];


    /**
     * @return names of the authors and their student IDs (1 per line).
     */
    public String authors() {
        return "David Russo\nZoe Liang";
    }

    /**
     * Performs constraint satisfaction on the given Sudoku board using Arc Consistency and Domain Splitting.
     *
     * @param board the 2d int array representing the Sudoku board. Zeros indicate unfilled cells.
     * @return the solved Sudoku board
     */
    public int[][] solve(String[][] board) {
        simpleDFS(board);
        return board;
    }

    //Basic backtracking solver
    public boolean simpleDFS(String[][] board) {
        int move[] = nextVar(board);
        if (checkLayout(board))
            return true;

        int curVal = 0;

        int row = move[0];
        int col = move[1];

        while (spaceCounter(board)) {
            curVal = nextVal(curVal);
            if (checkValid(board, row, col, curVal)) {
                board[row][col] = curVal;
                if (simpleDFS(board))
                    return true;
                board[row][col] = '';
            }
        }
        return false;
    }
    //backtracking solver using the MRV and Degree heuristics
    public boolean degreeMRV(int[][] board) {
        int move[] = getVar(board);
        if (checkLayout(board))
            return true;

        int curVal = 0;

        int row = move[0];
        int col = move[1];

        while (spaceCounter(board)) {
            curVal = getValFromDomain(row, col, curVal);
            if (checkValid(board, row, col, curVal)) {
                board[row][col] = curVal;
                variables = getVariables(board);
                if (degreeMRV(board))
                    return true;
                board[row][col] = '';
            }
        }
        return false;
    }

    public boolean spaceCounter(String[][] board) {
        counter = 0;
        for (String[] row : board) {
            for (String val : row) {
                if (val.length == 2) {
                    counter++
                }
            }
        }
        if (counter > 3)
            return false;
        else
            return true;
    }

    public boolean checkLayout(String[][] board) {
        counter = 0;
        for (String[] row : board) {
            for (String val : row) {
                if (val.length == 1) {
                    counter++;
                }
            }
        }
        if (counter == 6) {
            return true;
        }
        else {
            return false;
        }
    }

    //Function to check if num is valid in that location
    public boolean checkValid(String[][] board, int row, int col, String val) {
        if (board[row][col].length != 0) {
            return false;
        }
        if (val == '  ')
            return true;
        if (val == 'G') {
            if (checkClose(board, row, col, 'h') || checkClose(board, row, col, 'H'))
                return false;
        }
        if (val == 'h') {
            if (checkClose(board, row, col, 'C') || !checkClose(board, row, col, 'R') || checkClose(board, row, col, 'G'))
                return false;
        }
        if (val == 'H') {
            if (checkClose(board, row, col, 'C') || !checkClose(board, row, col, 'R') || checkClose(board, row, col, 'G'))
                return false;
        }
        if (val == 'R') {
            if (!checkClose(board, row, col, 'L'))
                return false;
        }
        return true;
    }

    public boolean checkClose(String[][] board, int row, int col, String val) {
        if (row == 0) {
            if (col == 0) {
                if (board[row + 1][col] == val || board[row][col + 1] == val)
                    return true;
            }
            if (col == 1) {
                if (board[row + 1][col] == val || board[row][col + 1] == val || board[row][col - 1] == val)
                    return true;
            }
            if (col == 2) {
                if (board[row + 1][col] == val || board[row][col - 1] == val)
                    return true;
            }
        }
        if (row == 1) {
            if (col == 0) {
                if (board[row+1][col] == val || board[row-1][col] == val || board[row][col+1] == val)
                    return true;
            }
            if (col == 1) {
                if (board[row+1][col] == val || board[row][col+1] == val || board[row][col-1] == val || board[row-1][col] == val)
                    return true;
            }
            if (col == 2) {
                if (board[row + 1][col] == val || board[row-1][col] == val || board[row][col - 1] == val)
                    return true;
            }
        }
        if (row == 2) {
            if (col == 0) {
                if (board[row - 1][col] == val || board[row][col + 1] == val)
                    return true;
            }
            if (col == 1) {
                if (board[row - 1][col] == val || board[row][col + 1] == val || board[row][col - 1] == val)
                    return true;
            }
            if (col == 2) {
                if (board[row - 1][col] == val || board[row][col - 1] == val)
                    return true;
            }
        }
        return false;
    }

    //Simple function to get the next variable, iteratively.
    public int[] nextVar(int[][] board) {
        int[] pos = new int[2];
        pos[0] = -1;
        pos[1] = -1;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == '') {
                    pos[0] = i;
                    pos[1] = j;
                    return pos;
                }
            }
        }
        return pos;
    }

    //Simple function to get the next val to try for a variable.
    public int nextVal(int curVal) {
        if (curVal == 'G')
            return '  ';
        if (curVal == 'R')
            return 'H';
        if (curVal == 'H')
            return 'h';
        if (curVal == 'h')
            return 'G';
        if (curVal == '')
            return 'R';
    }

    //Function to build a list of variables and their domains for use in heuristic calulation
    public ArrayList<ArrayList<String>> getVariables(String[][] board) {
        ArrayList<ArrayList<String>> domains = new ArrayList<>();
        ArrayList<String> fixed = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            fixed.add('  ');
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == '') {
                    domains.add(fillDomain(board, i, j));
                } else {
                    domains.add(fixed);
                }
            }
        }
        return domains;
    }

    public boolean checkDomain(String[][] board, int row, int col, String val) {
        if (board[row][col].length != 0) {
            return false;
        }
        if (val == 'G') {
            if (checkClose(board, row, col, 'h') || checkClose(board, row, col, 'H'))
                return false;
        }
        if (val == 'h') {
            if (checkClose(board, row, col, 'C') || checkClose(board, row, col, 'G'))
                return false;
        }
        if (val == 'H') {
            if (checkClose(board, row, col, 'C') || checkClose(board, row, col, 'G'))
                return false;
        }
        if (val == 'R') {
            if (!checkClose(board, row, col, 'L'))
                return false;
        }
        return true;
    }

    //Helper function to fill in the domain for the variables
    public ArrayList<String> fillDomain(String[][] board, int row, int col) {
        ArrayList<String> domain = new ArrayList<>();
        ArrayList<String> valid = new ArrayList<>();
        ArrayList<String> used = new ArrayList<>();
        for (String[] arr : board) {
            for (String var : arr) {
                if (var.length == 1) {
                    used.add(var);
                }
            }
        }

        for (String val : vals) {
            if (!used.contains(val))
                valid.add(val);
        }

        for (String val : valid) {
            if (checkDomain(board, row, col, val)) {
                domain.add(val);
            }
        }
        return domain;
    }

    public String getValFromDomain(int row, int col, String curVal) {
        index = (row * 3) + col;
        ArrayList<String> valLst = variables.get(index);
        if (curVal == '')
            return valLst.get(0);
        if (curVal == valLst.get(valLst.size() - 1))
            return '  ';
        for (int i = 0; i < valLst.size(); i++) {
            if (valLst.get(i) == curVal)
                return valLst.get(i + 1);
        }
        return '  ';
    }

    //Gets length of domain lists, for use in MRV
    public ArrayList<Integer> getLength() {
        ArrayList<Integer> domainLength = new ArrayList<>();
        for (ArrayList<String> lst : variables) {
            domainLength.add(lst.size());
        }
        return domainLength;
    }

    //Function to get the next variable, using degree and MRV heuristics
    public int[] getVar(int[][] board) {
        int[] pos = new int[2];
        pos[0] = -1;
        pos[1] = -1;

        ArrayList<Integer> lengths = getLength();

        int min = 10;
        int degree = 0;
        for (int i = 0; i < lengths.size(); i++) {
            if (lengths.get(i) <= min) {
                if (lengths.get(i) == min) {
                    if (degree < getDegree(board, i / 3, i % 3)) {
                        pos[0] = i / 3;
                        pos[1] = i % 3;
                        degree = getDegree(board, pos[0], pos[1]);
                    }
                }
                else {
                    min = lengths.get(i);
                    pos[0] = i / 3;
                    pos[1] = i % 3;
                    degree = getDegree(board, pos[0], pos[1]);
                }
            }
        }
        return pos;
    }

    //Helper function to calculate the degree of a variable

}