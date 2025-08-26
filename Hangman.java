import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;

public class Hangman {
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();
    
    // Word arrays for different subjects
    private static final String[] MATH_WORDS = {
        "ALGEBRA", "GEOMETRY", "CALCULUS", "FRACTION", "EQUATION", 
        "TRIANGLE", "CIRCLE", "SQUARE", "RECTANGLE", "DIAMETER",
        "POLYNOMIAL", "TRIGONOMETRY", "STATISTICS", "PROBABILITY", "LOGARITHM"
    };
    
    private static final String[] MALTESE_WORDS = {
        "MALTA", "GOZO", "VALLETTA", "MDINA", "MARSAXLOKK",
        "QORMI", "BIRKIRKARA", "HAMRUN", "SLIEMA", "FLORIANA",
        "MOSTA", "ZABBAR", "ZEJTUN", "NAXXAR", "MELLIEHA"
    };
    
    private static final String[] ENGLISH_WORDS = {
        "COMPUTER", "PROGRAMMING", "LANGUAGE", "EDUCATION", "KNOWLEDGE",
        "LITERATURE", "GRAMMAR", "VOCABULARY", "SENTENCE", "PARAGRAPH",
        "DICTIONARY", "LIBRARY", "READING", "WRITING", "SPELLING"
    };
    
    private static String currentWord;
    private static char[] guessedWord;
    private static boolean[] guessedLetters;
    private static int wrongGuesses;
    private static final int MAX_WRONG_GUESSES = 6;
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║            HANGMAN GAME               ║");
        System.out.println("║        Educational Edition            ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println();
        
        while (true) {
            try {
                showMainMenu();
                int subjectChoice = getUserChoice(1, 4);
                
                if (subjectChoice == 4) {
                    System.out.println("\nThank you for playing! Goodbye! 👋");
                    break;
                }
                
                String subject = getSubjectName(subjectChoice);
                System.out.println("\n📚 You selected: " + subject);
                
                showModeMenu();
                int modeChoice = getUserChoice(1, 3);
                
                if (modeChoice == 3) {
                    continue; // Go back to main menu
                }
                
                String[] wordArray = getWordArray(subjectChoice);
                setupGame(wordArray, modeChoice);
                showGameRules(subject, modeChoice);
                playGame();
                
                System.out.print("\nWould you like to play again? (y/n): ");
                String playAgain = scanner.nextLine().trim().toLowerCase();
                if (!playAgain.equals("y") && !playAgain.equals("yes")) {
                    System.out.println("\nThank you for playing! Goodbye! 👋");
                    break;
                }
                
            } catch (Exception e) {
                System.out.println("❌ An error occurred. Please try again.");
                scanner.nextLine(); // Clear the scanner buffer
            }
        }
        
        scanner.close();
    }
    
