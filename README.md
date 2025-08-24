# 🎮 Pac-Man Game

A classic Pac-Man game implementation in Java using Swing for graphics and game mechanics.

## 📸 Game Features

- **Classic Pac-Man Gameplay**: Navigate through a maze, collect food, and avoid ghosts
- **Multiple Ghosts**: Four different colored ghosts (Red, Blue, Pink, Orange) with AI movement
- **Score System**: Earn points by collecting food items
- **Lives System**: Start with 3 lives, lose a life when caught by ghosts
- **Collision Detection**: Realistic collision detection for walls, food, and ghosts
- **Smooth Movement**: Arrow key controls with smooth character movement
- **Game States**: Start screen, gameplay, and game over states

## 🎯 How to Play

1. **Starting the Game**: Press any key to start the game
2. **Movement**: Use arrow keys to move Pac-Man
   - ↑ Arrow Key: Move Up
   - ↓ Arrow Key: Move Down
   - ← Arrow Key: Move Left
   - → Arrow Key: Move Right
3. **Objective**: Collect all the white food dots while avoiding the colored ghosts
4. **Scoring**: Each food dot gives you 10 points
5. **Lives**: You have 3 lives. Touching a ghost costs you a life
6. **Game Over**: Game ends when all lives are lost
7. **Restart**: Press any key after game over to restart

## 🛠️ Technical Details

### Technologies Used
- **Java**: Core programming language
- **Swing**: GUI framework for graphics and user interface
- **AWT**: Used for graphics rendering and event handling

### Game Architecture
- **Object-Oriented Design**: Uses classes and objects for game entities
- **Game Loop**: Timer-based game loop running at 20 FPS
- **Event-Driven**: Keyboard event handling for player input
- **Collision System**: Rectangle-based collision detection

### Key Classes
- `app.java`: Main class that sets up the JFrame and initializes the game
- `PacMan.java`: Core game class containing all game logic, rendering, and mechanics
- `Block`: Inner class representing game entities (Pac-Man, ghosts, walls, food)

### Game Grid
- **Board Size**: 19 columns × 21 rows
- **Tile Size**: 32×32 pixels
- **Total Resolution**: 608×672 pixels

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Any Java IDE (VS Code, IntelliJ IDEA, Eclipse) or command line

### Installation & Running

1. **Clone or Download** the project
2. **Navigate** to the project directory
3. **Compile** the Java files:
   ```bash
   javac src/*.java
   ```
4. **Run** the game:
   ```bash
   java -cp src app
   ```

### Project Structure
```
PacMan/
├── README.md
└── src/
    ├── app.java              # Main application entry point
    ├── PacMan.java           # Core game logic and mechanics
    ├── his.java              # Additional class file
    ├── *.class               # Compiled bytecode files
    └── assets/               # Game sprites and images
        ├── pacmanUp.png      # Pac-Man facing up
        ├── pacmanDown.png    # Pac-Man facing down
        ├── pacmanLeft.png    # Pac-Man facing left
        ├── pacmanRight.png   # Pac-Man facing right
        ├── redGhost.png      # Red ghost sprite
        ├── blueGhost.png     # Blue ghost sprite
        ├── pinkGhost.png     # Pink ghost sprite
        ├── orangeGhost.png   # Orange ghost sprite
        ├── scaredGhost.png   # Scared ghost state
        ├── wall.png          # Wall tile sprite
        ├── cherry.png        # Cherry power-up
        ├── cherry2.png       # Alternative cherry sprite
        └── powerFood.png     # Power pellet sprite
```

## 🎮 Game Mechanics

### Movement System
- **Speed**: Characters move at 1/4 tile per frame
- **Direction Changes**: Smooth direction changes with collision prevention
- **Wall Collision**: Characters cannot move through walls

### Ghost AI
- **Random Movement**: Ghosts change direction randomly when hitting walls
- **Spawn Behavior**: Ghosts start at designated positions and move in random directions
- **Collision**: Ghosts reset to starting positions after catching Pac-Man

### Map Layout
The game uses a character-based map system:
- `X`: Wall blocks
- `P`: Pac-Man starting position
- `r`, `b`, `p`, `o`: Red, Blue, Pink, Orange ghost starting positions
- ` ` (space): Food dots
- `O`: Empty space (no collision)

## 🎯 Game Controls Summary

| Key | Action |
|-----|--------|
| Any Key | Start Game (when not started) |
| ↑ | Move Up |
| ↓ | Move Down |
| ← | Move Left |
| → | Move Right |
| Any Key | Restart (when game over) |

## 🏆 Scoring System

- **Food Dot**: 10 points each
- **Lives**: Start with 3 lives
- **Life Lost**: When Pac-Man touches any ghost

## 🐛 Known Features
- Ghost AI with random movement patterns
- Collision detection for all game entities
- Smooth character animations
- Real-time score and lives display
- Game state management (start, playing, game over)

## 🔧 Development Notes

- Game runs at 20 FPS (50ms timer intervals)
- Uses HashSet collections for efficient entity management
- Implements proper separation of concerns with distinct classes
- Event-driven architecture for responsive controls

## 📝 License

This project is created for educational purposes. Feel free to use and modify as needed.

## 🤝 Contributing

Feel free to fork this project and submit pull requests for improvements or bug fixes.

---

**Enjoy playing Pac-Man! 🎮👻**