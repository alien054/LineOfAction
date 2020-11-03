import pygame
import time

pygame.init()

# Constants
filePath = "game.txt"

display_width = 480
display_height = 580

board_dim = 8
board_width = display_width
board_height = display_height - 100

square_size = board_width // board_dim

piece_centerX = (board_height // (board_dim*2))
piece_centerY = (board_width // (board_dim*2)) + 100
piece_radius = piece_centerX - 2

game_quit = False

# Colors
white = (255, 255, 255)
black = (0, 0, 0)
red = (255, 0, 0)
blue = (100, 100, 100)
charcol = (60, 73, 96)
wood_light = (151, 157, 172)
wood_dark = (4, 102, 200)

gameDisplay = pygame.display.set_mode((display_width, display_height))
pygame.display.set_caption('Line of action')
clock = pygame.time.Clock()


def show_text(msg, x, y, size, color, font="magneto", sysfont=True):
    if sysfont:
        font = pygame.font.SysFont(font, size)
    else:
        font = pygame.font.Font(font, size)
    TextSurf = font.render(msg, True, color)
    TextRect = TextSurf.get_rect()
    TextRect.center = ((x), (y))
    gameDisplay.blit(TextSurf, TextRect)


def init_file(path):
    output = ""
    output += str(0) + "\n"
    output += str(1) + "\n"

    if board_dim == 8:
        output += "0 1 1 1 1 1 1 0\n2 0 0 0 0 0 0 2\n2 0 0 0 0 0 0 2\n2 0 0 0 0 0 0 2\n2 0 0 0 0 0 0 2\n2 0 0 0 0 0 0 2\n2 0 0 0 0 0 0 2\n0 1 1 1 1 1 1 0"
    elif board_dim == 6:
        output += "0 1 1 1 1 0\n2 0 0 0 0 2\n2 0 0 0 0 2\n2 0 0 0 0 2\n2 0 0 0 0 2\n0 1 1 1 1 0"

    with open(path, "w") as file:
        file.write(output)


def draw_banner(turn,previous = "0"):
    gameDisplay.fill(charcol, rect=(0, 0, 600, 100))

    if turn == "1":
        show_text("Whites Move", 100, 50, 25, white)
    elif turn == "2":
        show_text("Blacks Move", 100, 50, 25, white)
    elif turn == "100":
        if previous == "1":
            winner = "White"
        elif previous == "2":
            winner = "Black"
            
        show_text("{} Wins".format(winner), 100, 50, 25, white)
    # pygame.display.flip()

def draw_board():
    w = board_width / board_dim
    h = board_height / board_dim

    board = pygame.Surface((board_width, board_height))
    board.fill(white)
    for x in range(0, board_dim):
        for y in range(0, board_dim):
            color = wood_light if (x+y) % 2 == 0 else wood_dark
            pygame.draw.rect(board, color, (y*w, x*w, w, h))

    gameDisplay.blit(board, (0, 100))
    # pygame.display.flip()


def draw_piece(color, i, j):
    outer = piece_radius
    inner = piece_radius - (piece_radius * 0.30)

    centerX = piece_centerX + (j*(square_size))
    centerY = piece_centerY + (i*(square_size))
    position = (centerX, centerY)

    if color == "red" :
        show_text("{} {}".format(i,j), centerX,centerY, 15, charcol)
    else:
        image = pygame.draw.circle(gameDisplay, color, position, outer, width=5)
        image = pygame.draw.circle(gameDisplay, color, position, inner, width=15)



    


def read_file(path):
    with open(path,"r") as file:
        turn = file.readline().replace("\n", "")
        previous = file.readline().replace("\n", "")
        # print(line)

        board = []
        for i in range(0, board_dim):
            line = file.readline()
            line = line.replace("\n", "")
            temp = list(line.split(" "))
            board.append(temp)

    return turn, previous, board
    

def write_file(path,turn, previous, board):
    output = ""
    output += str(turn) + "\n"
    output += str(previous) + "\n"

    for i in range(0, board_dim):
        for j in range(0, board_dim):
            output += str(board[i][j])
            if j != board_dim - 1:
                 output += " "
        if i != board_dim - 1:
            output += "\n"
    
    print(output)
    with open(path, "w") as file:
        file.write(output)

    

# gameDisplay.fill(white, rect=(0, 100, 600, 600))
# pygame.display.flip()

init_file(filePath)

while not game_quit:
    turn, previous, board = read_file("game.txt")
    
    if turn == "0" or turn == "100":

        draw_board()

        #Drawing pieces
        for i in range(0, board_dim):
            for j in range(0, board_dim):
                draw_piece("red", i, j)
                if board[i][j] == "1":
                    draw_piece("white", i, j)
                elif board[i][j] == "2":
                    draw_piece("black", i, j)

        if turn == "100":
            draw_banner(turn, previous)
        elif turn == "0":
            previous = int(previous)
            turn = (previous % 2) + 1
            draw_banner(str(turn))
            write_file(filePath, turn, previous, board)

        pygame.display.flip()
        

    time.sleep(5)  # this sleep is must for synchronization
    clock.tick(60) 
    
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            quit()
