1. Github: https://github.com/Darren002/CW2024

		2. Compilation Instructions
   	 2.1. Download the zip file from GitHub and extract DarrenRaj_Intellij_21.zip.
    2.2. Open IntelliJ and import the CW2024-master project.
    2.3. If prompted, allow Maven to load by clicking the option in the bottom-right corner.
    2.4. Navigate to src/main/java/com.example.demo/Controller/Main.java and open SkyBattleMainMenu.java.
    2.5. Check if volume if muted, if muted turn on volume so that background music can be heard 
    2.6. Once Maven is fully loaded and SkyBattleMainMenu.java is open, run the main method to start the game.

3. Implemented and Working Properly
   
    3.1 Added W keys (up) and S key (down) for UserPlane movements
        3.1.1. UserPlane can now be moved using keys other than the arrow keys, providing users with more diversity and control choices while playing.
    3.2 Added mouse left click to shoot from UserPlane (Innovative Features)
        3.2.1. Provides user with more comfort as most games use right click to shoot
    3.3. Added “KillTracker” to display the killcount to LevelOne, LevelTwo and LevelThree
        3.3.1. Killtracker was added at the top right of the screen to display how many enemies the user killed
    3.4 Added Boss health Bar in Level Boss and Level Bonus (Innovative Features)
        3.4.1. Added Health bar to visually display the boss’s health
        3.4.2 Health bar has turn colour from green (high health) to orange (medium health) and red (low health)
    3.5 Added pause to the game
        3.5.1. User presses p to pause the game.
        3.5.2. User presses p again to resume the game
    3.6. Added Audio
         3.6.1. Added background music to the game
    3.7. Added Level Three, Level Boss and Bonus Level
         3.7.1. Added 3 new levels to the game, expanding from 2 levels to 5 levels.
         3.7.2. Levels Two and Three are similar to Level One but with more enemies spawned and higher kill requirements:
         3.7.3 Level Two: 7 enemies spawned; 20 kills required to proceed.
         3.7.4 Level Three: 9 enemies spawned; 25 kills required to proceed.
         3.7.5. Level Boss was originally Level Two, now upgraded to a dedicated boss level.
         3.7.6. Bonus Level is an enhanced version of Level Boss, featuring two bosses instead of one.
3.8. Added Burst feature (Innovative Features)
         3.8.1. Burst for UserPlane allow users to shoot multiple bullets at once 
         3.8.2. Press "B" key to activate burst when available
         3.8.3. Text will be shown at the top center when Burst is available.
         3.8.4. Burst will shoot out 5 bullets. 
         3.8.5. Burst has a cooldown of 4 seconds.   
         3.8.6 Burst text will start to blink when burst is ready but not used 
3.9 Added Main Menu to game called Sky Battle Main Menu
         3.9.1 Main Menu consists of Three Buttons (Play, Instructions, Exit)
         3.9.2 The play button starts the game
         3.9.3 The instruction button leads u to an instruction page. After reading user can close the instruction page and will be directed back to the main menu page
         3.9.4 The exit button closes the game
3.10 Added transition screen with a timer
         3.10.1 Before starting a level, an image will appear displaying the level name, such as "Level One" or "Level Two” and etc that last for 3 seconds.
3.11 Added level Cleared message with different animation for each level 
         3.11.1 For level one, level two and level 3 a “Level Cleared” message will be displayed when user reaches the number of kills to proceed
         3.11.2 For level Boss a “Level Cleared” message will be displayed when the boss dies
3.12 Prevention of Spacebar Spamming
         3.12.1 Implemented a flag (spacebar Pressed) to prevent the user from firing projectiles by holding or rapidly pressing the spacebar.
         3.12.2  Shooting is only allowed when the spacebar is released and pressed again.


4. Implemented but not Working Properly
   
4.1. Enemy planes that make it to the end of screen is counted as a kill
        4.1.1. Enemy planes should not be counted as kills if they reach the end
4.2. Enemy planes are affected by bullets before they are visible in the screen
        4.2.1. Enemy planes are killed before they are fully seen on screen
4.3. Pause method not pausing when in countdown.
        4.3.1. During countdown, when pressed P, it doesnt pause and it continues gameply
        4.3.2. When press P it should pause the whole game including countdown. 
4.4 Bounds of Main menu button 
        4.4.1 The click radius of the button is larger than it’s supposed to be
4.5 Pause not working during “Level Cleared”
        4.5.1 When p is pressed during “Level Cleared” message the animation of the message continues
4.6 Retro Font issue
        4.6.1 Retro Font doesn’t seem to work in other computers

