import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RoboticSystem {
	static String[][] matrix;
	static int[] robot = new int[3];
	static int[] target = new int[2];
	static boolean obstacle = false;
	static int[] next = new int[2];
	static ArrayList<ArrayList<Integer>> agent = new ArrayList<ArrayList<Integer>>();

	public static void makeMatrix() throws IOException {
		File file = new File("input.das");
		Scanner input = new Scanner(file);
		ArrayList<ArrayList<String>> arrayMatrix = new ArrayList<ArrayList<String>>();
		String lineStr = "";
		while (input.hasNext()) {
			lineStr = input.nextLine();
			ArrayList<String> lineList = new ArrayList<String>();
			for (int i = 0; i < lineStr.length(); i++)
				lineList.add(Character.toString(lineStr.charAt(i)));
			arrayMatrix.add(lineList);
		}
		matrix = new String[arrayMatrix.size()][arrayMatrix.get(0).size()];
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				matrix[i][j] = arrayMatrix.get(i).get(j);

		boolean flag1 = false, flag2 = false;
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				if (matrix[i][j].equals("ꓥ")) {
					robot[0] = i;
					robot[1] = j;
					robot[2] = 1;
					flag1 = true;
				} else if (matrix[i][j].equals(">")) {
					robot[0] = i;
					robot[1] = j;
					robot[2] = 3;
					flag1 = true;
				} else if (matrix[i][j].equals("ꓦ")) {
					robot[0] = i;
					robot[1] = j;
					robot[2] = 5;
					flag1 = true;
				} else if (matrix[i][j].equals("<")) {
					robot[0] = i;
					robot[1] = j;
					robot[2] = 7;
					flag1 = true;
				} else if (matrix[i][j].equals("A")) {
					target[0] = i;
					target[1] = j;
					flag2 = true;
				} else if (flag1 == true && flag2 == true)
					break;
	}

	public static void main(String[] args) throws IOException {
		makeMatrix();
		move();
		String rowNum = "", colNum = "", direction = "", print = "";
		print += "------------Direction---------------\n";
		print += "      0         1          2\n";
		print += "      7       Robot        3\n";
		print += "      6         5          4\n";
		print += "\nThe sequence of actions performed \nby the agent to collect the target ";
		print += "\n-----------------------------------";
		print += "\nRow number Column number Direction";
		for (int i = 0; i < agent.size(); i++) {
			rowNum = (agent.get(i).get(0) + 1) + "";
			if (rowNum.length() == 1)
				rowNum = " " + rowNum;
			colNum = (agent.get(i).get(1) + 1) + "";
			if (colNum.length() == 1)
				colNum = " " + colNum;
			direction = agent.get(i).get(2) + "";
			print += "\n    " + rowNum + "          " + colNum + "          " + direction;
		}
		System.out.println(print);
		FileWriter output = new FileWriter("accoes.txt");
		output.write(print);
		output.close();
	}

	private static void move() {
		int deep = 70, directionChange = 0;
		boolean right = true;
		ArrayList<Integer> oneMove = new ArrayList<Integer>();
		oneMove.add(robot[0]);
		oneMove.add(robot[1]);
		oneMove.add(robot[2]);
		agent.add(oneMove);
		while (!detectTarget() && deep > 0) {
			if (!obstacle) {
				robot[0] = next[0];
				robot[1] = next[1];
				ArrayList<Integer> oneMove1 = new ArrayList<Integer>();
				oneMove1.add(robot[0]);
				oneMove1.add(robot[1]);
				oneMove1.add(robot[2]);
				agent.add(oneMove1);
			} else {
				if (right)
					while (obstacle) {
						robot[2] = (robot[2] + 1) % 8;
						ArrayList<Integer> oneMove1 = new ArrayList<Integer>();
						oneMove1.add(robot[0]);
						oneMove1.add(robot[1]);
						oneMove1.add(robot[2]);
						agent.add(oneMove1);
						if (isTarget())
							break;
						else
							continue;
					}
				else
					while (obstacle) {
						robot[2] = (robot[2] + 7) % 8;
						ArrayList<Integer> oneMove1 = new ArrayList<Integer>();
						oneMove1.add(robot[0]);
						oneMove1.add(robot[1]);
						oneMove1.add(robot[2]);
						agent.add(oneMove1);
						if (isTarget())
							break;
						else
							continue;
					}
				directionChange++;
				if (directionChange == 4) {
					directionChange = 0;
					if (right)
						right = false;
					else
						right = true;
				}
			}
			deep--;
		}
	}

	private static boolean detectTarget() {
		if (isTarget())
			return true;
		else
			robot[2] = (robot[2] + 1) % 8;
		if (isTarget())
			return true;
		else
			robot[2] = (robot[2] + 7) % 8;
		robot[2] = (robot[2] + 7) % 8;
		if (isTarget())
			return true;
		else
			robot[2] = (robot[2] + 1) % 8;
		isTarget();
		return false;
	}

	private static boolean isTarget() {
		String compare = "";
		int k = 0;
		switch (robot[2]) {
		case 0:
			k = Math.min(robot[0], robot[1]);
			for (int i = 1; i <= k; i++) {
				compare = matrix[robot[0] - i][robot[1] - i];
				if (i == 1) {
					next[0] = robot[0] - i;
					next[1] = robot[1] - i;
				}
				if (compare.equals("O")) {
					if (i == 1)
						obstacle = true;
					else {
						obstacle = false;
					}
					return false;
				} else if (compare.equals("A")) {
					for (int j = 1; j <= i; j++) {
						ArrayList<Integer> oneMove = new ArrayList<Integer>();
						oneMove.add(robot[0] - j);
						oneMove.add(robot[1] - j);
						oneMove.add(robot[2]);
						agent.add(oneMove);
					}
					return true;
				}
			}
			break;
		case 1:
			k = robot[0];
			for (int i = 1; i <= k; i++) {
				compare = matrix[robot[0] - i][robot[1]];
				if (i == 1) {
					next[0] = robot[0] - i;
					next[1] = robot[1];
				}
				if (compare.equals("O")) {
					if (i == 1)
						obstacle = true;
					else
						obstacle = false;
					return false;
				} else if (compare.equals("A")) {
					for (int j = 1; j <= i; j++) {
						ArrayList<Integer> oneMove = new ArrayList<Integer>();
						oneMove.add(robot[0] - j);
						oneMove.add(robot[1]);
						oneMove.add(robot[2]);
						agent.add(oneMove);
					}
					return true;
				}
			}
			break;
		case 2:
			k = Math.min(robot[0], matrix[0].length - 1 - robot[1]);
			for (int i = 1; i <= k; i++) {
				compare = matrix[robot[0] - i][robot[1] + i];
				if (i == 1) {
					next[0] = robot[0] - i;
					next[1] = robot[1] + i;
				}
				if (compare.equals("O")) {
					if (i == 1)
						obstacle = true;
					else
						obstacle = false;
					return false;
				} else if (compare.equals("A")) {
					for (int j = 1; j <= i; j++) {
						ArrayList<Integer> oneMove = new ArrayList<Integer>();
						oneMove.add(robot[0] - j);
						oneMove.add(robot[1] + j);
						oneMove.add(robot[2]);
						agent.add(oneMove);
					}
					return true;
				}
			}
			break;
		case 3:
			k = matrix[0].length - 1 - robot[1];
			for (int i = 1; i <= k; i++) {
				compare = matrix[robot[0]][robot[1] + i];
				if (i == 1) {
					next[0] = robot[0];
					next[1] = robot[1] + i;
				}
				if (compare.equals("O")) {
					if (i == 1)
						obstacle = true;
					else
						obstacle = false;
					return false;
				} else if (compare.equals("A")) {
					for (int j = 1; j <= i; j++) {
						ArrayList<Integer> oneMove = new ArrayList<Integer>();
						oneMove.add(robot[0]);
						oneMove.add(robot[1] + j);
						oneMove.add(robot[2]);
						agent.add(oneMove);
					}
					return true;
				}
			}
			break;
		case 4:
			k = Math.min(matrix.length - 1 - robot[0], matrix[0].length - 1 - robot[1]);
			for (int i = 1; i <= k; i++) {
				compare = matrix[robot[0] + i][robot[1] + i];
				if (i == 1) {
					next[0] = robot[0] + i;
					next[1] = robot[1] + i;
				}
				if (compare.equals("O")) {
					if (i == 1)
						obstacle = true;
					else
						obstacle = false;
					return false;
				} else if (compare.equals("A")) {
					for (int j = 1; j <= i; j++) {
						ArrayList<Integer> oneMove = new ArrayList<Integer>();
						oneMove.add(robot[0] + j);
						oneMove.add(robot[1] + j);
						oneMove.add(robot[2]);
						agent.add(oneMove);
					}
					return true;
				}
			}
			break;
		case 5:
			k = matrix.length - 1 - robot[0];
			for (int i = 1; i <= k; i++) {
				compare = matrix[robot[0] + i][robot[1]];
				if (i == 1) {
					next[0] = robot[0] + i;
					next[1] = robot[1];
				}
				if (compare.equals("O")) {
					if (i == 1)
						obstacle = true;
					else
						obstacle = false;
					return false;
				} else if (compare.equals("A")) {
					for (int j = 1; j <= i; j++) {
						ArrayList<Integer> oneMove = new ArrayList<Integer>();
						oneMove.add(robot[0] + j);
						oneMove.add(robot[1]);
						oneMove.add(robot[2]);
						agent.add(oneMove);
					}
					return true;
				}
			}
			break;
		case 6:
			k = Math.min(matrix.length - 1 - robot[0], robot[1]);
			for (int i = 1; i <= k; i++) {
				compare = matrix[robot[0] + i][robot[1] - i];
				if (i == 1) {
					next[0] = robot[0] + i;
					next[1] = robot[1] - i;
				}
				if (compare.equals("O")) {
					if (i == 1)
						obstacle = true;
					else
						obstacle = false;
					return false;
				} else if (compare.equals("A")) {
					for (int j = 1; j <= i; j++) {
						ArrayList<Integer> oneMove = new ArrayList<Integer>();
						oneMove.add(robot[0] + j);
						oneMove.add(robot[1] - j);
						oneMove.add(robot[2]);
						agent.add(oneMove);
					}
					return true;
				}
			}
			break;
		case 7:
			k = robot[1];
			for (int i = 1; i <= k; i++) {
				compare = matrix[robot[0]][robot[1] - i];
				if (i == 1) {
					next[0] = robot[0];
					next[1] = robot[1] - i;
				}
				if (compare.equals("O")) {
					if (i == 1)
						obstacle = true;
					else
						obstacle = false;
					return false;
				} else if (compare.equals("A")) {
					for (int j = 1; j <= i; j++) {
						ArrayList<Integer> oneMove = new ArrayList<Integer>();
						oneMove.add(robot[0]);
						oneMove.add(robot[1] - j);
						oneMove.add(robot[2]);
						agent.add(oneMove);
					}
					return true;
				}
			}
			break;
		default:
			break;
		}
		return false;
	}
}