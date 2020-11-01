import pygame

pygame.init()

display_width = 600
display_height = 600

game_quit = False

# Colors
white = (255, 255, 255)
black = (0, 0, 0)
charcol = (60, 73, 96)

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


gameDisplay.fill(white, rect=(0, 100, 600, 600))
pygame.display.flip()


while not game_quit:

    with open("game.txt") as file:
        line = file.read()

    gameDisplay.fill(charcol, rect=(0, 0, 600, 100))
    if line == "1":
        show_text("Whites Move", 100, 50, 25, white)
    elif line == "2":
        show_text("Blacks Move", 100, 50, 25, white)

    pygame.display.flip()

    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            quit()
