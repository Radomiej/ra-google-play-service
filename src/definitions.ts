import type { PluginListenerHandle } from '@capacitor/core';

export interface PGServicePlugin {
  /**
   * Sign in the user to Google Play Games Services
   * @returns Promise with sign-in result
   */
  signIn(): Promise<SignInResult>;

  /**
   * Sign out the user from Google Play Games Services
   * @returns Promise with void result
   */
  signOut(): Promise<void>;

  /**
   * Check if user is signed in to Google Play Games Services
   * @returns Promise with sign-in status
   */
  isSignedIn(): Promise<{ isSignedIn: boolean }>;

  /**
   * Get the currently signed in player information
   * @returns Promise with player data
   */
  getPlayerInfo(): Promise<PlayerInfo>;

  /**
   * Show the leaderboard UI for a specific leaderboard
   * @param options Options containing leaderboard ID
   * @returns Promise with void result
   */
  showLeaderboard(options: LeaderboardOptions): Promise<void>;

  /**
   * Show all leaderboards UI
   * @returns Promise with void result
   */
  showAllLeaderboards(): Promise<void>;

  /**
   * Submit a score to a leaderboard
   * @param options Options containing leaderboard ID and score
   * @returns Promise with void result
   */
  submitScore(options: SubmitScoreOptions): Promise<void>;

  /**
   * Show the achievements UI
   * @returns Promise with void result
   */
  showAchievements(): Promise<void>;

  /**
   * Unlock an achievement
   * @param options Options containing achievement ID
   * @returns Promise with void result
   */
  unlockAchievement(options: AchievementOptions): Promise<void>;

  /**
   * Increment an achievement with the specified amount
   * @param options Options containing achievement ID and increment amount
   * @returns Promise with void result
   */
  incrementAchievement(options: IncrementAchievementOptions): Promise<void>;

  /**
   * Reveal a hidden achievement
   * @param options Options containing achievement ID
   * @returns Promise with void result
   */
  revealAchievement(options: AchievementOptions): Promise<void>;

  /**
   * Save game data to the cloud
   * @param options Options containing save data and description
   * @returns Promise with SaveResult
   */
  saveGameData(options: SaveGameOptions): Promise<SaveResult>;

  /**
   * Load saved game data from the cloud
   * @param options Options for loading saved game
   * @returns Promise with LoadGameResult
   */
  loadGameData(options: LoadGameOptions): Promise<LoadGameResult>;

  /**
   * Show the saved games UI
   * @param options Options for showing saved games UI
   * @returns Promise with LoadGameResult if a save was selected
   */
  showSavedGames(options: ShowSavedGamesOptions): Promise<LoadGameResult>;

  /**
   * Add listener for sign-in state changes
   * @param eventName Event to listen for
   * @param listenerFunc Callback function
   * @returns Promise with listener handle
   */
  addListener(
    eventName: 'signInStatusChanged',
    listenerFunc: (result: SignInResult) => void,
  ): Promise<PluginListenerHandle>;

  /**
   * Remove all listeners for this plugin
   * @returns Promise with void result
   */
  removeAllListeners(): Promise<void>;
}

export interface SignInResult {
  /**
   * Whether the user is signed in
   */
  isSignedIn: boolean;
  
  /**
   * Player ID if signed in
   */
  playerId?: string;
  
  /**
   * Player display name if signed in
   */
  displayName?: string;
  
  /**
   * URL to the player's profile image if signed in
   */
  imageUrl?: string;
  
  /**
   * Error message if sign in failed
   */
  error?: string;
}

export interface PlayerInfo {
  /**
   * Player ID
   */
  playerId: string;
  
  /**
   * Player display name
   */
  displayName: string;
  
  /**
   * URL to the player's profile image
   */
  imageUrl?: string;
}

export interface LeaderboardOptions {
  /**
   * Leaderboard ID as defined in Google Play Console
   */
  leaderboardId: string;
}

export interface SubmitScoreOptions extends LeaderboardOptions {
  /**
   * Score to submit to the leaderboard
   */
  score: number;
}

export interface AchievementOptions {
  /**
   * Achievement ID as defined in Google Play Console
   */
  achievementId: string;
}

export interface IncrementAchievementOptions extends AchievementOptions {
  /**
   * Amount to increment the achievement by
   */
  steps: number;
}

export interface SaveGameOptions {
  /**
   * Unique identifier for the save
   */
  saveId?: string;
  
  /**
   * Save data as string (will be encoded as needed)
   */
  data: string;
  
  /**
   * Human readable description of the save
   */
  description: string;
}

export interface LoadGameOptions {
  /**
   * Unique identifier for the save to load
   */
  saveId: string;
}

export interface ShowSavedGamesOptions {
  /**
   * Title to show in the saved games UI
   */
  title: string;
  
  /**
   * Whether to allow creating new saves
   */
  allowAddButton?: boolean;
  
  /**
   * Whether to allow deleting saves
   */
  allowDelete?: boolean;
  
  /**
   * Maximum number of saves to show
   */
  maxSavedGames?: number;
}

export interface SaveResult {
  /**
   * Whether save was successful
   */
  success: boolean;
  
  /**
   * Save ID of the saved game
   */
  saveId?: string;
  
  /**
   * Error message if save failed
   */
  error?: string;
}

export interface LoadGameResult {
  /**
   * Whether data was successfully loaded
   */
  success: boolean;
  
  /**
   * Save ID that was loaded
   */
  saveId?: string;
  
  /**
   * The loaded save data
   */
  data?: string;
  
  /**
   * Description of the save
   */
  description?: string;
  
  /**
   * Last modified timestamp
   */
  lastModifiedTimestamp?: number;
  
  /**
   * Error message if load failed
   */
  error?: string;
}
