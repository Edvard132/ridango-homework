export interface Cocktail {
  hiddenName?: string;
  instructions?: string;
  attemptsLeft?: number;
  hints?: string[];
  score?: number;
  result?: string;
}

export interface GuessRequest {
  guess: string;
}
