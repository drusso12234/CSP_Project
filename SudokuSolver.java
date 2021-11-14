package sudoku;

/**
 * Place for your code.
 */
public class SudokuSolver {

	/**
	 * @return names of the authors and their student IDs (1 per line).
	 */
	public String authors() {
		// TODO write it;
		return "NAMES OF THE AUTHORS AND THEIR STUDENT IDs (1 PER LINE)";
	}

	/**
	 * Performs constraint satisfaction on the given Sudoku board using Arc Consistency and Domain Splitting.
	 * 
	 * @param board the 2d int array representing the Sudoku board. Zeros indicate unfilled cells.
	 * @return the solved Sudoku board
	 */
	public int[][] solve(int[][] board) {

		return board;
	}

	public int[][] simpleDFS(int[][] board) {
	int move[] = nextVar(0, 0, board);
	if (move[0] == -1)
		return board;
	int curVal = 0;

	while (nextVal(curVal) != -1) {
		curVal = nextVal(curVal);
		if (checkValid(board, move[0], move[1], curVal)) {
			board[move[0]][move[1]] = curVal;
		}
	}

	}

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

	public int[] nextVar(int row, int col, int[][] board) {
		for (int i = row; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == 0) {
					return [i, j];
				}
			}
		}
		return [-1, -1];
	}

	public int nextVal(int curVal) {
		if curVal == 0
				return 1;
		if curVal == 9
				return -1;
		else
			return curVal++;
	}
}


