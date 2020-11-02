import pygame
import time

pygame.init()

# Constants

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


def show_text(msg, x, y, size, color, font="magneto", sysfont=True):
    if sysfont:
        font = pygame.font.SysFont(font, size)
    else:
        font = pygame.font.Font(font, size)
    TextSurf = font.render(msg, True, color)
    TextRect = TextSurf.get_rect()
    TextRect.center = ((x), (y))
    gameDisplay.blit(TextSurf, TextRect)


def draw_banner(turn):
    gameDisplay.fill(charcol, rect=(0, 0, 600, 100))

    if turn == "1":
        show_text("Whites Move", 100, 50, 25, white)
    elif turn == "2":
        show_text("Blacks Move", 100, 50, 25, white)
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

    image = pygame.draw.circle(gameDisplay, color, position, outer, width=5)
    image = pygame.draw.circle(gameDisplay, color, position, inner, width=15)



    


def read_file():
    with open("game.txt") as file:
        turn = file.readline().replace("\n", "")
        # print(line)

        board = []
        for i in range(0, 8):
            line = file.readline()
            line = line.replace("\n", "")
            temp = list(line.split(" "))
            board.append(temp)

    return turn, board


# gameDisplay.fill(white, rect=(0, 100, 600, 600))
# pygame.display.flip()

turn, board = read_file()

while not game_quit:
    turn, board = read_file()

    draw_banner(turn)

    draw_board()

    #Drawing pieces
    for i in range(0, 8):
        for j in range(0, 8):
            if board[i][j] == "1":
                draw_piece("white", i, j)
            elif board[i][j] == "2":
                draw_piece("black", i, j)

    pygame.display.flip()

    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            quit()