5. New Java Classes
   
    5.1 Class: LevelTwo
        5.1.1. Location: com.example.demo.Levels
        5.1.2 Purpose:
               Defines the second level of the game, which is essentially the same as level one but with increased enemy on screen and enemy kills required to proceed. The class is responsible for:
                   • Setting up the level environment with a desert-themed background.
                   • Spawning enemy planes based on predefined probabilities and limits.
                   • Managing player health and kill count for level progression.
                   • Displaying UI elements like a kill tracker for real-time updates.
                   • Handling game-over conditions and transitioning to the next level when the kill target is achieved.
                   • Adding an animated "Level Cleared" message to enhance user experience.
       5.1.3. Key Methods: 
            - checkIfGameOver()
            - spawnEnemyUnits()
            - instantiateLevelView()
            - updateScene()
    	    -showLevelClearedMessage()
	        - initializeFriendlyUnits()

   5.2 Class: LevelTwo
     5.2.1. Location: com.example.demo.Levels
     5.2.2 Purpose:
            Defines the third level of the game, which is essentially the same as level one and level two but with increased enemy on screen and enemy kills required to proceed compared to level two. The class               is responsible for:
                • Setting up the level environment with a jungle-themed background.
                • Spawning enemy planes based on probability while maintaining the maximum number of active enemies.
                • Managing player health and tracking kills required to advance.
                • Displaying UI elements such as a kill tracker for real-time updates.
                • Handling game-over conditions and transitioning to the next level upon meeting the kill target.
                • Enhancing the user experience with an animated "Level Cleared" message.
     5.2.3. Key Methods: 
           - checkIfGameOver()
           - spawnEnemyUnits()
           - instantiateLevelView()
           - updateScene()
	       - showLevelClearedMessage()
	       - initializeFriendlyUnits()

   5.4 Class: LevelBoss (Originally LevelTwo)
       5.4.1. Location: com.example.demo.Levels
       5.4.2. Purpose  : 
                 Defines the "boss battle" level of the game, where the player confronts a boss enemy. This level focuses on:
                    •	Introducing the boss with unique mechanics, such as a shield and health bar.
                    •	Managing player health and the boss's destruction status.
                    •	Displaying "Game Over" or "Level Cleared" messages based on outcomes.
                    •	Handling transitions to a bonus level upon defeating the boss.
                    •	Enhancing gameplay with a visually appealing background and effects.
       5.4.3. Key Methods: 
                     - initializeFriendlyUnits()
                     - checkIfGameOver()
                     - spawnEnemyUnits()
                     - instantiateLevelView()
                     - updateShieldPosition()
	                 - showLevelClearedMessage()

5.5 Class:BonusLevel 
     5.5.1. Location: com.example.demo.levels
     5.5.2. Purpose  :
             Defines the "bonus level" of the game, featuring a unique setup with two bosses. This level focuses on:
                    •	Managing interactions between the player and two bosses simultaneously.
                    •	Ensuring smooth gameplay by preventing boss overlap.
                    •	Providing an enhanced challenge with increased player health and distinct boss mechanics.
                    •	Checking win/loss conditions and updating actors dynamically.
                    • Enhancing gameplay with a visually appealing background and effects.
                    • Handle game over and win conditions based on the boss's status.
    5.5.3. Key Methods: 
                     - initializeFriendlyUnits()
                     - checkIfGameOver()
                     - spawnEnemyUnits()
                     - instantiateLevelView()
	                 -update actors()

5.6 Class: Healthbar 
     5.6.1. Location: com.example.demo.Healthbar.java
     5.6.2. Purpose  :
            Defines a health bar for visual representation of the player's health, with a dynamic width and color that changes based on the current health. This class focuses on:
                •	Displaying a graphical health bar with two rectangles: one for the background and one for the health.
                •	Dynamically adjusting the health bar's width based on the current health relative to the maximum health.
                •	Changing the health bar's color to reflect health status: green for high health, orange for medium health, and red for low health.
     5.6.3. Key Methods: 
        - HealthBar(double width, double height)
        - updateHealth(double currentHealth, double maxHealth)

5.7 Class: BonusLevelView Class
     5.7.1. Location: com.example.demo.BonusLevelView.java
     5.7.2. Purpose  :
            Defines the visual components of the bonus level in the game, adding specific shield images for the two boss characters. This class:
                •	Extends the LevelView class to inherit general level components.
                •	Initializes and places the shields for the bosses at specified screen positions.
                •	Adds the shield images to the root container for display during the bonus level.
     5.7.3. Key Methods: 
            - BonusLevelView(Group root, int heartsToDisplay)
            - addImagesToRoot()

			
	    











