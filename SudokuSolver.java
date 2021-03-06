package sudoku;
import java.util.ArrayList;
/**
 * Place for your code.
 */
public class SudokuSolver {

	ArrayList<ArrayList<Integer>> variables;
	/**
	 * @return names of the authors and their student IDs (1 per line).
	 */
	public String authors() {
		// TODO write it;
		return "David Russo\nZoe Liang";
	}

	/**
	 * Performs constraint satisfaction on the given Sudoku board using Arc Consistency and Domain Splitting.
	 * 
	 * @param board the 2d int array representing the Sudoku board. Zeros indicate unfilled cells.
	 * @return the solved Sudoku board
	 */
	public int[][] solve(int[][] board) {
		variables = getVariables(board);
		degreeMRV(board);
		return board;
	}

	//Basic backtracking solver
	public boolean simpleDFS(int[][] board) {
		int move[] = nextVar(board);
		if (move[0] == -1)
			return true;

		int curVal = 0;

		int row = move[0];
		int col = move[1];

		while (nextVal(curVal) != -1) {
			curVal = nextVal(curVal);
			if (checkValid(board, row, col, curVal)) {
				board[row][col] = curVal;
				if (simpleDFS(board))
					return true;
				board[row][col] = 0;
			}
		}
		return false;
	}
	//backtracking solver using the MRV and Degree heuristics
	public boolean degreeMRV(int[][] board) {
		int move[] = getVar(board);
		if (move[0] == -1)
			return true;

		int curVal = 0;

		int row = move[0];
		int col = move[1];

		while (nextVal(curVal) != -1) {
			curVal = nextVal(curVal);
			if (checkValid(board, row, col, curVal)) {
				board[row][col] = curVal;
				variables = getVariables(board);
				if (degreeMRV(board))
					return true;
				board[row][col] = 0;
			}
		}
		return false;
	}

	//Function to check if num is valid in that location
	public boolean checkValid(int[][] board, int row, int col, int num) {

		//Check if row is valid
		for (int i = 0; i < board.length; i++) {
			if (board[i][col] == num) {
				return false;
			}
		}

		//Check if col is valid
		for (int i = 0; i < board.length; i++) {
			if (board[row][i] == num) {
				return false;
			}
		}

		//Math to get location of box
		int sqrt = (int)Math.sqrt(board.length);
		int boxRow = row - row % sqrt;
		int boxCol = col - col % sqrt;

		for (int r = boxRow; r < boxRow + sqrt; r++) {
			for (int d = boxCol; d < boxCol + sqrt; d++) {
				if (board[r][d] == num) {
					return false;
				}
			}
		}

		return true;
	}

	//Simple function to get the next variable, iteratively.
	public int[] nextVar(int[][] board) {
		int[] pos = new int[2];
		pos[0] = -1;
		pos[1] = -1;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == 0) {
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
		if (curVal == 9)
				return -1;
		else
			return ++curVal;
	}

	//Function to build a list of variables and their domains for use in heuristic calulation
	public ArrayList<ArrayList<Integer>> getVariables(int[][] board) {
		ArrayList<ArrayList<Integer>> domains = new ArrayList<>();
		ArrayList<Integer> fixed = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			fixed.add(0);
		}
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == 0) {
					domains.add(fillDomain(board, i, j));
				} else {
					domains.add(fixed);
				}
			}
		}
		return domains;
	}

	//Helper function to fill in the domain for the variables
	public ArrayList<Integer> fillDomain(int[][] board, int row, int col) {
		ArrayList<Integer> domain = new ArrayList<>();
		for (int i = 1; i <= board.length; i++) {
			if (checkValid(board, row, col, i)) {
				domain.add(i);
			}
		}
		return domain;
	}

	//Gets length of domain lists, for use in MRV
	public ArrayList<Integer> getLength() {
		ArrayList<Integer> domainLength = new ArrayList<>();
		for (ArrayList<Integer> lst : variables) {
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
					if (degree < getDegree(board, i / 9, i % 9)) {
						pos[0] = i / 9;
						pos[1] = i % 9;
						degree = getDegree(board, pos[0], pos[1]);
					}
				}
				else {
					min = lengths.get(i);
					pos[0] = i / 9;
					pos[1] = i % 9;
					degree = getDegree(board, pos[0], pos[1]);
				}
			}
		}
		return pos;
	}

	//Helper function to calculate the degree of a variable
	public int getDegree(int[][] board, int row, int col) {
		int degree = 0;
		for (int i = 0; i < board.length; i++) {
			if (board[i][col] == 0) {
				degree++;
			}
		}

		for (int i = 0; i < board.length; i++) {
			if (board[row][i] == 0) {
				degree++;
			}
		}

		int sqrt = (int)Math.sqrt(board.length);
		int boxRow = row - row % sqrt;
		int boxCol = col - col % sqrt;

		for (int r = boxRow; r < boxRow + sqrt; r++) {
			for (int d = boxCol; d < boxCol + sqrt; d++) {
				if (board[r][d] == 0) {
					degree++;
				}
			}
		}
		return degree;
	}
}


