# ra-google-play-service

Expose Google Play Game Service API

## Install

```bash
npm install ra-google-play-service
npx cap sync
```

## API

<docgen-index>

* [`signIn()`](#signin)
* [`signOut()`](#signout)
* [`isSignedIn()`](#issignedin)
* [`getPlayerInfo()`](#getplayerinfo)
* [`showLeaderboard(...)`](#showleaderboard)
* [`showAllLeaderboards()`](#showallleaderboards)
* [`submitScore(...)`](#submitscore)
* [`showAchievements()`](#showachievements)
* [`unlockAchievement(...)`](#unlockachievement)
* [`incrementAchievement(...)`](#incrementachievement)
* [`revealAchievement(...)`](#revealachievement)
* [`saveGameData(...)`](#savegamedata)
* [`loadGameData(...)`](#loadgamedata)
* [`showSavedGames(...)`](#showsavedgames)
* [`addListener('signInStatusChanged', ...)`](#addlistenersigninstatuschanged-)
* [`removeAllListeners()`](#removealllisteners)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### signIn()

```typescript
signIn() => Promise<SignInResult>
```

Sign in the user to Google Play Games Services

**Returns:** <code>Promise&lt;<a href="#signinresult">SignInResult</a>&gt;</code>

--------------------


### signOut()

```typescript
signOut() => Promise<void>
```

Sign out the user from Google Play Games Services

--------------------


### isSignedIn()

```typescript
isSignedIn() => Promise<{ isSignedIn: boolean; }>
```

Check if user is signed in to Google Play Games Services

**Returns:** <code>Promise&lt;{ isSignedIn: boolean; }&gt;</code>

--------------------


### getPlayerInfo()

```typescript
getPlayerInfo() => Promise<PlayerInfo>
```

Get the currently signed in player information

**Returns:** <code>Promise&lt;<a href="#playerinfo">PlayerInfo</a>&gt;</code>

--------------------


### showLeaderboard(...)

```typescript
showLeaderboard(options: LeaderboardOptions) => Promise<void>
```

Show the leaderboard UI for a specific leaderboard

| Param         | Type                                                              | Description                       |
| ------------- | ----------------------------------------------------------------- | --------------------------------- |
| **`options`** | <code><a href="#leaderboardoptions">LeaderboardOptions</a></code> | Options containing leaderboard ID |

--------------------


### showAllLeaderboards()

```typescript
showAllLeaderboards() => Promise<void>
```

Show all leaderboards UI

--------------------


### submitScore(...)

```typescript
submitScore(options: SubmitScoreOptions) => Promise<void>
```

Submit a score to a leaderboard

| Param         | Type                                                              | Description                                 |
| ------------- | ----------------------------------------------------------------- | ------------------------------------------- |
| **`options`** | <code><a href="#submitscoreoptions">SubmitScoreOptions</a></code> | Options containing leaderboard ID and score |

--------------------


### showAchievements()

```typescript
showAchievements() => Promise<void>
```

Show the achievements UI

--------------------


### unlockAchievement(...)

```typescript
unlockAchievement(options: AchievementOptions) => Promise<void>
```

Unlock an achievement

| Param         | Type                                                              | Description                       |
| ------------- | ----------------------------------------------------------------- | --------------------------------- |
| **`options`** | <code><a href="#achievementoptions">AchievementOptions</a></code> | Options containing achievement ID |

--------------------


### incrementAchievement(...)

```typescript
incrementAchievement(options: IncrementAchievementOptions) => Promise<void>
```

Increment an achievement with the specified amount

| Param         | Type                                                                                | Description                                            |
| ------------- | ----------------------------------------------------------------------------------- | ------------------------------------------------------ |
| **`options`** | <code><a href="#incrementachievementoptions">IncrementAchievementOptions</a></code> | Options containing achievement ID and increment amount |

--------------------


### revealAchievement(...)

```typescript
revealAchievement(options: AchievementOptions) => Promise<void>
```

Reveal a hidden achievement

| Param         | Type                                                              | Description                       |
| ------------- | ----------------------------------------------------------------- | --------------------------------- |
| **`options`** | <code><a href="#achievementoptions">AchievementOptions</a></code> | Options containing achievement ID |

--------------------


### saveGameData(...)

```typescript
saveGameData(options: SaveGameOptions) => Promise<SaveResult>
```

Save game data to the cloud

| Param         | Type                                                        | Description                                  |
| ------------- | ----------------------------------------------------------- | -------------------------------------------- |
| **`options`** | <code><a href="#savegameoptions">SaveGameOptions</a></code> | Options containing save data and description |

**Returns:** <code>Promise&lt;<a href="#saveresult">SaveResult</a>&gt;</code>

--------------------


### loadGameData(...)

```typescript
loadGameData(options: LoadGameOptions) => Promise<LoadGameResult>
```

Load saved game data from the cloud

| Param         | Type                                                        | Description                    |
| ------------- | ----------------------------------------------------------- | ------------------------------ |
| **`options`** | <code><a href="#loadgameoptions">LoadGameOptions</a></code> | Options for loading saved game |

**Returns:** <code>Promise&lt;<a href="#loadgameresult">LoadGameResult</a>&gt;</code>

--------------------


### showSavedGames(...)

```typescript
showSavedGames(options: ShowSavedGamesOptions) => Promise<LoadGameResult>
```

Show the saved games UI

| Param         | Type                                                                    | Description                        |
| ------------- | ----------------------------------------------------------------------- | ---------------------------------- |
| **`options`** | <code><a href="#showsavedgamesoptions">ShowSavedGamesOptions</a></code> | Options for showing saved games UI |

**Returns:** <code>Promise&lt;<a href="#loadgameresult">LoadGameResult</a>&gt;</code>

--------------------


### addListener('signInStatusChanged', ...)

```typescript
addListener(eventName: 'signInStatusChanged', listenerFunc: (result: SignInResult) => void) => Promise<PluginListenerHandle>
```

Add listener for sign-in state changes

| Param              | Type                                                                       | Description         |
| ------------------ | -------------------------------------------------------------------------- | ------------------- |
| **`eventName`**    | <code>'signInStatusChanged'</code>                                         | Event to listen for |
| **`listenerFunc`** | <code>(result: <a href="#signinresult">SignInResult</a>) =&gt; void</code> | Callback function   |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### removeAllListeners()

```typescript
removeAllListeners() => Promise<void>
```

Remove all listeners for this plugin

--------------------


### Interfaces


#### SignInResult

| Prop              | Type                 | Description                                    |
| ----------------- | -------------------- | ---------------------------------------------- |
| **`isSignedIn`**  | <code>boolean</code> | Whether the user is signed in                  |
| **`playerId`**    | <code>string</code>  | Player ID if signed in                         |
| **`displayName`** | <code>string</code>  | Player display name if signed in               |
| **`imageUrl`**    | <code>string</code>  | URL to the player's profile image if signed in |
| **`error`**       | <code>string</code>  | Error message if sign in failed                |


#### PlayerInfo

| Prop              | Type                | Description                       |
| ----------------- | ------------------- | --------------------------------- |
| **`playerId`**    | <code>string</code> | Player ID                         |
| **`displayName`** | <code>string</code> | Player display name               |
| **`imageUrl`**    | <code>string</code> | URL to the player's profile image |


#### LeaderboardOptions

| Prop                | Type                | Description                                      |
| ------------------- | ------------------- | ------------------------------------------------ |
| **`leaderboardId`** | <code>string</code> | Leaderboard ID as defined in Google Play Console |


#### SubmitScoreOptions

| Prop        | Type                | Description                        |
| ----------- | ------------------- | ---------------------------------- |
| **`score`** | <code>number</code> | Score to submit to the leaderboard |


#### AchievementOptions

| Prop                | Type                | Description                                      |
| ------------------- | ------------------- | ------------------------------------------------ |
| **`achievementId`** | <code>string</code> | Achievement ID as defined in Google Play Console |


#### IncrementAchievementOptions

| Prop        | Type                | Description                            |
| ----------- | ------------------- | -------------------------------------- |
| **`steps`** | <code>number</code> | Amount to increment the achievement by |


#### SaveResult

| Prop          | Type                 | Description                  |
| ------------- | -------------------- | ---------------------------- |
| **`success`** | <code>boolean</code> | Whether save was successful  |
| **`saveId`**  | <code>string</code>  | Save ID of the saved game    |
| **`error`**   | <code>string</code>  | Error message if save failed |


#### SaveGameOptions

| Prop              | Type                | Description                                     |
| ----------------- | ------------------- | ----------------------------------------------- |
| **`saveId`**      | <code>string</code> | Unique identifier for the save                  |
| **`data`**        | <code>string</code> | Save data as string (will be encoded as needed) |
| **`description`** | <code>string</code> | Human readable description of the save          |


#### LoadGameResult

| Prop                        | Type                 | Description                          |
| --------------------------- | -------------------- | ------------------------------------ |
| **`success`**               | <code>boolean</code> | Whether data was successfully loaded |
| **`saveId`**                | <code>string</code>  | Save ID that was loaded              |
| **`data`**                  | <code>string</code>  | The loaded save data                 |
| **`description`**           | <code>string</code>  | Description of the save              |
| **`lastModifiedTimestamp`** | <code>number</code>  | Last modified timestamp              |
| **`error`**                 | <code>string</code>  | Error message if load failed         |


#### LoadGameOptions

| Prop         | Type                | Description                            |
| ------------ | ------------------- | -------------------------------------- |
| **`saveId`** | <code>string</code> | Unique identifier for the save to load |


#### ShowSavedGamesOptions

| Prop                 | Type                 | Description                         |
| -------------------- | -------------------- | ----------------------------------- |
| **`title`**          | <code>string</code>  | Title to show in the saved games UI |
| **`allowAddButton`** | <code>boolean</code> | Whether to allow creating new saves |
| **`allowDelete`**    | <code>boolean</code> | Whether to allow deleting saves     |
| **`maxSavedGames`**  | <code>number</code>  | Maximum number of saves to show     |


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |

</docgen-api>