    private static void showMainMenu() {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║              MAIN MENU                ║");
        System.out.println("╠═══════════════════════════════════════╣");
        System.out.println("║  📐 1. Mathematics                    ║");
        System.out.println("║  🏛️  2. Maltese                       ║");
        System.out.println("║  📖 3. English                        ║");
        System.out.println("║  🚪 4. Exit Game                      ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.print("Choose a subject (1-4): ");
    }
    
    private static void showModeMenu() {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║             GAME MODE                 ║");
        System.out.println("╠═══════════════════════════════════════╣");
        System.out.println("║  🤖 1. Single Player (vs Computer)   ║");
        System.out.println("║  👥 2. Multiplayer (vs Friend)       ║");
        System.out.println("║  ⬅️  3. Back to Main Menu             ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.print("Choose game mode (1-3): ");
    }
    
    private static int getUserChoice(int min, int max) {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.print("❌ Invalid choice. Please enter a number between " + min + " and " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("❌ Invalid input. Please enter a valid number: ");
            }
        }
    }
    
    private static String getSubjectName(int choice) {
        switch (choice) {
            case 1: return "Mathematics";
            case 2: return "Maltese";
            case 3: return "English";
            default: return "Unknown";
        }
    }
    
    private static String[] getWordArray(int choice) {
        switch (choice) {
            case 1: return MATH_WORDS;
            case 2: return MALTESE_WORDS;
            case 3: return ENGLISH_WORDS;
            default: return ENGLISH_WORDS;
        }
    }
    
    private static void setupGame(String[] wordArray, int mode) {
        wrongGuesses = 0;
        guessedLetters = new boolean[26]; // A-Z
        
        if (mode == 1) { // Single player
            currentWord = wordArray[random.nextInt(wordArray.length)];
            System.out.println("\n🎲 Computer has chosen a word for you!");
        } else { // Multiplayer
            System.out.print("\n👤 Player 1, enter a word for Player 2 to guess: ");
            currentWord = scanner.nextLine().trim().toUpperCase();
            
            // Validate the word
            while (currentWord.isEmpty() || !currentWord.matches("[A-Z]+")) {
                System.out.print("❌ Please enter a valid word (letters only): ");
                currentWord = scanner.nextLine().trim().toUpperCase();
            }
            
            // Clear screen to hide the word
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
            
            System.out.println("✅ Word has been set! Player 2, it's your turn to guess!");
        }
        
        // Initialize guessed word array
        guessedWord = new char[currentWord.length()];
        Arrays.fill(guessedWord, '_');
    }
    
    private static void showGameRules(String subject, int mode) {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║              GAME RULES               ║");
        System.out.println("╠═══════════════════════════════════════╣");
        System.out.println("║  🎯 Subject: " + String.format("%-24s", subject) + " ║");
        System.out.println("║  🎮 Mode: " + String.format("%-27s", (mode == 1 ? "Single Player" : "Multiplayer")) + " ║");
        System.out.println("║                                       ║");
        System.out.println("║  📝 How to play:                      ║");
        System.out.println("║  • Guess letters one at a time       ║");
        System.out.println("║  • You have 6 wrong guesses allowed  ║");
        System.out.println("║  • Enter single letters only         ║");
        System.out.println("║  • Case doesn't matter               ║");
        System.out.println("║                                       ║");
        System.out.println("║  🏆 Win: Guess the complete word     ║");
        System.out.println("║  💀 Lose: Make 6 wrong guesses       ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println("\nPress Enter to start the game...");
        scanner.nextLine();
    }
    
    private static void playGame() {
        System.out.println("\n🎮 GAME STARTED! Good luck!");
        
        while (wrongGuesses < MAX_WRONG_GUESSES && !isWordComplete()) {
            displayGameState();
            
            System.out.print("\nEnter your guess (single letter): ");
            String input = scanner.nextLine().trim().toUpperCase();
            
            // Validate input
            if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                System.out.println("❌ Please enter a single letter only!");
                continue;
            }
            
            char guess = input.charAt(0);
            int letterIndex = guess - 'A';
            
            // Check if letter was already guessed
            if (guessedLetters[letterIndex]) {
                System.out.println("⚠️  You already guessed that letter! Try a different one.");
                continue;
            }
            
            // Mark letter as guessed
            guessedLetters[letterIndex] = true;
            
            // Check if guess is correct
            if (currentWord.contains(String.valueOf(guess))) {
                System.out.println("✅ Good guess! The letter '" + guess + "' is in the word!");
                
                // Update guessed word
                for (int i = 0; i < currentWord.length(); i++) {
                    if (currentWord.charAt(i) == guess) {
                        guessedWord[i] = guess;
                    }
                }
            } else {
                wrongGuesses++;
                System.out.println("❌ Wrong guess! The letter '" + guess + "' is not in the word.");
                System.out.println("💔 Wrong guesses: " + wrongGuesses + "/" + MAX_WRONG_GUESSES);
            }
        }
        
        // Game ended - show results
        displayGameState();
        
        if (isWordComplete()) {
            System.out.println("\n🎉 CONGRATULATIONS! 🎉");
            System.out.println("🏆 You successfully guessed the word: " + currentWord);
            System.out.println("📊 You made " + wrongGuesses + " wrong guesses out of " + MAX_WRONG_GUESSES + " allowed.");
        } else {
            System.out.println("\n💀 GAME OVER! 💀");
            System.out.println("😞 You ran out of guesses!");
            System.out.println("💡 The word was: " + currentWord);
        }
    }
    
    private static void displayGameState() {
        System.out.println("\n" + "=".repeat(50));
        
        // Display hangman
        displayHangman();
        
        // Display current word state
        System.out.println("Word: " + getWordDisplay());
        System.out.println("Wrong guesses: " + wrongGuesses + "/" + MAX_WRONG_GUESSES);
        
        // Display guessed letters
        System.out.println("Guessed letters: " + getGuessedLettersDisplay());
        
        System.out.println("=".repeat(50));
    }
    
    private static void displayHangman() {
        String[] hangman = {
            "   ╔═══╗",
            "   ║   ║",
            "   ║   " + (wrongGuesses > 0 ? "😵" : " "),
            "   ║  " + (wrongGuesses > 2 ? "/" : " ") + (wrongGuesses > 1 ? "|" : " ") + (wrongGuesses > 3 ? "\\" : " "),
            "   ║  " + (wrongGuesses > 4 ? "/" : " ") + " " + (wrongGuesses > 5 ? "\\" : " "),
            "   ║",
            "═══╩═══"
        };
        
        for (String line : hangman) {
            System.out.println(line);
        }
    }
    
    private static String getWordDisplay() {
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < guessedWord.length; i++) {
            display.append(guessedWord[i]);
            if (i < guessedWord.length - 1) {
                display.append(" ");
            }
        }
        return display.toString();
    }
    
    private static String getGuessedLettersDisplay() {
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            if (guessedLetters[i]) {
                display.append((char) ('A' + i)).append(" ");
            }
        }
        return display.toString().trim();
    }
    
    private static boolean isWordComplete() {
        for (char c : guessedWord) {
            if (c == '_') {
                return false;
            }
        }
        return true;
    }
}
