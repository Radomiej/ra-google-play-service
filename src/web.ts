import { WebPlugin } from '@capacitor/core';

import type { AchievementOptions, IncrementAchievementOptions, LeaderboardOptions, LoadGameOptions, LoadGameResult, PGServicePlugin, PlayerInfo, SaveGameOptions, SaveResult, ShowSavedGamesOptions, SignInResult, SubmitScoreOptions } from './definitions';

export class PGServiceWeb extends WebPlugin implements PGServicePlugin {
  constructor() {
    super();
  }

  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  async signIn(): Promise<SignInResult> {
    console.warn('Google Play Games Services are not available on web platform.');
    return { isSignedIn: false };
  }

  async signOut(): Promise<void> {
    console.warn('Google Play Games Services are not available on web platform.');
  }

  async isSignedIn(): Promise<SignInResult> {
    console.warn('Google Play Games Services are not available on web platform.');
    return { isSignedIn: false };
  }

  async getPlayerInfo(): Promise<PlayerInfo> {
    console.warn('Google Play Games Services are not available on web platform.');
    return {
      playerId: '',
      displayName: '',
    };
  }

  async showLeaderboard(options: LeaderboardOptions): Promise<void> {
    console.warn(`Google Play Games Services are not available on web platform. Cannot show leaderboard: ${options.leaderboardId}`);
  }

  async showAllLeaderboards(): Promise<void> {
    console.warn('Google Play Games Services are not available on web platform. Cannot show all leaderboards.');
  }

  async submitScore(options: SubmitScoreOptions): Promise<void> {
    console.warn(`Google Play Games Services are not available on web platform. Cannot submit score: ${options.score} to leaderboard: ${options.leaderboardId}`);
  }

  async showAchievements(): Promise<void> {
    console.warn('Google Play Games Services are not available on web platform. Cannot show achievements.');
  }

  async unlockAchievement(options: AchievementOptions): Promise<void> {
    console.warn(`Google Play Games Services are not available on web platform. Cannot unlock achievement: ${options.achievementId}`);
  }

  async incrementAchievement(options: IncrementAchievementOptions): Promise<void> {
    console.warn(`Google Play Games Services are not available on web platform. Cannot increment achievement: ${options.achievementId} by ${options.steps} steps`);
  }

  async revealAchievement(options: AchievementOptions): Promise<void> {
    console.warn(`Google Play Games Services are not available on web platform. Cannot reveal achievement: ${options.achievementId}`);
  }

  async saveGameData(options: SaveGameOptions): Promise<SaveResult> {
    console.warn(`Google Play Games Services are not available on web platform. Cannot save game data with ID: ${options.saveId || 'auto-generated'}`);
    return { success: false, error: 'Not available on web platform' };
  }

  async loadGameData(options: LoadGameOptions): Promise<LoadGameResult> {
    console.warn(`Google Play Games Services are not available on web platform. Cannot load game data with ID: ${options.saveId}`);
    return { success: false, error: 'Not available on web platform' };
  }

  async showSavedGames(options: ShowSavedGamesOptions): Promise<LoadGameResult> {
    console.warn(`Google Play Games Services are not available on web platform. Cannot show saved games with title: ${options.title}`);
    return { success: false, error: 'Not available on web platform' };
  }

  async addListener(
    _eventName: 'signInStatusChanged',
    _listenerFunc: (result: SignInResult) => void,
  ): Promise<any> {
    console.warn('Google Play Games Services are not available on web. Event listeners will not be triggered.');
    return Promise.resolve({ remove: () => {} });
  }

  async removeAllListeners(): Promise<void> {
    console.warn('Google Play Games Services are not available on web. No listeners to remove.');
  }
}
