with open("game.txt") as file:
    line = file.readline()
    # print(line)

    board = []
    for i in range(0, 8):
        line = file.readline()
        line = line.replace("\n", "")
        temp = list(line.split(" "))
        board.append(temp)

    for i in range(0, 8):
        for j in range(0, 8):
            print(board[i][j], end="\t")
        print("")
    